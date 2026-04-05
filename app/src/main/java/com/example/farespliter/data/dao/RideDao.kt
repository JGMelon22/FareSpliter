package com.example.farespliter.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.farespliter.data.model.Ride

@Dao
interface RideDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(ride: Ride): Long // returns the generated id

    @Query("DELETE FROM rides WHERE id = :rideId")
    suspend fun deleteById(rideId: Long)

    @Update
    suspend fun update(ride: Ride)

    @Query("SELECT * FROM  rides WHERE id = :rideId")
    suspend fun getRideById(rideId: Long): Ride?


    @Query("SELECT * FROM rides ORDER BY date DESC")
    fun getAllRides(): LiveData<List<Ride>>

    @Query(
        """
        SELECT * FROM rides
        WHERE date >= :startMs AND date <= :endMs
        ORDER BY date DESC
    """
    )
    fun getRidesByMonth(startMs: Long, endMs: Long): LiveData<List<Ride>>
}