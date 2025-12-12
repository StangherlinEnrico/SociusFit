package com.sociusfit.app.ui.profile.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sociusfit.feature.profile.presentation.onboarding.sports.OnboardingSportsViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.ui.unit.dp

/**
 * Step 2/3 Onboarding: Selezione Sport
 *
 * AGGIORNATO: Salva i dati in OnboardingRepository prima di continuare
 */
@Composable
fun OnboardingSportsScreen(
    onContinue: () -> Unit,
    onBack: () -> Unit,
    viewModel: OnboardingSportsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    OnboardingSportsContent(
        uiState = uiState,
        onSportSelected = viewModel::onSportSelected,
        onSportDeselected = viewModel::onSportDeselected,
        onContinue = {
            if (viewModel.saveAndContinue()) {
                onContinue()
            }
        },
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OnboardingSportsContent(
    uiState: com.sociusfit.feature.profile.presentation.onboarding.sports.OnboardingSportsUiState,
    onSportSelected: (String, com.sociusfit.feature.profile.domain.model.SportLevel) -> Unit,
    onSportDeselected: (String) -> Unit,
    onContinue: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Seleziona i tuoi sport") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Indietro"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Step Indicator
            LinearProgressIndicator(
                progress = { 2f / 3f },
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Step 2 di 3",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Seleziona da 1 a 5 sport",
                style = MaterialTheme.typography.bodyLarge
            )

            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else if (uiState.error != null) {
                Text(
                    text = uiState.error!!,
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                // TODO: Implementa la grid di sport
                // Per ora placeholder
                Text("Lista sport qui")
            }

            Spacer(modifier = Modifier.weight(1f))

            // Continue Button
            Button(
                onClick = onContinue,
                enabled = uiState.canContinue,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Continua")
            }
        }
    }
}