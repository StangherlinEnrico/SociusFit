package com.sociusfit.app.presentation.auth.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.navigation.NavHostController
import com.sociusfit.app.core.util.UiState
import com.sociusfit.app.core.util.showToast
import com.sociusfit.app.presentation.components.SFButton
import com.sociusfit.app.presentation.components.SFCenteredTopAppBar
import com.sociusfit.app.presentation.components.SFLoadingIndicator
import com.sociusfit.app.presentation.components.SFPasswordTextField
import com.sociusfit.app.presentation.components.SFTextField
import com.sociusfit.app.presentation.navigation.navigateToProfileSetup
import com.sociusfit.app.presentation.theme.spacing
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreen(
    navController: NavHostController,
    viewModel: RegisterViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val name by viewModel.name.collectAsState()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val confirmPassword by viewModel.confirmPassword.collectAsState()
    val nameError by viewModel.nameError.collectAsState()
    val emailError by viewModel.emailError.collectAsState()
    val passwordError by viewModel.passwordError.collectAsState()
    val confirmPasswordError by viewModel.confirmPasswordError.collectAsState()

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(uiState) {
        when (uiState) {
            is UiState.Success -> {
                context.showToast("Registrazione completata")
                navController.navigateToProfileSetup()
                viewModel.resetUiState()
            }

            is UiState.Error -> {
                context.showToast((uiState as UiState.Error).message)
            }

            else -> {}
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(MaterialTheme.spacing.medium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            SFCenteredTopAppBar(
                title = "Registrazione",
                onNavigationClick = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            Text(
                text = "Crea il tuo account",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))

            Text(
                text = "Inizia a trovare il tuo partner sportivo",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            SFTextField(
                value = name,
                onValueChange = viewModel::onNameChange,
                label = "Nome",
                placeholder = "Il tuo nome",
                leadingIcon = Icons.Default.Person,
                isError = nameError != null,
                errorMessage = nameError,
                enabled = !uiState.isLoading,
                imeAction = ImeAction.Next,
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            SFTextField(
                value = email,
                onValueChange = viewModel::onEmailChange,
                label = "Email",
                placeholder = "esempio@email.com",
                leadingIcon = Icons.Default.Email,
                isError = emailError != null,
                errorMessage = emailError,
                enabled = !uiState.isLoading,
                imeAction = ImeAction.Next,
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            SFPasswordTextField(
                value = password,
                onValueChange = viewModel::onPasswordChange,
                label = "Password",
                isError = passwordError != null,
                errorMessage = passwordError,
                imeAction = ImeAction.Next,
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            SFPasswordTextField(
                value = confirmPassword,
                onValueChange = viewModel::onConfirmPasswordChange,
                label = "Conferma Password",
                isError = confirmPasswordError != null,
                errorMessage = confirmPasswordError,
                imeAction = ImeAction.Done,
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        viewModel.register()
                    }
                )
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            SFButton(
                text = "Registrati",
                onClick = viewModel::register,
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            TextButton(
                onClick = { navController.popBackStack() },
                enabled = !uiState.isLoading
            ) {
                Text("Hai già un account? Accedi")
            }
        }

        if (uiState.isLoading) {
            SFLoadingIndicator()
        }
    }
}