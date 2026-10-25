package com.horizon.doveinventory.feature_auth.presentation.auth.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.horizon.doveinventory.R
import com.horizon.doveinventory.core.presentation.topappbar.DefaultBackButtonTopAppBar
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    RegisterContent(
        navController,
        viewModel
    )
}

@Composable
fun RegisterContent(
    navController: NavController,
    viewModel: RegisterViewModel
) {

    // Snack bar
    val snackbarHostState = remember { SnackbarHostState() }

    // Runs once when this screen loads. This is where one time events like navigating away
    // or showing a snackbar are handled, not state that redraws the UI.
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is RegisterUiEvent.Register -> {
                    navController.navigateUp()
                }
                is RegisterUiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            DefaultBackButtonTopAppBar(
                navController // For popping the navigation back stack when back button is selected.
            )
        },
        snackbarHost = {
            snackbarHostState
        },
        content = { paddingValues ->
            Box(modifier = Modifier
                .padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    content = {
                        Row(
                            modifier = Modifier
                                .padding(4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            content = {
                                Icon(
                                    modifier = Modifier
                                        .size(80.dp),
                                    imageVector = Icons.Rounded.AccountCircle,
                                    contentDescription = stringResource(R.string.profile_image),
                                )
                            }
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            content = {
                                Text(
                                    text = stringResource(R.string.register),
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                        )
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            value = viewModel.firstName.value,
                            onValueChange = {
                                viewModel.onEvent(RegisterEvent.EnteredFirstName(it))
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.AccountCircle,
                                    contentDescription = stringResource(R.string.first_name)
                                )
                            },
                            label = {
                                Text(text = stringResource(R.string.first_name))
                            }
                        )
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            value = viewModel.lastName.value,
                            onValueChange = {
                                viewModel.onEvent(RegisterEvent.EnteredLastName(it))
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.AccountCircle,
                                    contentDescription = stringResource(R.string.last_name)
                                )
                            },
                            label = {
                                Text(text = stringResource(R.string.last_name))
                            }
                        )
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            value = viewModel.userName.value,
                            onValueChange = {
                                viewModel.onEvent(RegisterEvent.EnteredUserName(it))
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Email,
                                    contentDescription = stringResource(R.string.username)
                                )
                            },
                            label = {
                                Text(text = stringResource(R.string.username))
                            }
                        )
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            value = viewModel.password.value,
                            onValueChange = {
                                viewModel.onEvent(RegisterEvent.EnteredPassword(it))
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Password,
                                    contentDescription = stringResource(R.string.password)
                                )
                            },
                            label = {
                                Text(text = stringResource(R.string.password))
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            content = {
                                Button(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    onClick = {
                                        viewModel.onEvent(RegisterEvent.Register)
                                    }
                                ) {
                                    Text(text = stringResource(R.string.submit))
                                }
                            }
                        )
                    }
                )
            }
        }
    )
}