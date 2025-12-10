package com.sociusfit.app.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sociusfit.feature.profile.domain.model.SportLevel
import com.sociusfit.feature.profile.presentation.edit.EditProfileViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * Screen per modificare il profilo
 */
@Composable
fun EditProfileScreen(
    onNavigateBack: () -> Unit,
    viewModel: EditProfileViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            onNavigateBack()
        }
    }

    EditProfileContent(
        uiState = uiState,
        onAgeChanged = viewModel::onAgeChanged,
        onCityChanged = viewModel::onCityChanged,
        onBioChanged = viewModel::onBioChanged,
        onMaxDistanceChanged = viewModel::onMaxDistanceChanged,
        onSportSelected = viewModel::onSportSelected,
        onSportDeselected = viewModel::onSportDeselected,
        onSave = viewModel::onSave,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditProfileContent(
    uiState: com.sociusfit.feature.profile.presentation.edit.EditProfileUiState,
    onAgeChanged: (String) -> Unit,
    onCityChanged: (String) -> Unit,
    onBioChanged: (String) -> Unit,
    onMaxDistanceChanged: (Int) -> Unit,
    onSportSelected: (String, SportLevel) -> Unit,
    onSportDeselected: (String) -> Unit,
    onSave: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Modifica profilo") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Indietro"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = onSave,
                        enabled = uiState.canSave
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Salva"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(androidx.compose.ui.Alignment.Center)
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Error message
                uiState.error?.let { error ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }

                // Età
                OutlinedTextField(
                    value = uiState.age,
                    onValueChange = onAgeChanged,
                    label = { Text("Età") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = !uiState.isAgeValid && uiState.age.isNotBlank(),
                    supportingText = {
                        if (!uiState.isAgeValid && uiState.age.isNotBlank()) {
                            Text("L'età deve essere tra 18 e 100 anni")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Città
                OutlinedTextField(
                    value = uiState.city,
                    onValueChange = onCityChanged,
                    label = { Text("Città") },
                    isError = !uiState.isCityValid && uiState.city.isNotBlank(),
                    supportingText = {
                        if (!uiState.isCityValid && uiState.city.isNotBlank()) {
                            Text("La città è obbligatoria")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Bio
                OutlinedTextField(
                    value = uiState.bio,
                    onValueChange = onBioChanged,
                    label = { Text("Bio") },
                    supportingText = {
                        Text("${uiState.bio.length}/500")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    maxLines = 6
                )

                // Distanza Massima
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Distanza massima: ${uiState.maxDistance} km",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Slider(
                        value = uiState.maxDistance.toFloat(),
                        onValueChange = { onMaxDistanceChanged(it.toInt()) },
                        valueRange = 5f..100f,
                        steps = 18,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("5 km", style = MaterialTheme.typography.labelSmall)
                        Text("100 km", style = MaterialTheme.typography.labelSmall)
                    }
                }

                // Sports Section (Simplified)
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Sport selezionati: ${uiState.selectedSports.size}/5",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Text(
                            text = "Per modificare gli sport, utilizza la schermata di onboarding",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Save Button
                Button(
                    onClick = onSave,
                    enabled = uiState.canSave,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    if (uiState.isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Salva modifiche")
                    }
                }
            }
        }
    }
}