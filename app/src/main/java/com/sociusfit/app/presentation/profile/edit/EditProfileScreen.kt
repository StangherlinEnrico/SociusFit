package com.sociusfit.app.presentation.profile.edit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sociusfit.app.presentation.components.*
import com.sociusfit.app.presentation.theme.spacing
import org.koin.androidx.compose.koinViewModel

/**
 * Schermata per modificare il profilo utente
 *
 * Features:
 * - Header con avatar placeholder
 * - Modifica nome e cognome
 * - Selezione località con autocomplete e chip separato
 * - Campo di ricerca sempre vuoto all'apertura
 * - Slider distanza massima con preview
 * - Design moderno e consistente con SociusFit
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
                selectedLocation = uiState.selectedMunicipalityName,  // 🔥 FIXED
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
    ) {
        // Header con avatar
        ProfileHeader(
            firstName = firstName,
            lastName = lastName
        )

        // Form content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.large)
        ) {
            // Sezione Dati Personali
            SectionTitle(
                text = "Dati Personali",
                icon = Icons.Default.Person
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

            // Divider
            HorizontalDivider()

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            // Sezione Località
            SectionTitle(
                text = "Località e Raggio",
                icon = Icons.Default.LocationOn
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            Text(
                text = "Scegli dove cercare partner sportivi",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            // 🔥 FIXED: Location section with separate chip and search field
            LocationSection(
                locationQuery = locationQuery,
                locationError = locationError,
                selectedLocation = selectedLocation,
                locationSuggestions = locationSuggestions,
                enabled = !isSaving,
                onLocationQueryChange = onLocationQueryChange,
                onLocationSelected = onLocationSelected
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            // Distance slider section
            DistanceSliderSection(
                maxDistance = maxDistance,
                enabled = !isSaving,
                onMaxDistanceChange = onMaxDistanceChange
            )

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
}

/**
 * Header con avatar e info base
 */
@Composable
private fun ProfileHeader(
    firstName: String,
    lastName: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(vertical = MaterialTheme.spacing.extraLarge),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar placeholder
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(56.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }

            if (firstName.isNotBlank() || lastName.isNotBlank()) {
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                Text(
                    text = "$firstName $lastName".trim(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

/**
 * Titolo sezione con icona
 */
@Composable
private fun SectionTitle(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

/**
 * 🔥 FIXED: Sezione località con chip separato e campo di ricerca vuoto
 *
 * Comportamento:
 * - All'apertura, il campo di ricerca è VUOTO
 * - Se c'è una località selezionata, viene mostrata in un chip sopra
 * - Il chip ha un pulsante X per rimuovere la località
 * - Il campo di ricerca è indipendente dalla località selezionata
 */
@Composable
private fun LocationSection(
    locationQuery: String,
    locationError: String?,
    selectedLocation: String?,
    locationSuggestions: List<String>,
    enabled: Boolean,
    onLocationQueryChange: (String) -> Unit,
    onLocationSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // 🔥 Selected location chip with remove button
        AnimatedVisibility(
            visible = selectedLocation != null,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            selectedLocation?.let {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = MaterialTheme.spacing.medium),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Chip con località selezionata
                    ElevatedCard(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(MaterialTheme.spacing.medium),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }

        // 🔥 Campo di ricerca (sempre vuoto all'apertura)
        SFTextField(
            value = locationQuery,
            onValueChange = onLocationQueryChange,
            label = if (selectedLocation != null) "Cambia località" else "Cerca comune",
            placeholder = "es. Milano, Roma, Torino...",
            leadingIcon = Icons.Default.Search,
            isError = locationError != null,
            errorMessage = locationError,
            keyboardType = androidx.compose.ui.text.input.KeyboardType.Text,
            imeAction = ImeAction.Done,
            enabled = enabled,
            modifier = Modifier.fillMaxWidth()
        )

        // Dropdown suggerimenti
        AnimatedVisibility(
            visible = locationSuggestions.isNotEmpty(),
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = MaterialTheme.spacing.small),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(vertical = MaterialTheme.spacing.small)
                ) {
                    locationSuggestions.take(5).forEach { suggestion ->
                        SuggestionItem(
                            text = suggestion,
                            onClick = { onLocationSelected(suggestion) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Item suggerimento località
 */
@Composable
private fun SuggestionItem(
    text: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = MaterialTheme.spacing.medium,
                    vertical = MaterialTheme.spacing.small
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

/**
 * Sezione slider distanza con preview
 */
@Composable
private fun DistanceSliderSection(
    maxDistance: Int,
    enabled: Boolean,
    onMaxDistanceChange: (Float) -> Unit
) {
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Distanza massima",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )

                // Distance badge
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.primary
                ) {
                    Text(
                        text = "$maxDistance km",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(
                            horizontal = MaterialTheme.spacing.medium,
                            vertical = MaterialTheme.spacing.small
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            Slider(
                value = maxDistance.toFloat(),
                onValueChange = onMaxDistanceChange,
                valueRange = 1f..100f,
                steps = 99,
                enabled = enabled,
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary,
                    inactiveTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "1 km",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                )
                Text(
                    text = "100 km",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            // Helper text
            Text(
                text = "Cerca partner entro $maxDistance km dalla tua posizione",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}