package com.example.farespliter.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.farespliter.data.model.Friend

@Dao
interface FriendDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(friend: Friend)

    @Query("DELETE FROM friends WHERE id = :friendId")
    suspend fun deleteById(friendId: Long)

    @Update
    suspend fun update(friend: Friend)

    @Query("SELECT * FROM friends ORDER BY name ASC")
    fun getAllFriends(): LiveData<List<Friend>>

    @Query("SELECT * FROM friends ORDER BY name ASC")
    suspend fun getAllFriendsOnce(): List<Friend>
}