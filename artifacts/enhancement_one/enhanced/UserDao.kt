package com.horizon.doveinventory.feature_auth.data.data_source

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.horizon.doveinventory.feature_auth.domain.model.User

@Dao
interface UserDao {

    @Query("SELECT * FROM user WHERE userID = :userID")
    suspend fun getUserByID(userID: String): User?

    // Upsert to prevent crashing in case of duplicate User data.
    @Upsert
    suspend fun upsertUser(user:User)
}