package com.example.farespliter.ui.rides

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.farespliter.data.AppDatabase
import com.example.farespliter.data.model.Friend
import com.example.farespliter.data.model.Ride
import com.example.farespliter.data.model.RideParticipant
import com.example.farespliter.repository.RideRepository
import kotlinx.coroutines.launch

class RidesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: RideRepository = RideRepository(AppDatabase.getInstance(application))

    val rides: LiveData<List<Ride>> = repository.getAllRides()

    fun addRide(
        appName: String,
        totalFare: Double,
        date: Long,
        participantIds: List<Long>
    ) {
        if (appName.isBlank() || totalFare <= 0 || participantIds.isEmpty()) return
        viewModelScope.launch {
            repository.addRide(
                ride = Ride(
                    appName = appName.trim(),
                    totalFare = totalFare,
                    date = date
                ),
                participantIds = participantIds
            )
        }
    }

    fun deleteRide(rideId: Long) {
        viewModelScope.launch {
            repository.deleteRide(rideId)
        }
    }

    fun getRideById(rideId: Long, callback: (Ride?) -> Unit) {
        viewModelScope.launch {
            callback(repository.getRideById(rideId))
        }
    }

    fun updateRide(
        ride: Ride,
        participantIds: List<Long>
    ) {
        if (ride.appName.isBlank() || ride.totalFare <= 0 || participantIds.isEmpty()) return
        viewModelScope.launch {
            repository.updateRide(ride, participantIds)
        }
    }

    fun getParticipantsForRide(
        rideId: Long,
        callback: (List<Friend>) -> Unit
    ) {
        viewModelScope.launch {
            val participants = repository.getParticipantsForRide(rideId)
            callback(participants)
        }
    }
}