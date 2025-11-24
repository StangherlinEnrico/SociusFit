package com.sociusfit.app.presentation.profile.edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sociusfit.app.presentation.components.*
import com.sociusfit.app.presentation.theme.spacing
import org.koin.androidx.compose.koinViewModel

/**
 * Schermata per modificare il profilo utente
 *
 * Features:
 * - Modifica nome e cognome
 * - Selezione località (autocomplete)
 * - Slider distanza massima (1-100 km)
 * - Salvataggio modifiche
 *
 * @param navController Controller per la navigazione
 * @param viewModel ViewModel per gestire la logica
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavHostController,
    viewModel: EditProfileViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Gestione successo salvataggio
    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            navController.popBackStack()
        }
    }

    // Gestione errori
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
            TopAppBar(
                title = { Text("Modifica Profilo") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Indietro"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        if (uiState.isLoading) {
            SFLoadingIndicator()
        } else {
            EditProfileContent(
                firstName = uiState.firstName,
                lastName = uiState.lastName,
                firstNameError = uiState.firstNameError,
                lastNameError = uiState.lastNameError,
                locationQuery = uiState.locationQuery,
                locationError = uiState.locationError,
                selectedLocation = uiState.selectedLocation,
                locationSuggestions = uiState.locationSuggestions,
                maxDistance = uiState.maxDistance,
                isSaving = uiState.isSaving,
                onFirstNameChange = viewModel::onFirstNameChanged,
                onLastNameChange = viewModel::onLastNameChanged,
                onLocationQueryChange = viewModel::onLocationQueryChanged,
                onLocationSelected = viewModel::onLocationSelected,
                onMaxDistanceChange = viewModel::onMaxDistanceChanged,
                onSaveClick = viewModel::onSaveClick,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

/**
 * Contenuto della schermata di modifica profilo
 */
@Composable
private fun EditProfileContent(
    firstName: String,
    lastName: String,
    firstNameError: String?,
    lastNameError: String?,
    locationQuery: String,
    locationError: String?,
    selectedLocation: String?,
    locationSuggestions: List<String>,
    maxDistance: Int,
    isSaving: Boolean,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onLocationQueryChange: (String) -> Unit,
    onLocationSelected: (String) -> Unit,
    onMaxDistanceChange: (Float) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(MaterialTheme.spacing.large)
    ) {
        // Sezione Informazioni Personali
        Text(
            text = "Informazioni Personali",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

        // Nome
        SFTextField(
            value = firstName,
            onValueChange = onFirstNameChange,
            label = "Nome",
            placeholder = "Mario",
            leadingIcon = Icons.Default.Person,
            isError = firstNameError != null,
            errorMessage = firstNameError,
            keyboardType = androidx.compose.ui.text.input.KeyboardType.Text,
            imeAction = ImeAction.Next,
            enabled = !isSaving,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

        // Cognome
        SFTextField(
            value = lastName,
            onValueChange = onLastNameChange,
            label = "Cognome",
            placeholder = "Rossi",
            leadingIcon = Icons.Default.Person,
            isError = lastNameError != null,
            errorMessage = lastNameError,
            keyboardType = androidx.compose.ui.text.input.KeyboardType.Text,
            imeAction = ImeAction.Done,
            enabled = !isSaving,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

        HorizontalDivider()

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

        // Sezione Località
        Text(
            text = "Località e Distanza",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

        // Location search
        SFTextField(
            value = locationQuery,
            onValueChange = onLocationQueryChange,
            label = "Cerca comune",
            placeholder = "Milano, Roma, Torino...",
            leadingIcon = Icons.Default.Search,
            isError = locationError != null,
            errorMessage = locationError,
            keyboardType = androidx.compose.ui.text.input.KeyboardType.Text,
            imeAction = ImeAction.Done,
            enabled = !isSaving,
            modifier = Modifier.fillMaxWidth()
        )

        // Location suggestions
        if (locationSuggestions.isNotEmpty()) {
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(MaterialTheme.spacing.small)
                ) {
                    locationSuggestions.take(5).forEach { suggestion ->
                        TextButton(
                            onClick = { onLocationSelected(suggestion) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = suggestion,
                                modifier = Modifier.fillMaxWidth(),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }

        // Selected location chip
        if (selectedLocation != null) {
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            AssistChip(
                onClick = { /* Already selected */ },
                label = { Text("✓ $selectedLocation") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    labelColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

        // Distanza massima slider
        Text(
            text = "Distanza massima: $maxDistance km",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

        Slider(
            value = maxDistance.toFloat(),
            onValueChange = onMaxDistanceChange,
            valueRange = 1f..100f,
            steps = 99,
            enabled = !isSaving,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "1 km",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "100 km",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))

        // Pulsante Salva
        SFButton(
            text = if (isSaving) "Salvataggio..." else "Salva Modifiche",
            onClick = onSaveClick,
            enabled = !isSaving,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
    }
}