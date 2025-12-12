package com.sociusfit.app.ui.profile.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sociusfit.feature.profile.domain.model.Sport
import com.sociusfit.feature.profile.domain.model.SportLevel
import com.sociusfit.feature.profile.presentation.onboarding.sports.OnboardingSportsViewModel
import org.koin.androidx.compose.koinViewModel

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
    onSportSelected: (String, SportLevel) -> Unit,
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
                            imageVector = Icons.Default.ArrowBack,
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
                .padding(16.dp)
        ) {
            // Step Indicator
            LinearProgressIndicator(
                progress = { 2f / 3f },
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Step 2 di 3",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(
                text = "Seleziona da 1 a 5 sport",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "${uiState.selectedSports.size}/5 selezionati",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Error message
            uiState.error?.let { error ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            // Sports Grid
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(uiState.availableSports) { sport ->
                        SportCard(
                            sport = sport,
                            isSelected = sport.id in uiState.selectedSports,
                            currentLevel = uiState.selectedSports[sport.id],
                            onSelect = { level -> onSportSelected(sport.id, level) },
                            onDeselect = { onSportDeselected(sport.id) }
                        )
                    }
                }
            }

            // Continue Button
            Button(
                onClick = onContinue,
                enabled = uiState.canContinue && !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Continua")
            }
        }
    }
}

@Composable
private fun SportCard(
    sport: Sport,
    isSelected: Boolean,
    currentLevel: SportLevel?,
    onSelect: (SportLevel) -> Unit,
    onDeselect: () -> Unit
) {
    var showLevelDialog by remember { mutableStateOf(false) }

    Card(
        onClick = {
            if (isSelected) {
                onDeselect()
            } else {
                showLevelDialog = true
            }
        },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        border = if (isSelected) {
            BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        } else {
            BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = sport.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )

                if (isSelected && currentLevel != null) {
                    Text(
                        text = currentLevel.toDisplayString(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selezionato",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(20.dp)
                )
            }
        }
    }

    // Level Selection Dialog
    if (showLevelDialog) {
        AlertDialog(
            onDismissRequest = { showLevelDialog = false },
            title = { Text("Seleziona il tuo livello") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SportLevel.entries.forEach { level ->
                        FilledTonalButton(
                            onClick = {
                                onSelect(level)
                                showLevelDialog = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(level.toDisplayString())
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showLevelDialog = false }) {
                    Text("Annulla")
                }
            }
        )
    }
}