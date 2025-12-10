package com.sociusfit.app.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.sociusfit.feature.profile.domain.model.Profile
import com.sociusfit.feature.profile.presentation.other.OtherUserProfileViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * Screen per visualizzare il profilo di un altro utente
 */
@Composable
fun OtherUserProfileScreen(
    userId: String,
    onNavigateBack: () -> Unit,
    viewModel: OtherUserProfileViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(userId) {
        viewModel.loadProfile(userId)
    }

    OtherUserProfileContent(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onRetry = { viewModel.loadProfile(userId) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OtherUserProfileContent(
    uiState: com.sociusfit.feature.profile.presentation.other.OtherUserProfileUiState,
    onNavigateBack: () -> Unit,
    onRetry: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profilo") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Indietro"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                uiState.error != null -> {
                    val errorMessage = uiState.error!!
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = errorMessage,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = onRetry) {
                            Text("Riprova")
                        }
                    }
                }

                uiState.profile != null -> {
                    val profile = uiState.profile!!
                    OtherUserProfileDetails(profile = profile)
                }
            }
        }
    }
}

@Composable
private fun OtherUserProfileDetails(profile: Profile) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Photo
        Box(
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (profile.photoUrl != null) {
                Image(
                    painter = rememberAsyncImagePainter(profile.photoUrl),
                    contentDescription = "Foto profilo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Name
        Text(
            text = profile.fullName,
            style = MaterialTheme.typography.headlineMedium
        )

        // City & Age
        Text(
            text = "${profile.city}, ${profile.age} anni",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Sports Section
        if (profile.sports.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Sport",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    profile.sports.forEach { sport ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = sport.sportName,
                                style = MaterialTheme.typography.bodyLarge
                            )

                            SuggestionChip(
                                onClick = {},
                                label = { Text(sport.level.toDisplayString()) },
                                enabled = false
                            )
                        }

                        if (sport != profile.sports.last()) {
                            HorizontalDivider()
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bio Section
        if (profile.bio.isNotBlank()) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Bio",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = profile.bio,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}