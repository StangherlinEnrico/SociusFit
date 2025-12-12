package com.sociusfit.app.ui.profile.onboarding

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sociusfit.feature.profile.presentation.onboarding.photo.OnboardingPhotoViewModel
import org.koin.androidx.compose.koinViewModel
import java.io.File

/**
 * Step 3/3 Onboarding: Upload Foto Profilo (opzionale)
 *
 * AGGIORNATO: Legge i dati da OnboardingRepository
 */
@Composable
fun OnboardingPhotoScreen(
    onComplete: () -> Unit,
    onBack: () -> Unit,
    viewModel: OnboardingPhotoViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var photoFile by remember { mutableStateOf<File?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            photoUri = it
            viewModel.onPhotoSelected(it)

            // Copy file to cache for upload
            val cacheFile = File(context.cacheDir, "profile_photo_${System.currentTimeMillis()}.jpg")
            context.contentResolver.openInputStream(uri)?.use { input ->
                cacheFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            photoFile = cacheFile
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && photoUri != null) {
            viewModel.onPhotoSelected(photoUri!!)
            photoFile = File(photoUri!!.path!!)
        }
    }

    LaunchedEffect(uiState.isComplete) {
        if (uiState.isComplete) {
            onComplete()
        }
    }

    OnboardingPhotoContent(
        uiState = uiState,
        photoUri = photoUri,
        onSelectFromGallery = { galleryLauncher.launch("image/*") },
        onTakePhoto = {
            val file = File(context.cacheDir, "profile_photo_${System.currentTimeMillis()}.jpg")
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            )
            photoUri = uri
            cameraLauncher.launch(uri)
        },
        onSkip = { viewModel.onSkip() },
        onComplete = { viewModel.onComplete(photoFile) },
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OnboardingPhotoContent(
    uiState: com.sociusfit.feature.profile.presentation.onboarding.photo.OnboardingPhotoUiState,
    photoUri: Uri?,
    onSelectFromGallery: () -> Unit,
    onTakePhoto: () -> Unit,
    onSkip: () -> Unit,
    onComplete: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Foto profilo") },
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Step Indicator
            LinearProgressIndicator(
                progress = { 3f / 3f },
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Step 3 di 3",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (photoUri != null) {
                // TODO: Mostra preview foto
                Text("Foto selezionata: $photoUri")
            } else {
                Text(
                    text = "Aggiungi una foto profilo",
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Buttons
            Button(
                onClick = onSelectFromGallery,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isUploading
            ) {
                Text("Scegli dalla galleria")
            }

            Button(
                onClick = onTakePhoto,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isUploading
            ) {
                Text("Scatta foto")
            }

            TextButton(
                onClick = onSkip,
                enabled = !uiState.isUploading
            ) {
                Text("Salta per ora")
            }

            if (photoUri != null) {
                Button(
                    onClick = onComplete,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isUploading
                ) {
                    if (uiState.isUploading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Completa profilo")
                    }
                }
            }

            if (uiState.error != null) {
                Text(
                    text = uiState.error!!,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}