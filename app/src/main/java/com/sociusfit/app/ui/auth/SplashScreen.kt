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
 * Splash Screen - FIXED
 *
 * Prima schermata mostrata all'avvio dell'app.
 * Verifica l'autenticazione e naviga alla schermata appropriata.
 *
 * FLUSSO CORRETTO:
 * - Nessun token → Login
 * - Token + profileComplete = false → Onboarding
 * - Token + profileComplete = true → Profile
 *
 * @param onNavigateToLogin Callback per navigare al login
 * @param onNavigateToProfile Callback per navigare al profilo (era onNavigateToHome)
 * @param onNavigateToOnboarding Callback per navigare all'onboarding
 * @param viewModel ViewModel iniettato dal chiamante
 */
@Composable
fun SplashScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToProfile: () -> Unit,  // ← CAMBIATO da onNavigateToHome
    onNavigateToOnboarding: () -> Unit,
    viewModel: SplashViewModel
) {
    val destination by viewModel.destination.collectAsStateWithLifecycle()

    // Gestisce la navigazione quando viene decisa la destinazione
    LaunchedEffect(destination) {
        when (destination) {
            SplashDestination.Login -> {
                onNavigateToLogin()
                viewModel.onNavigationComplete()  // ← CAMBIATO da onNavigationHandled
            }

            SplashDestination.Profile -> {  // ← AGGIUNTO (era Home prima)
                onNavigateToProfile()
                viewModel.onNavigationComplete()
            }

            SplashDestination.Onboarding -> {
                onNavigateToOnboarding()
                viewModel.onNavigationComplete()
            }

            null -> {
                // Ancora in verifica, non fare nulla
            }
        }
    }

    // UI Splash Screen
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Logo o icona dell'app
                Text(
                    text = "SociusFit",
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(Spacing.large))

                // Loading indicator
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(Spacing.medium))

                Text(
                    text = "Caricamento...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}