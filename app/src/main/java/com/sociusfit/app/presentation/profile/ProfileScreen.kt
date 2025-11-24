package com.sociusfit.app.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sociusfit.app.presentation.components.SFLoadingIndicator
import com.sociusfit.app.presentation.theme.spacing
import org.koin.androidx.compose.koinViewModel

/**
 * Schermata del Profilo Utente
 *
 * Features:
 * - Immagine profilo (placeholder)
 * - Nome e cognome utente
 * - Loading state
 *
 * @param navController Controller per la navigazione
 * @param viewModel ViewModel per gestire la logica
 */
@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        when {
            uiState.isLoading -> {
                SFLoadingIndicator()
            }

            uiState.error != null -> {
                ErrorContent(
                    error = uiState.error!!,
                    onRetry = viewModel::loadUserProfile
                )
            }

            uiState.user != null -> {
                ProfileContent(
                    firstName = uiState.user!!.firstName,
                    lastName = uiState.user!!.lastName,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

/**
 * Contenuto del profilo con foto e nome
 */
@Composable
private fun ProfileContent(
    firstName: String,
    lastName: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(MaterialTheme.spacing.large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Immagine profilo placeholder
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Immagine profilo",
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

        // Nome completo
        Text(
            text = "$firstName $lastName",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

        // Sottotitolo
        Text(
            text = "Benvenuto su SociusFit!",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Contenuto di errore
 */
@Composable
private fun ErrorContent(
    error: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(MaterialTheme.spacing.large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Errore",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

        Text(
            text = error,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

        Button(onClick = onRetry) {
            Text("Riprova")
        }
    }
}