package com.sociusfit.app.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sociusfit.core.ui.components.SFButton
import com.sociusfit.core.ui.components.SFPasswordTextField
import com.sociusfit.core.ui.components.SFTextField
import com.sociusfit.core.ui.theme.Spacing
import com.sociusfit.feature.auth.presentation.register.RegisterNavigationEvent
import com.sociusfit.feature.auth.presentation.register.RegisterViewModel

/**
 * Register Screen - FIXED
 */
@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToOnboarding: () -> Unit,
    viewModel: RegisterViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val navigationEvent by viewModel.navigationEvent.collectAsStateWithLifecycle()

    // Gestisce navigazione tramite event
    LaunchedEffect(navigationEvent) {
        when (navigationEvent) {
            RegisterNavigationEvent.NavigateToOnboarding -> {
                onNavigateToOnboarding()
                viewModel.onNavigationEventConsumed()
            }
            null -> { /* No navigation */ }
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = Spacing.large),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Crea Account",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(Spacing.small))

            Text(
                text = "Unisciti a SociusFit",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(Spacing.large * 2))

            SFTextField(
                value = uiState.firstName,
                onValueChange = viewModel::onFirstNameChanged,
                label = "Nome",
                placeholder = "Mario",
                errorMessage = if (!uiState.isFirstNameValid && uiState.firstName.isNotEmpty())
                    "Minimo 2 caratteri" else null,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
                onImeAction = { }
            )

            Spacer(modifier = Modifier.height(Spacing.medium))

            SFTextField(
                value = uiState.lastName,
                onValueChange = viewModel::onLastNameChanged,
                label = "Cognome",
                placeholder = "Rossi",
                errorMessage = if (!uiState.isLastNameValid && uiState.lastName.isNotEmpty())
                    "Minimo 2 caratteri" else null,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
                onImeAction = { }
            )

            Spacer(modifier = Modifier.height(Spacing.medium))

            SFTextField(
                value = uiState.email,
                onValueChange = viewModel::onEmailChanged,
                label = "Email",
                placeholder = "mario.rossi@example.com",
                errorMessage = if (!uiState.isEmailValid && uiState.email.isNotEmpty())
                    uiState.emailError else null,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
                onImeAction = { }
            )

            Spacer(modifier = Modifier.height(Spacing.medium))

            SFPasswordTextField(
                value = uiState.password,
                onValueChange = viewModel::onPasswordChanged,
                label = "Password",
                placeholder = "Minimo 8 caratteri",
                errorMessage = if (!uiState.isPasswordValid && uiState.password.isNotEmpty())
                    uiState.passwordError else null,
                imeAction = ImeAction.Done,
                onImeAction = {
                    if (uiState.isFormValid && !uiState.isLoading) {
                        viewModel.onRegisterClick()
                    }
                }
            )

            Spacer(modifier = Modifier.height(Spacing.large * 2))

            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(48.dp))
            } else {
                SFButton(
                    text = "Registrati",
                    onClick = viewModel::onRegisterClick,
                    enabled = uiState.isFormValid,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(Spacing.medium))

            TextButton(
                onClick = onNavigateToLogin,
                enabled = !uiState.isLoading
            ) {
                Text("Hai già un account? Accedi")
            }

            if (uiState.error != null) {
                Spacer(modifier = Modifier.height(Spacing.medium))
                Text(
                    text = uiState.error!!,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}