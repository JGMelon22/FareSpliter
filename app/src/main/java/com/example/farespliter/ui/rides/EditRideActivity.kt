package com.example.farespliter.ui.rides

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.farespliter.R
import com.example.farespliter.data.model.Ride
import com.example.farespliter.ui.friends.FriendsViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditRideActivity : AppCompatActivity() {

    private lateinit var ridesViewModel: RidesViewModel
    private lateinit var friendsViewModel: FriendsViewModel
    private lateinit var participantsAdapter: ParticipantsAdapter

    private var selectedDateMs: Long = System.currentTimeMillis()
    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    // Guarda o rideId recebido pelo Intent
    private var rideId: Long = -1L

    companion object {
        const val EXTRA_RIDE_ID = "extra_ride_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_ride)

        // Lê o rideId passado pela Activity anterior
        rideId = intent.getLongExtra(EXTRA_RIDE_ID, -1L)
        if (rideId == -1L) {
            finish() // Proteção: se não veio id, fecha a tela
            return
        }

        ridesViewModel = ViewModelProvider(this)[RidesViewModel::class.java]
        friendsViewModel = ViewModelProvider(this)[FriendsViewModel::class.java]

        setupToolbar()
        setupAppNameDropDown()
        setupDatePicker()
        setupFareWatcher()
        setupParticipantsList()
        setupSaveButton()
        observeFriends()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.title_edit_ride)
    }

    private fun setupAppNameDropDown() {
        val apps = resources.getStringArray(R.array.ride_apps)
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, apps)
        val etAppName = findViewById<AutoCompleteTextView>(R.id.etAppName)
        etAppName.setAdapter(adapter)
    }

    private fun setupDatePicker() {
        val etDate = findViewById<TextInputEditText>(R.id.etDate)
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
        participantsAdapter = ParticipantsAdapter { updateEachPays() }
        val rv = findViewById<RecyclerView>(R.id.rvParticipants)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = participantsAdapter
        rv.isNestedScrollingEnabled = false
    }

    private fun updateEachPays() {
        val fareText = findViewById<TextInputEditText>(R.id.etFare)
            .text?.toString() ?: return
        val fare = fareText.toDoubleOrNull() ?: return
        val count = participantsAdapter.getSelectedIds().size
        val tvEachPays = findViewById<TextView>(R.id.tvEachPays)
        if (count > 0) {
            tvEachPays.text = String.format(Locale.getDefault(), "R$ %.2f", fare / count)
        } else {
            tvEachPays.text = "-"
        }
    }

    private fun setupFareWatcher() {
        findViewById<TextInputEditText>(R.id.etFare)
            .doAfterTextChanged { updateEachPays() }
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
        if (participantIds.isEmpty()) valid = false

        if (valid) {
            ridesViewModel.updateRide(
                // 🆕 .copy() mantém o id original, atualiza os outros campos
                ride = Ride(
                    id = rideId,
                    appName = appName.trim(),
                    totalFare = fare!!,
                    date = selectedDateMs
                ),
                participantIds = participantIds
            )
        }

        return valid
    }

    private fun observeFriends() {
        friendsViewModel.friends.observe(this) { friends ->
            participantsAdapter.submitList(friends)

            ridesViewModel.getRideById(rideId) { ride ->
                ride ?: return@getRideById

                val etAppName = findViewById<AutoCompleteTextView>(R.id.etAppName)
                etAppName.setText(ride.appName, false)

                val etFare = findViewById<TextInputEditText>(R.id.etFare)
                etFare.setText(ride.totalFare.toString())

                selectedDateMs = ride.date
                val etDate = findViewById<TextInputEditText>(R.id.etDate)
                etDate.setText(dateFormatter.format(selectedDateMs))
            }

            // Após carregar os amigos, pré-preenche os dados da corrida
            ridesViewModel.getParticipantsForRide(rideId) { currentParticipants ->
                participantsAdapter.preSelectIds(
                    currentParticipants.map { it.id }
                )
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}