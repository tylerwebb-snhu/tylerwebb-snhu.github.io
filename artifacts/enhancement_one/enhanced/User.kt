package com.horizon.doveinventory.feature_auth.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    // Firebase UID. Not a generated val so it maps to Firebase.
    @PrimaryKey val userID: String,
    val firstName: String,
    val lastName: String,
    val userName: String
    // No password val is needed. Firebase owns all credentials. Room only needs profile info.
)

class InvalidUserException (message: String): Exception(message)