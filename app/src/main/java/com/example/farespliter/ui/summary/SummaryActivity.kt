package com.example.farespliter.ui.summary

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.farespliter.R
import com.example.farespliter.util.ExportHelper
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import java.util.Locale

class SummaryActivity : AppCompatActivity() {

    private lateinit var viewModel: SummaryViewModel
    private lateinit var adapter: SummaryAdapter
    private var currentItems: List<SummaryItem> = emptyList()
    private var currentTotal: Double = 0.0

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
            showExportDialog()
        }
    }

    private fun showExportDialog() {
        val montLabel = getString(R.string.title_summary)
        val text = ExportHelper.buildSummaryText(
            items = currentItems,
            totalSpent = currentTotal,
            monthLabel = montLabel
        )

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_export_title))
            .setMessage(getString(R.string.dialog_export_message))
            .setPositiveButton(getString(R.string.btn_share)) { _, _ ->
                ExportHelper.shareAsText(this, text, montLabel)
            }
            .setNeutralButton(getString(R.string.btn_save_file)) { _, _ ->
                val saved = ExportHelper.saveToFile(this, text, montLabel)
                val msg = if (saved)
                    getString(R.string.msg_file_saved)
                else
                    getString((R.string.msg_file_error))
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton(getString(R.string.btn_cancel), null)
            .show()
    }

    private fun observeSummary() {
        viewModel.owedAmounts.observe(this) { amountMap ->
            val items = amountMap.map { (friend, pair) ->
                SummaryItem(
                    friend = friend,
                    amountOwed = pair.first,
                    rideCount = pair.second
                )
            }.sortedByDescending { it.amountOwed }

            val total = amountMap.values.sumOf { it.first }

            currentItems = items
            currentTotal = total

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