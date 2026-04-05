package com.example.farespliter.ui.friends

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionScene
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.farespliter.R
import com.example.farespliter.data.model.Friend

class FriendsAdapter(
    private val onEditClick: (Friend) -> Unit,
    private val onDeleteClick: (Friend) -> Unit
) : ListAdapter<Friend, FriendsAdapter.FriendViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_friend, parent, false)
        return FriendViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvInitials = itemView.findViewById<TextView>(R.id.tvInitials)
        private val tvName = itemView.findViewById<TextView>(R.id.tvFriendName)
        private val btnDelete = itemView.findViewById<TextView>(R.id.btnDelete)

        fun bind(friend: Friend) {
            tvName.text = friend.name
            tvInitials.text = friend.name
                .split(" ")
                .filter { it.isNotBlank() }
                .take(2)
                .joinToString("") { it.first().uppercase() } // Eg: "Ana Lima" -> "AL"

            itemView.setOnClickListener {
                onEditClick(friend)
            }

            btnDelete.setOnClickListener {
                onDeleteClick(friend)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Friend>() {
        override fun areItemsTheSame(old: Friend, new: Friend) = old.id == new.id
        override fun areContentsTheSame(old: Friend, new: Friend) = old == new
    }
}