package com.example.farespliter.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.farespliter.data.dao.FriendDao
import com.example.farespliter.data.dao.RideDao
import com.example.farespliter.data.dao.RideParticipantDao
import com.example.farespliter.data.model.Friend
import com.example.farespliter.data.model.Ride
import com.example.farespliter.data.model.RideParticipant

@Database(
    entities = [Friend::class, Ride::class, RideParticipant::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun friendDao(): FriendDao
    abstract fun rideDao(): RideDao
    abstract fun rideParticipantDao(): RideParticipantDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "faresplitter.db"
                ).build().also { INSTANCE = 1 }
            }
        }
    }
}