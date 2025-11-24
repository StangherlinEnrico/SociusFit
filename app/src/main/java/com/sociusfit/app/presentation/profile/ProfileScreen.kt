package com.sociusfit.app.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
 * - Immagine profilo con gradiente
 * - Informazioni utente in card
 * - Email e verifica
 * - Design moderno Material 3
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
                    onRetry = viewModel::loadUserProfile,
                    modifier = Modifier.padding(paddingValues)
                )
            }

            uiState.user != null -> {
                ProfileContent(
                    firstName = uiState.user!!.firstName,
                    lastName = uiState.user!!.lastName,
                    email = uiState.user!!.email,
                    isEmailVerified = uiState.user!!.isEmailVerified,
                    provider = uiState.user!!.provider,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

/**
 * Contenuto del profilo con design moderno
 */
@Composable
private fun ProfileContent(
    firstName: String,
    lastName: String,
    email: String,
    isEmailVerified: Boolean,
    provider: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Header con gradiente e foto profilo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Foto profilo circolare con bordo
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
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
                }

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                // Nome completo
                Text(
                    text = "$firstName $lastName",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

                // Provider badge se presente
                if (provider != null) {
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                    ) {
                        Text(
                            text = "Accesso con $provider",
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(
                                horizontal = MaterialTheme.spacing.small,
                                vertical = 4.dp
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        // Cards con informazioni
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.large)
        ) {
            // Card Email
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.elevatedCardElevation(
                    defaultElevation = 2.dp
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.spacing.medium),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Icon container
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

                    // Email text
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Email",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = email,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    // Verified badge
                    if (isEmailVerified) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Email verificata",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            // Card Benvenuto
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.spacing.medium)
                ) {
                    Text(
                        text = "🎉 Benvenuto su SociusFit!",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                    Text(
                        text = "Inizia a trovare il tuo partner sportivo ideale e organizza sessioni di allenamento nella tua zona.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            // Statistiche placeholder
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
            ) {
                StatCard(
                    title = "Sessioni",
                    value = "0",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Partner",
                    value = "0",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Sport",
                    value = "0",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

/**
 * Card per statistiche
 */
@Composable
private fun StatCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier,
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.medium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Contenuto di errore
 */
@Composable
private fun ErrorContent(
    error: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(MaterialTheme.spacing.large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

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