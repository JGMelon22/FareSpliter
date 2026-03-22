package com.example.farespliter.ui.rides


import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.farespliter.R
import com.example.farespliter.ui.friends.FriendsViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddRideActivity : AppCompatActivity() {

    private lateinit var ridesViewModel: RidesViewModel
    private lateinit var friendsViewModel: FriendsViewModel
    private lateinit var participantsAdapter: ParticipantsAdapter

    private var selectedDateMs: Long = System.currentTimeMillis()
    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ride)

        ridesViewModel = ViewModelProvider(this)[RidesViewModel::class.java]
        friendsViewModel = ViewModelProvider(this)[FriendsViewModel::class.java]

        setupToolbar()
        setupAppNameDropDown()
        setupDatePicker()
        setupParticipantsList()
        setupSaveButton()
        observeFriends()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.title_add_ride)
    }

    private fun setupAppNameDropDown() {
        val apps = resources.getStringArray(R.array.ride_apps)
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, apps)
        val etAppName = findViewById<AutoCompleteTextView>(R.id.etAppName)
        etAppName.setAdapter(adapter)
        etAppName.setText(apps[0], false) // Uber pre-selected by default
    }

    private fun setupDatePicker() {
        val etDate = findViewById<TextInputEditText>(R.id.etDate)

        // Show today's date as default
        etDate.setText(dateFormatter.format(selectedDateMs))

        etDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, year, month, day ->
                    calendar.set(year, month, day, 0, 0, 0)
                    calendar.set(Calendar.MILLISECOND, 0)
                    selectedDateMs = calendar.timeInMillis
                    etDate.setText(dateFormatter.format(selectedDateMs))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun setupParticipantsList() {
        participantsAdapter = ParticipantsAdapter()
        val rv = findViewById<RecyclerView>(R.id.rvParticipants)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = participantsAdapter
        rv.isNestedScrollingEnabled = false

        // Update per-person amount when checkbox change
        participantsAdapter.registerAdapterDataObserver(
            object : RecyclerView.AdapterDataObserver() {
                override fun onChanged() = updateEachPays()
            }
        )
    }

    private fun updateEachPays() {
        val fareText = findViewById<TextInputEditText>(R.id.etFare)
            .text?.toString() ?: return
        val fare = fareText.toDoubleOrNull() ?: return
        val count = participantsAdapter.getSelectedIds().size
        val tvEachPays = findViewById<TextView>(R.id.tvEachPays)

        if (count > 0) {
            tvEachPays.text = String.format(
                Locale.getDefault(), "R$ %.2f", fare / count
            )
        } else {
            tvEachPays.text = "-"
        }
    }

    private fun setupSaveButton() {
        val btnSave = findViewById<MaterialButton>(R.id.btnSaveRide)
        btnSave.setOnClickListener {
            if (validateAndSave()) finish()
        }
    }

    private fun validateAndSave(): Boolean {
        val tilAppName = findViewById<TextInputLayout>(R.id.tilAppName)
        val tilFare = findViewById<TextInputLayout>(R.id.tilFare)
        val appName = findViewById<AutoCompleteTextView>(R.id.etAppName)
            .text?.toString() ?: ""
        val fareText = findViewById<TextInputEditText>(R.id.etFare)
            .text?.toString() ?: ""

        var valid = true

        if (appName.isBlank()) {
            tilAppName.error = getString(R.string.error_app_name)
            valid = false
        } else {
            tilAppName.error = null
        }

        val fare = fareText.toDoubleOrNull()
        if (fare == null || fare <= 0) {
            tilFare.error = getString(R.string.error_fare)
            valid = false
        } else {
            tilFare.error = null
        }

        val participantIds = participantsAdapter.getSelectedIds()
        if (participantIds.isEmpty()) {
            valid = false
        }

        if (valid) {
            ridesViewModel.addRide(
                appName = appName,
                totalFare = fare!!,
                date = selectedDateMs,
                participantIds = participantIds
            )
        }

        return valid
    }

    private fun observeFriends() {
        friendsViewModel.friends.observe(this) { friends ->
            participantsAdapter.submitList(friends)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}