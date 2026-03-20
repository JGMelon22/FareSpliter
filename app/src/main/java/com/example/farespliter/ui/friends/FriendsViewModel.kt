package com.example.farespliter.ui.friends

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.farespliter.data.AppDatabase
import com.example.farespliter.data.model.Friend
import com.example.farespliter.repository.RideRepository
import kotlinx.coroutines.launch

class FriendsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: RideRepository = RideRepository(AppDatabase.getInstance(application))

    val friends: LiveData<List<Friend>> = repository.getAllFriends()

    fun addFriend(name: String) {
        if (name.isBlank()) return
        viewModelScope.launch {
            repository.addFriend(name)
        }
    }

    fun deleteFriend(friendId: Long) {
        viewModelScope.launch {
            repository.deleteFriend(friendId)
        }
    }
}