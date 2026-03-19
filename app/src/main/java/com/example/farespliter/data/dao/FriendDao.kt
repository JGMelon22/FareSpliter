package com.example.farespliter.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.farespliter.data.model.Friend

@Dao
interface FriendDao{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(friend: Friend)

    @Query("DELETE FROM friends WHERE id = :friendId")
    suspend fun deleteById(friendId: Long)

    @Query("SELECT * FROM friends ORDER BY name ASC")
    fun getAllFriends() : LiveData<List<Friend>>
}