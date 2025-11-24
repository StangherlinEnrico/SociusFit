package com.sociusfit.app.presentation.auth.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sociusfit.app.presentation.components.*
import com.sociusfit.app.presentation.navigation.Routes
import com.sociusfit.app.presentation.theme.spacing
import org.koin.androidx.compose.koinViewModel

/**
 * Schermata di Registrazione di SociusFit
 *
 * Features:
 * - Registrazione con nome, cognome, email e password
 * - Conferma password
 * - Pulsanti OAuth (Google, Apple)
 * - Link a login
 * - Validazione visuale campi
 *
 * @param navController Controller per la navigazione
 * @param viewModel ViewModel per gestire la logica
 */
@Composable
fun RegisterScreen(
    navController: NavHostController,
    viewModel: RegisterViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Gestione successo registrazione
    LaunchedEffect(uiState.isRegistrationSuccessful) {
        if (uiState.isRegistrationSuccessful) {
            navController.navigate(Routes.PROFILE) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    // Gestione errori generici
    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackbarHostState.showSnackbar(
                message = error,
                duration = SnackbarDuration.Short
            )
            viewModel.clearError()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = MaterialTheme.spacing.large)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.huge))

            // Logo e titolo
            Text(
                text = "Crea il tuo account",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            Text(
                text = "Inizia a trovare il tuo partner sportivo",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))

            // Nome field
            SFTextField(
                value = uiState.firstName,
                onValueChange = viewModel::onFirstNameChanged,
                label = "Nome",
                placeholder = "Mario",
                leadingIcon = Icons.Default.Person,
                isError = uiState.firstNameError != null,
                errorMessage = uiState.firstNameError,
                keyboardType = androidx.compose.ui.text.input.KeyboardType.Text,
                imeAction = ImeAction.Next,
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            // Cognome field
            SFTextField(
                value = uiState.lastName,
                onValueChange = viewModel::onLastNameChanged,
                label = "Cognome",
                placeholder = "Rossi",
                leadingIcon = Icons.Default.Person,
                isError = uiState.lastNameError != null,
                errorMessage = uiState.lastNameError,
                keyboardType = androidx.compose.ui.text.input.KeyboardType.Text,
                imeAction = ImeAction.Next,
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            // Email field
            SFTextField(
                value = uiState.email,
                onValueChange = viewModel::onEmailChanged,
                label = "Email",
                placeholder = "esempio@email.com",
                leadingIcon = Icons.Default.Email,
                isError = uiState.emailError != null,
                errorMessage = uiState.emailError,
                keyboardType = androidx.compose.ui.text.input.KeyboardType.Email,
                imeAction = ImeAction.Next,
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            // Password field
            SFPasswordTextField(
                value = uiState.password,
                onValueChange = viewModel::onPasswordChanged,
                label = "Password",
                isError = uiState.passwordError != null,
                errorMessage = uiState.passwordError,
                imeAction = ImeAction.Next,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            // Conferma Password field
            SFPasswordTextField(
                value = uiState.confirmPassword,
                onValueChange = viewModel::onConfirmPasswordChanged,
                label = "Conferma Password",
                isError = uiState.confirmPasswordError != null,
                errorMessage = uiState.confirmPasswordError,
                imeAction = ImeAction.Done,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            // Pulsante Registrati
            SFButton(
                text = if (uiState.isLoading) "Registrazione in corso..." else "Registrati",
                onClick = viewModel::onRegisterClick,
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            // Divider con testo "oppure"
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f))
                Text(
                    text = "oppure",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium)
                )
                HorizontalDivider(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            // Pulsanti OAuth
            OAuthButtonsSection(
                isLoading = uiState.isLoading,
                onGoogleClick = viewModel::onGoogleSignInClick,
                onAppleClick = viewModel::onAppleSignInClick
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))

            // Link a login
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Hai già un account?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                SFTextButton(
                    text = "Accedi",
                    onClick = {
                        navController.popBackStack()
                    }
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
        }
    }
}

/**
 * Sezione con pulsanti OAuth per registrazione social
 */
@Composable
private fun OAuthButtonsSection(
    isLoading: Boolean,
    onGoogleClick: () -> Unit,
    onAppleClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium, Alignment.CenterHorizontally)
    ) {
        // Google Sign-In Button
        OutlinedButton(
            onClick = onGoogleClick,
            enabled = !isLoading,
            modifier = Modifier
                .height(48.dp),
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.White,
            ),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Image(
                painter = painterResource(id = com.sociusfit.app.R.drawable.google_logo),
                contentDescription = "Google logo",
                modifier = Modifier.size(24.dp)
            )
        }

        // Apple Sign-In Button
        OutlinedButton(
            onClick = onAppleClick,
            enabled = !isLoading,
            modifier = Modifier
                .height(48.dp),
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.White,
            ),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Image(
                painter = painterResource(id = com.sociusfit.app.R.drawable.apple_logo),
                contentDescription = "Apple logo",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}