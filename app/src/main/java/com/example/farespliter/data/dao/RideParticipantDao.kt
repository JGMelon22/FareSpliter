package com.example.farespliter.data.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.farespliter.data.model.Friend
import com.example.farespliter.data.model.RideParticipant

interface RideParticipantDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(participant: RideParticipant)

    @Query("DELETE FROM ride_participants WHERE rideId = :rideId")
    suspend fun deleteByRide(rideId: Int)

    @Query("""
        SELECT friends.id, friends.name
        FROM friends 
        INNER JOIN ride_participants ON friends.id = ride_participants.friendId
        WHERE ride_participants.rideId = :rideId
    """)
    suspend fun getFriendsForRide(rideId: Int) : List<Friend>

    @Query("""
        SELECT rides.id, rides.appName, rides.totalFare, rides.date,
               COUNT(ride_participants.friendId) AS participantCount
        FROM rides 
        INNER JOIN ride_participants ON rides.id = ride_participants.rideId
        WHERE ride_participants.friendId = :friendId
        GROUP BY rides.id
    """)
    suspend fun getRidesForFriend(friendId: Int) : List<RideWithCount>

    data class RideWithCount(
        val id: Int,
        val appName: String,
        val totalFare: Double,
        val date: Long,
        val participantCount: Int // need to calculate each person's share
    )
}