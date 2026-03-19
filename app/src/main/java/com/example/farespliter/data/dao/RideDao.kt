package com.example.farespliter.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.farespliter.data.model.Ride

@Dao
interface RideDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(ride: Ride): Int // returns the generated id

    @Query("DELETE FROM rides WHERE id = :rideId")
    suspend fun deleteById(rideId: Int)

    @Query("SELECT * FROM rides ORDER BY date DESC")
    fun getAllRides() : LiveData<List<Ride>>

    @Query("""
        SELECT * FROM rides
        WHERE date >= :startMs AND date <= :endMs
        ORDER BY date DESC
    """)
    fun getRidesByMonth(startMs: Long, endMs: Long) : LiveData<List<Ride>>
}