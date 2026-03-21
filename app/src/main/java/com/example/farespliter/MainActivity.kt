package com.example.farespliter

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.farespliter.ui.rides.RidesAdapter
import com.example.farespliter.ui.rides.RidesViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: RidesViewModel
    private lateinit var adapter: RidesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[RidesViewModel::class.java]

        setupToolbar()
        setupRecyclerView()
        setupFab()
        observeRides()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
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
            // TODO - Develop RideActivity later
            // startActivity(Intent(this, AddRideActivity::class.javaClass))
        }
    }

    private fun observeRides() {
        viewModel.rides.observe(this) { rides ->
            adapter.submitList(rides)
        }
    }
}