package com.example.farespliter.ui.rides

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.farespliter.R
import com.example.farespliter.data.model.Friend

class ParticipantsAdapter(
    private val onSelectionChanged: () -> Unit
) :
    ListAdapter<Friend, ParticipantsAdapter.ParticipantViewHolder>(DiffCallback) {

    private val checkedIds = mutableSetOf<Long>()

    fun getSelectedIds(): List<Long> = checkedIds.toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_participant_checkbox, parent, false)
        return ParticipantViewHolder(view)
    }

    override fun onBindViewHolder(holder: ParticipantViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ParticipantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cb = itemView.findViewById<CheckBox>(R.id.cbParticipant)
        private val tvName = itemView.findViewById<TextView>(R.id.tvParticipantName)

        fun bind(friend: Friend) {
            tvName.text = friend.name

            // Restore state without trigger listener
            cb.setOnCheckedChangeListener(null)
            cb.isChecked = checkedIds.contains(friend.id)

            cb.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) checkedIds.add(friend.id)
                else checkedIds.remove(friend.id)
                onSelectionChanged()
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Friend>() {
        override fun areItemsTheSame(old: Friend, new: Friend) = old.id == new.id
        override fun areContentsTheSame(old: Friend, new: Friend) = old == new
    }
}