package com.sociusfit.app.presentation.auth.forgotpassword

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sociusfit.app.presentation.components.*
import com.sociusfit.app.presentation.theme.spacing
import org.koin.androidx.compose.koinViewModel

/**
 * Schermata per il recupero password dimenticata
 *
 * Features:
 * - Input email per recupero password
 * - Invio email di reset
 * - Navigazione back a login
 *
 * @param navController Controller per la navigazione
 * @param viewModel ViewModel per gestire la logica
 */
@Composable
fun ForgotPasswordScreen(
    navController: NavHostController,
    viewModel: ForgotPasswordViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

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
        topBar = {
            SFTopAppBar(
                title = "Password dimenticata",
                onNavigationClick = {
                    navController.popBackStack()
                }
            )
        },
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

            if (!uiState.isEmailSent) {
                // Schermata di richiesta reset
                ResetPasswordRequestContent(
                    email = uiState.email,
                    emailError = uiState.emailError,
                    isLoading = uiState.isLoading,
                    onEmailChange = viewModel::onEmailChanged,
                    onSendClick = viewModel::onSendClick
                )
            } else {
                // Schermata di conferma invio
                EmailSentConfirmationContent(
                    email = uiState.email,
                    onBackToLoginClick = {
                        navController.popBackStack()
                    }
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
        }
    }
}

/**
 * Contenuto per la richiesta di reset password
 */
@Composable
private fun ResetPasswordRequestContent(
    email: String,
    emailError: String?,
    isLoading: Boolean,
    onEmailChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Icona email
        Icon(
            imageVector = Icons.Default.Email,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

        // Titolo
        Text(
            text = "Recupera la tua password",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

        // Descrizione
        Text(
            text = "Inserisci l'indirizzo email associato al tuo account e ti invieremo un link per reimpostare la password",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))

        // Email field
        SFTextField(
            value = email,
            onValueChange = onEmailChange,
            label = "Email",
            placeholder = "esempio@email.com",
            leadingIcon = Icons.Default.Email,
            isError = emailError != null,
            errorMessage = emailError,
            keyboardType = androidx.compose.ui.text.input.KeyboardType.Email,
            imeAction = ImeAction.Done,
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

        // Pulsante Invia
        SFButton(
            text = if (isLoading) "Invio in corso..." else "Invia link di reset",
            onClick = onSendClick,
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/**
 * Contenuto di conferma invio email
 */
@Composable
private fun EmailSentConfirmationContent(
    email: String,
    onBackToLoginClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Icona successo
        Icon(
            imageVector = Icons.Default.Email,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

        // Titolo
        Text(
            text = "Email inviata!",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

        // Descrizione
        Text(
            text = "Abbiamo inviato un link per reimpostare la password a:",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

        Text(
            text = email,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

        Text(
            text = "Controlla la tua casella email e segui le istruzioni per reimpostare la password",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))

        // Pulsante torna al login
        SFButton(
            text = "Torna al login",
            onClick = onBackToLoginClick,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

        // Note aggiuntiva
        Text(
            text = "Non hai ricevuto l'email? Controlla la cartella spam",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}