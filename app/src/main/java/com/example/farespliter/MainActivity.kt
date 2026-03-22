package com.example.farespliter

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.farespliter.ui.friends.FriendsActivity
import com.example.farespliter.ui.rides.AddRideActivity
import com.example.farespliter.ui.rides.RidesAdapter
import com.example.farespliter.ui.rides.RidesViewModel
import com.example.farespliter.ui.summary.SummaryActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: RidesViewModel
    private lateinit var adapter: RidesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[RidesViewModel::class.java]

        setupToolbar()
        setupMonthHeader()
        setupRecyclerView()
        setupFab()
        observeRides()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_friends -> {
                startActivity(Intent(this, FriendsActivity::class.java))
                true
            }

            R.id.action_summary -> {
                startActivity(Intent(this, SummaryActivity::class.java))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupToolbar() {
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    private fun setupMonthHeader() {
        val label = SimpleDateFormat("MMM yyy", Locale.getDefault())
            .format(Date())
            .uppercase()
        findViewById<TextView>(R.id.tvMonthHeader).text = label
    }

    private fun setupRecyclerView() {
        adapter = RidesAdapter { rideId, callBack ->
            viewModel.getParticipantsForRide(rideId, callBack)
        }
        val rv = findViewById<RecyclerView>(R.id.rvRides)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
    }

    private fun setupFab() {
        val fab = findViewById<FloatingActionButton>(R.id.fabAddRide)
        fab.setOnClickListener {
            startActivity(Intent(this, AddRideActivity::class.java))
        }
    }

    private fun observeRides() {
        viewModel.rides.observe(this) { rides ->
            adapter.submitList(rides)

            val tvEmpty = findViewById<TextView>(R.id.tvEmptyState)
            tvEmpty.visibility = if (rides.isEmpty()) View.VISIBLE else View.GONE
        }
    }
}