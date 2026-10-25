package com.horizon.doveinventory.feature_auth.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.horizon.doveinventory.feature_auth.domain.model.User

@Database(
    entities = [User::class],
    version = 1 // First schema for this table
)
abstract class UserDatabase: RoomDatabase() {

    abstract val userDao: UserDao

    companion object {
        const val DATABASE_NAME = "user_db"
    }
}