package com.example.farespliter.ui.summary

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.farespliter.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import java.util.Locale

class SummaryActivity : AppCompatActivity() {

    private lateinit var viewModel: SummaryViewModel
    private lateinit var adapter: SummaryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        viewModel = ViewModelProvider(this)[SummaryViewModel::class.java]

        setupToolbar()
        setupRecyclerView()
        setupExportButton()
        observeSummary()

        viewModel.loadSummary()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.title_summary)
    }

    private fun setupRecyclerView() {
        adapter = SummaryAdapter()
        val rv = findViewById<RecyclerView>(R.id.rvSummary)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
    }

    private fun setupExportButton() {
        val btnExport = findViewById<MaterialButton>(R.id.btnExport)
        btnExport.setOnClickListener {
            // TODO - Will be configured latter
        }
    }

    private fun observeSummary() {
        viewModel.owedAmounts.observe(this) { amountMap ->
            val items = amountMap.map { (friend, amount) ->
                SummaryItem(
                    friend = friend,
                    amountOwed = amount,
                    rideCound = 0 // TODO - Will be configured latter
                )
            }.sortedByDescending { it.amountOwed }

            val total = amountMap.values.sum()
            findViewById<TextView>(R.id.tvTotalSpent).text =
                String.format(Locale.getDefault(), "R$ %.2f", total)

            adapter.submitList(items)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}