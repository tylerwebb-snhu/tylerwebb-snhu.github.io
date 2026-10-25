package com.horizon.doveinventory.feature_auth.domain.use_case

data class UserUseCases(
    val registerUserUseCase : RegisterUserUseCase,
    val getUserByIDUseCase: GetUserIDUseCase
)