package com.sociusfit.app.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sociusfit.core.ui.theme.Spacing
import com.sociusfit.feature.auth.presentation.splash.SplashDestination
import com.sociusfit.feature.auth.presentation.splash.SplashViewModel

/**
 * Splash Screen
 *
 * Prima schermata mostrata all'avvio dell'app.
 * Verifica l'autenticazione e naviga alla schermata appropriata.
 *
 * IMPORTANTE: Il ViewModel deve essere iniettato dall'esterno (:app module).
 *
 * @param onNavigateToLogin Callback per navigare al login
 * @param onNavigateToHome Callback per navigare alla home
 * @param onNavigateToOnboarding Callback per navigare all'onboarding
 * @param viewModel ViewModel iniettato dal chiamante
 */
@Composable
fun SplashScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToOnboarding: () -> Unit,
    viewModel: SplashViewModel  // ← NO default parameter!
) {
    val destination by viewModel.destination.collectAsStateWithLifecycle()

    LaunchedEffect(destination) {
        when (destination) {
            SplashDestination.Login -> {
                onNavigateToLogin()
                viewModel.onNavigationHandled()
            }
            SplashDestination.Home -> {
                onNavigateToHome()
                viewModel.onNavigationHandled()
            }
            SplashDestination.Onboarding -> {
                onNavigateToOnboarding()
                viewModel.onNavigationHandled()
            }
            null -> { }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.primary
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.large),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "SF",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(modifier = Modifier.height(Spacing.medium))

            Text(
                text = "SociusFit",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(modifier = Modifier.height(Spacing.small))

            Text(
                text = "Trova il tuo compagno di sport",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(Spacing.large * 2))

            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}