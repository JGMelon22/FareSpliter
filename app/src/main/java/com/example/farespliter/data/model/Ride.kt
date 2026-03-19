package com.example.farespliter.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rides")
data class Ride(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val appName: String,
    val totalFare: Double,
    val date: Long // Unix timestamp in ms (better Android Support with room)
)