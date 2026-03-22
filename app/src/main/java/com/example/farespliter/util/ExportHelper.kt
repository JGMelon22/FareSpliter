package com.example.farespliter.util

import android.content.Context
import android.content.Intent
import android.os.Environment
import com.example.farespliter.ui.summary.SummaryItem
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ExportHelper {
    fun buildSummaryText(
        items: List<SummaryItem>,
        totalSpent: Double,
        monthLabel: String
    ): String {
        val sb = StringBuilder()
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        sb.appendLine("================================")
        sb.appendLine("  FareSplitter — $monthLabel")
        sb.appendLine("  Generated: ${formatter.format(Date())}")
        sb.appendLine("================================")
        sb.appendLine()
        sb.appendLine("Total spent: R$ %.2f".format(totalSpent))
        sb.appendLine()
        sb.appendLine("Amount owed to you:")
        sb.appendLine("================================")

        items.forEach { item ->
            val line = "%-20s R$ %,.2f (%d rides)".format(
                item.friend.name,
                item.amountOwed,
                item.rideCound
            )
            sb.appendLine(line)
        }

        sb.appendLine("--------------------------------")
        sb.appendLine()
        sb.appendLine("Sent via FareSplitter")

        return sb.toString()
    }

    fun shareAsText(context: Context, text: String, monthLabel: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "FareSplitter - $monthLabel")
            putExtra(Intent.EXTRA_TEXT, text)
        }
        context.startActivity(
            Intent.createChooser(intent, "Share summary via")
        )
    }

    fun saveToFile(context: Context, text: String, monthLabel: String): Boolean {
        return try {
            val fileName = "faresplitter_${monthLabel.replace(" ", "_")}.txt"
            val dir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            val file = File(dir, fileName)
            FileWriter(file).use { it.write(text) }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}