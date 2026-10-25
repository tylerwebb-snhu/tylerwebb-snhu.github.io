package com.horizon.doveinventory.feature_auth.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.horizon.doveinventory.feature_auth.data.data_source.UserDao
import com.horizon.doveinventory.feature_auth.domain.model.InvalidUserException
import com.horizon.doveinventory.feature_auth.domain.model.User
import com.horizon.doveinventory.feature_auth.domain.repository.UserRepository
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class UserRepositoryImpl(
    private val dao: UserDao,
    private val firebaseAuth: FirebaseAuth
): UserRepository {

    override suspend fun registerUser(
        firstName: String,
        lastName: String,
        userName: String,
        password: String
    ): User {
        // createUserWithEmailAndPassword is a callback. Wrap it in a coroutine so the rest
        // of the data layer can call it the same way
        val firebaseUID = suspendCancellableCoroutine<String> { continuation ->
            firebaseAuth.createUserWithEmailAndPassword(userName, password)
                .addOnCompleteListener { task ->
                    // Bridges the Firebase callback back into the suspend function
                    if (task.isSuccessful) {
                        val uid = task.result?.user?.uid
                        if (uid != null) {
                            continuation.resume(uid)
                        } else {
                            continuation.resumeWithException(
                                InvalidUserException("Firebase did not return a user after registration")
                            )
                        }
                    } else {
                        continuation.resumeWithException(
                            task.exception ?: InvalidUserException("Registration failed")
                        )
                    }
                }
        }

        val user = User(
            userID = firebaseUID,
            firstName = firstName,
            lastName = lastName,
            userName = userName
        )

        dao.upsertUser(user)

        return user
    }

    override suspend fun getUserByID(userID: String): User? {
        return dao.getUserByID(userID)
    }
}