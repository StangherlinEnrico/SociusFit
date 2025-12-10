package com.sociusfit.app.ui.profile.onboarding

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.sociusfit.feature.profile.presentation.onboarding.photo.OnboardingPhotoViewModel
import com.sociusfit.feature.profile.presentation.onboarding.photo.ProfileData
import org.koin.androidx.compose.koinViewModel
import java.io.File

/**
 * Step 3/3 Onboarding: Upload Foto Profilo (opzionale)
 */
@Composable
fun OnboardingPhotoScreen(
    profileData: ProfileData,
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
        onSkip = { viewModel.onSkip(profileData) },
        onComplete = { viewModel.onComplete(profileData, photoFile) },
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
                            imageVector = Icons.Default.Person, // TODO: Use ArrowBack
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Step Indicator
            LinearProgressIndicator(
                progress = { 3f / 3f },
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Step 3 di 3",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Photo Preview
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (photoUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(photoUri),
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
                                .padding(48.dp),
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Aggiungi una foto profilo",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "La foto aiuta gli altri a riconoscerti",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 24.dp)
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

            Spacer(modifier = Modifier.weight(1f))

            // Action Buttons
            if (photoUri == null) {
                Button(
                    onClick = onSelectFromGallery,
                    enabled = !uiState.isUploading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AddAPhoto,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("Scegli dalla galleria")
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = onTakePhoto,
                    enabled = !uiState.isUploading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AddAPhoto,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("Scatta una foto")
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = onSkip,
                    enabled = !uiState.isUploading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Salta per ora")
                }
            } else {
                Button(
                    onClick = onComplete,
                    enabled = !uiState.isUploading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
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

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = onSelectFromGallery,
                    enabled = !uiState.isUploading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cambia foto")
                }
            }
        }
    }
}