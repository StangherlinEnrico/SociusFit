package com.sociusfit.app.ui.profile.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sociusfit.core.ui.components.CityAutocompleteTextField
import com.sociusfit.feature.profile.presentation.onboarding.bio.OnboardingBioViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * Step 1/3 Onboarding: Età, Città, Bio, Distanza Massima
 *
 * AGGIORNATO: Salva i dati in OnboardingRepository prima di continuare
 */
@Composable
fun OnboardingBioScreen(
    onContinue: () -> Unit,
    viewModel: OnboardingBioViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    OnboardingBioContent(
        uiState = uiState,
        onAgeChanged = viewModel::onAgeChanged,
        onCityChanged = viewModel::onCityChanged,
        onCitySelected = viewModel::onCitySelected,
        onBioChanged = viewModel::onBioChanged,
        onMaxDistanceChanged = viewModel::onMaxDistanceChanged,
        onContinue = {
            if (viewModel.saveAndContinue()) {
                onContinue()
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OnboardingBioContent(
    uiState: com.sociusfit.feature.profile.presentation.onboarding.bio.OnboardingBioUiState,
    onAgeChanged: (String) -> Unit,
    onCityChanged: (String) -> Unit,
    onCitySelected: (com.sociusfit.core.domain.model.Municipality) -> Unit,
    onBioChanged: (String) -> Unit,
    onMaxDistanceChanged: (Int) -> Unit,
    onContinue: () -> Unit
) {
    // FocusRequesters per gestire il focus tra i campi
    val cityFocusRequester = remember { FocusRequester() }
    val bioFocusRequester = remember { FocusRequester() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crea il tuo profilo") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Step Indicator
            LinearProgressIndicator(
                progress = { 1f / 3f },
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Step 1 di 3",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Età - Passa a Città quando premi "Fatto"
            OutlinedTextField(
                value = uiState.age,
                onValueChange = onAgeChanged,
                label = { Text("Età") },
                placeholder = { Text("Es. 25") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { cityFocusRequester.requestFocus() }
                ),
                isError = !uiState.isAgeValid && uiState.age.isNotBlank(),
                supportingText = {
                    if (!uiState.isAgeValid && uiState.age.isNotBlank()) {
                        Text("L'età deve essere tra 18 e 100 anni")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Città con Autocomplete - Passa a Bio quando premi "Fatto"
            CityAutocompleteTextField(
                value = uiState.city,
                onValueChange = onCityChanged,
                onCitySelected = { municipality ->
                    onCitySelected(municipality)
                    bioFocusRequester.requestFocus()
                },
                municipalities = uiState.municipalities,
                label = "Città",
                placeholder = "Es. Milano",
                isError = !uiState.isCityValid && uiState.city.isNotBlank(),
                errorMessage = if (!uiState.isCityValid && uiState.city.isNotBlank()) {
                    "La città è obbligatoria"
                } else null,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(cityFocusRequester),
                onNext = { bioFocusRequester.requestFocus() }
            )

            // Bio - Multilinea, nessun "Fatto" sulla tastiera
            OutlinedTextField(
                value = uiState.bio,
                onValueChange = onBioChanged,
                label = { Text("Bio (opzionale)") },
                placeholder = { Text("Parlaci di te...") },
                supportingText = {
                    Text("${uiState.bio.length}/500")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .focusRequester(bioFocusRequester),
                maxLines = 6,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Default
                )
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