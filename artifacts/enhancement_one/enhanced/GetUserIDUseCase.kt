package com.horizon.doveinventory.feature_auth.domain.use_case

import com.horizon.doveinventory.feature_auth.domain.model.User
import com.horizon.doveinventory.feature_auth.domain.repository.UserRepository

class GetUserIDUseCase(
    private val repository: UserRepository
) {

    suspend operator fun invoke(userID: String): User? {
        return repository.getUserByID(userID)
    }
}