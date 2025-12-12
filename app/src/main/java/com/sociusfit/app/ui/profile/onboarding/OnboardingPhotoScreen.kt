package com.sociusfit.app.ui.profile.onboarding

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sociusfit.feature.profile.presentation.onboarding.photo.OnboardingPhotoViewModel
import org.koin.androidx.compose.koinViewModel
import java.io.File

/**
 * Step 3/3 Onboarding: Upload Foto Profilo (opzionale)
 *
 * Features:
 * - Verifica successo foto prima di aggiornare UI
 * - Anteprima foto con Coil
 * - Icona X per rimuovere foto
 * - "Completa profilo" se foto presente
 * - "Salta per ora" se foto assente
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
    var showPermissionDialog by remember { mutableStateOf(false) }

    // Temporary URI per la fotocamera (non mostriamo finché non confermato)
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }
    var tempCameraFile by remember { mutableStateOf<File?>(null) }

    // Gallery launcher (no permissions needed on Android 10+)
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            photoUri = it
            viewModel.onPhotoSelected(it)

            // Copy file to cache for upload
            val cacheFile = File(context.cacheDir, "profile_photo_${System.currentTimeMillis()}.jpg")
            try {
                context.contentResolver.openInputStream(uri)?.use { input ->
                    cacheFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                photoFile = cacheFile
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Camera launcher - IMPORTANTE: aggiorna UI solo se success = true
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempCameraUri != null) {
            // Utente ha confermato la foto
            photoUri = tempCameraUri
            photoFile = tempCameraFile
            viewModel.onPhotoSelected(tempCameraUri!!)
        }
        // Se success = false, l'utente ha annullato → non facciamo nulla
        // Reset temp vars
        tempCameraUri = null
        tempCameraFile = null
    }

    // Permission launcher for camera
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permission granted, proceed with camera
            val file = File(context.cacheDir, "profile_photo_${System.currentTimeMillis()}.jpg")
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            )
            // Salviamo in temp, aggiorneremo photoUri solo se confermato
            tempCameraUri = uri
            tempCameraFile = file
            cameraLauncher.launch(uri)
        } else {
            // Permission denied
            showPermissionDialog = true
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
            // Request camera permission first
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        },
        onRemovePhoto = {
            photoUri = null
            photoFile = null
        },
        onSkip = { viewModel.onSkip() },
        onComplete = { viewModel.onComplete(photoFile) },
        onBack = onBack
    )

    // Permission denied dialog
    if (showPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDialog = false },
            title = { Text("Permesso fotocamera necessario") },
            text = { Text("Per scattare una foto, è necessario concedere il permesso di accesso alla fotocamera. Puoi abilitarlo nelle impostazioni dell'app.") },
            confirmButton = {
                TextButton(onClick = { showPermissionDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OnboardingPhotoContent(
    uiState: com.sociusfit.feature.profile.presentation.onboarding.photo.OnboardingPhotoUiState,
    photoUri: Uri?,
    onSelectFromGallery: () -> Unit,
    onTakePhoto: () -> Unit,
    onRemovePhoto: () -> Unit,
    onSkip: () -> Unit,
    onComplete: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Foto profilo") },
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

            // Photo Preview or Placeholder
            Box(
                modifier = Modifier.size(200.dp),
                contentAlignment = Alignment.Center
            ) {
                if (photoUri != null) {
                    // Anteprima foto circolare con Coil
                    Card(
                        modifier = Modifier.fillMaxSize(),
                        shape = CircleShape
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(photoUri)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Foto profilo",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }

                    // X button in alto a destra per rimuovere
                    IconButton(
                        onClick = onRemovePhoto,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(36.dp),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Rimuovi foto",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                } else {
                    // Placeholder quando nessuna foto
                    Card(
                        modifier = Modifier.fillMaxSize(),
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Nessuna foto",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Text(
                text = if (photoUri != null) "Foto selezionata" else "Aggiungi una foto profilo",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.weight(1f))

            // Buttons - sempre visibili
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

            Spacer(modifier = Modifier.height(8.dp))

            // Action button - "Completa profilo" se foto presente, "Salta per ora" altrimenti
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
            } else {
                TextButton(
                    onClick = onSkip,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isUploading
                ) {
                    Text("Salta per ora")
                }
            }

            // Error message
            if (uiState.error != null) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = uiState.error!!,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }
    }
}