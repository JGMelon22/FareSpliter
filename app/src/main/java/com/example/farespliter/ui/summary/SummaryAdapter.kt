package com.example.farespliter.ui.summary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.farespliter.R
import com.example.farespliter.data.model.Friend
import java.util.Locale

class SummaryAdapter :
    ListAdapter<SummaryItem, SummaryAdapter.SummaryViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SummaryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_person_summary, parent, false)
        return SummaryViewHolder(view)
    }

    override fun onBindViewHolder(holder: SummaryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class SummaryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvInitials = itemView.findViewById<TextView>(R.id.tvInitials)
        private val tvName = itemView.findViewById<TextView>(R.id.tvFriendName)
        private val tvRideCount = itemView.findViewById<TextView>(R.id.tvRideCount)
        private val tvAmount = itemView.findViewById<TextView>(R.id.tvAmountOwed)

        fun bind(item: SummaryItem) {
            tvInitials.text = item.friend.name
                .split(" ")
                .filter { it.isNotBlank() }
                .take(2)
                .joinToString("") { it.first().uppercase() }

            tvName.text = item.friend.name
            tvRideCount.text = itemView.context.resources.getQuantityString(
                R.plurals.ride_count, item.rideCount, item.rideCount
            )
            tvAmount.text = String.format(
                Locale.getDefault(), "R$ %.2f", item.amountOwed
            )
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<SummaryItem>() {
        override fun areItemsTheSame(old: SummaryItem, new: SummaryItem) =
            old.friend.id == new.friend.id

        override fun areContentsTheSame(old: SummaryItem, new: SummaryItem) =
            old == new
    }
}

data class SummaryItem(
    val friend: Friend,
    val amountOwed: Double,
    val rideCount: Int
)