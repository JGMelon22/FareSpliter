package com.example.farespliter.repository

import androidx.lifecycle.LiveData
import com.example.farespliter.data.AppDatabase
import com.example.farespliter.data.dao.RideParticipantDao.RideWithCount
import com.example.farespliter.data.model.Friend
import com.example.farespliter.data.model.Ride
import com.example.farespliter.data.model.RideParticipant

class RideRepository(private val db: AppDatabase) {

    // Friends
    fun getAllFriends(): LiveData<List<Friend>> =
        db.friendDao().getAllFriends()

    suspend fun addFriend(name: String) {
        db.friendDao().insert(Friend(name = name))
    }

    suspend fun deleteFriend(friendId: Long) {
        db.friendDao().deleteById(friendId)
    }

    // Rides
    fun getAllRides(): LiveData<List<Ride>> =
        db.rideDao().getAllRides()

    fun getRidesByMonth(startMs: Long, endMs: Long): LiveData<List<Ride>> =
        db.rideDao().getRidesByMonth(startMs, endMs)

    suspend fun addRide(ride: Ride, participantIds: List<Long>) {
        val rideId = db.rideDao().insert(ride)
        participantIds.forEach { friendId ->
            db.rideParticipantDao().insert(
                RideParticipant(rideId = rideId, friendId = friendId)
            )
        }
    }

    suspend fun deleteRide(rideId: Long) {
        db.rideDao().deleteById(rideId)
    }

    // Participants
    suspend fun getParticipantsForRide(rideId: Long): List<Friend> =
        db.rideParticipantDao().getFriendsForRide(rideId)

    suspend fun getRidesForFriend(friendId: Long): List<RideWithCount> =
        db.rideParticipantDao().getRidesForFriend(friendId)

    // Summary
    suspend fun calculateOwedAmounts(): Map<Friend, Double> {
        val friends = db.friendDao().getAllFriends().value ?: return emptyMap()
        val result = mutableMapOf<Friend, Double>()

        friends.forEach { friend ->
            val rides = db.rideParticipantDao().getRidesForFriend(friend.id)
            val total = rides.sumOf { it.totalFare / it.participantCount }
            result[friend] = total
        }

        return result
    }
}