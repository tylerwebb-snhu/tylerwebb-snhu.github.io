package com.horizon.doveinventory.feature_auth.presentation.auth.register

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.horizon.doveinventory.feature_auth.domain.model.InvalidUserException
import com.horizon.doveinventory.feature_auth.domain.use_case.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userUseCases: UserUseCases
): ViewModel() {

    private val _firstName = mutableStateOf("")
    val firstName: State<String> = _firstName

    private val _lastName = mutableStateOf("")
    val lastName: State<String> = _lastName

    private val _userName = mutableStateOf("")
    val userName: State<String> = _userName

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    private val _eventFlow = MutableSharedFlow<RegisterUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: RegisterEvent) {
        when(event) {
            is RegisterEvent.EnteredFirstName -> {
                _firstName.value = event.firstName
            }
            is RegisterEvent.EnteredLastName -> {
                _lastName.value = event.lastName
            }
            is RegisterEvent.EnteredPassword -> {
                _password.value = event.password
            }
            is RegisterEvent.EnteredUserName -> {
                _userName.value = event.userName
            }
            is RegisterEvent.Register -> {
                registerUser()
            }
        }
    }

    private fun registerUser() {
        viewModelScope.launch {
            try {
                userUseCases.registerUserUseCase(
                    firstName = _firstName.value,
                    lastName = _lastName.value,
                    userName = _userName.value,
                    password = _password.value
                )
                _eventFlow.emit(RegisterUiEvent.Register)
            } catch (e: InvalidUserException) {
                // Caught separately from Exception below so a validation failure shows a
                // specific message instead of falling into the generic error case
                _eventFlow.emit(
                    RegisterUiEvent.ShowSnackbar(e.message ?: "Invalid registration details")
                )
            } catch (e: Exception) {
                _eventFlow.emit(
                    RegisterUiEvent.ShowSnackbar(e.message ?: "Registration failed")
                )
            }
        }
    }

}

sealed class RegisterUiEvent {
    data class ShowSnackbar(val message: String): RegisterUiEvent()
    object Register: RegisterUiEvent()
}