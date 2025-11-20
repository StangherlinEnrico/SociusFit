package com.sociusfit.app.presentation.auth.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
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
import com.sociusfit.app.presentation.navigation.navigateToForgotPassword
import com.sociusfit.app.presentation.navigation.navigateToRegister
import com.sociusfit.app.presentation.theme.spacing

/**
 * Schermata di Login di SociusFit
 *
 * Features:
 * - Login con email e password
 * - Pulsanti OAuth (Google, Facebook, Apple)
 * - Link a registrazione
 * - Link "Password dimenticata"
 * - Validazione visuale campi
 *
 * @param navController Controller per la navigazione
 */
@Composable
fun LoginScreen(
    navController: NavHostController
) {
    // State per i campi
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // State per errori di validazione
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    // State per loading
    var isLoading by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize()
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
                text = "SociusFit",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            Text(
                text = "Trova il tuo partner sportivo",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.huge))

            // Email field
            SFTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = null // Reset errore quando l'utente digita
                },
                label = "Email",
                placeholder = "esempio@email.com",
                leadingIcon = Icons.Default.Email,
                isError = emailError != null,
                errorMessage = emailError,
                keyboardType = androidx.compose.ui.text.input.KeyboardType.Email,
                imeAction = ImeAction.Next,
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            // Password field
            SFPasswordTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = null // Reset errore quando l'utente digita
                },
                label = "Password",
                isError = passwordError != null,
                errorMessage = passwordError,
                imeAction = ImeAction.Done,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            // Link "Password dimenticata"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                SFTextButton(
                    text = "Password dimenticata?",
                    onClick = {
                        navController.navigateToForgotPassword()
                    }
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            // Pulsante Login
            SFButton(
                text = if (isLoading) "Accesso in corso..." else "Accedi",
                onClick = {
                    // TODO: Implementare logica di login
                },
                enabled = !isLoading,
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
                isLoading = isLoading,
                onGoogleClick = {
                    // TODO: Implementare Google Sign-In
                },
                onAppleClick = {
                    // TODO: Implementare Apple Sign-In
                }
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))

            // Link a registrazione
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Non hai un account?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                SFTextButton(
                    text = "Registrati",
                    onClick = {
                        navController.navigateToRegister()
                    }
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
        }
    }
}

/**
 * Sezione con pulsanti OAuth per login social
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