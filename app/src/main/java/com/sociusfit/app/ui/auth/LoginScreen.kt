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
import com.sociusfit.feature.auth.presentation.login.LoginNavigationEvent
import com.sociusfit.feature.auth.presentation.login.LoginViewModel

/**
 * Login Screen - FIXED
 */
@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToOnboarding: () -> Unit,
    viewModel: LoginViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val navigationEvent by viewModel.navigationEvent.collectAsStateWithLifecycle()

    // Gestisce navigazione tramite event
    LaunchedEffect(navigationEvent) {
        when (navigationEvent) {
            LoginNavigationEvent.NavigateToProfile -> {
                onNavigateToProfile()
                viewModel.onNavigationEventConsumed()
            }
            LoginNavigationEvent.NavigateToOnboarding -> {
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
                text = "Bentornato!",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(Spacing.small))

            Text(
                text = "Accedi per continuare",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(Spacing.large * 2))

            SFTextField(
                value = uiState.email,
                onValueChange = viewModel::onEmailChanged,
                label = "Email",
                placeholder = "mario.rossi@example.com",
                errorMessage = if (!uiState.isEmailValid) uiState.emailError else null,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
                onImeAction = { }
            )

            Spacer(modifier = Modifier.height(Spacing.medium))

            SFPasswordTextField(
                value = uiState.password,
                onValueChange = viewModel::onPasswordChanged,
                label = "Password",
                placeholder = "La tua password",
                imeAction = ImeAction.Done,
                onImeAction = {
                    if (uiState.isFormValid && !uiState.isLoading) {
                        viewModel.onLoginClick()
                    }
                }
            )

            Spacer(modifier = Modifier.height(Spacing.large * 2))

            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(48.dp))
            } else {
                SFButton(
                    text = "Accedi",
                    onClick = viewModel::onLoginClick,
                    enabled = uiState.isFormValid,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(Spacing.medium))

            TextButton(
                onClick = onNavigateToRegister,
                enabled = !uiState.isLoading
            ) {
                Text("Non hai un account? Registrati")
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