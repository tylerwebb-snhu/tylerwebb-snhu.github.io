package com.horizon.doveinventory.feature_auth.domain.repository

import com.horizon.doveinventory.feature_auth.domain.model.User

interface UserRepository {

    suspend fun registerUser(
        firstName: String,
        lastName: String,
        userName: String,
        password: String // only passed to create the Firebase account, never stored.
    ): User

    suspend fun getUserByID(userID: String): User?
}