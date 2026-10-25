package com.horizon.doveinventory.feature_auth.domain.use_case

import com.horizon.doveinventory.feature_auth.domain.model.InvalidUserException
import com.horizon.doveinventory.feature_auth.domain.model.User
import com.horizon.doveinventory.feature_auth.domain.repository.UserRepository

class RegisterUserUseCase(
    private val repository: UserRepository
) {

    suspend operator fun invoke(
        firstName: String,
        lastName: String,
        userName: String,
        password: String
    ): User {
        // Validation lives here instead of in ViewModal to separate business logic from UI layer
        if (firstName.isBlank()) {
            throw InvalidUserException("First name cannot be empty")
        }
        if (lastName.isBlank()) {
            throw InvalidUserException("Last name cannot be empty")
        }
        if (userName.isBlank()) {
            throw InvalidUserException("Username cannot be empty")
        }
        if (password.length < 6) {
            // Matches the minimum password length for Firebase
            throw InvalidUserException("Password must be at least 6 characters long")
        }

        return repository.registerUser(
            firstName = firstName,
            lastName = lastName,
            userName = userName,
            password = password
        )
    }
}