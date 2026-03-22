package com.example.farespliter.ui.rides

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.farespliter.R
import com.example.farespliter.data.model.Friend
import com.example.farespliter.data.model.Ride
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RidesAdapter(
    private val onParticipantsNeeded: (rideId: Long, callBack: (List<Friend>) -> Unit) -> Unit
) : ListAdapter<Ride, RidesAdapter.RideViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RideViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ride, parent, false)
        return RideViewHolder(view)
    }

    override fun onBindViewHolder(holder: RideViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class RideViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvAppName = itemView.findViewById<TextView>(R.id.tvAppName)
        private val tvFare = itemView.findViewById<TextView>(R.id.tvFare)
        private val tvDate = itemView.findViewById<TextView>(R.id.tvDate)
        private val chipGroup = itemView.findViewById<ChipGroup>(R.id.chipGroupParticipants)

        fun bind(ride: Ride) {
            tvAppName.text = ride.appName
            tvFare.text = String.format(Locale.getDefault(), "R$ %.2f", ride.totalFare)
            tvDate.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                .format(Date(ride.date))

            // Clear chips from recycled view
            chipGroup.removeAllViews()

            // Fetch all participants then build the chip
            onParticipantsNeeded(ride.id) { friends ->
                chipGroup.removeAllViews()
                friends.forEach { friend ->
                    val chip = Chip(itemView.context)
                    chip.text = friend.name
                    chip.isClickable = false
                    chipGroup.addView(chip)
                }

                tvDate.text = String.format(
                    Locale.getDefault(),
                    "%s . %d participants",
                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(ride.date)),
                    friends.size
                )
            }
        }

    }

    companion object DiffCallback : DiffUtil.ItemCallback<Ride>() {
        override fun areItemsTheSame(old: Ride, new: Ride) = old.id == new.id
        override fun areContentsTheSame(old: Ride, new: Ride) = old == new
    }
}