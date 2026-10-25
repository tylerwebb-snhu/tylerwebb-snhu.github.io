package com.horizon.doveinventory.feature_auth.presentation.auth.register

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(

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
                /*TODO: Need add user use case for here*/
            }
        }
    }

}

sealed class RegisterUiEvent {
    data class ShowSnackbar(val message: String): RegisterUiEvent()
    object Register: RegisterUiEvent()
}