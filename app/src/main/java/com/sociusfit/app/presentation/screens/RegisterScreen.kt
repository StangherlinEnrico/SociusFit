package com.sociusfit.app.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sociusfit.app.presentation.components.SFButton
import com.sociusfit.app.presentation.components.SFPasswordTextField
import com.sociusfit.app.presentation.components.SFTextButton
import com.sociusfit.app.presentation.components.SFTextField
import com.sociusfit.app.presentation.navigation.Routes
import com.sociusfit.feature.user.presentation.register.RegisterViewModel
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreen(
    navController: NavHostController,
    viewModel: RegisterViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        visible = true
    }

    LaunchedEffect(uiState.registerSuccess) {
        if (uiState.registerSuccess) {
            navController.navigate(Routes.PROFILE) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it, duration = SnackbarDuration.Short)
            viewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.05f),
                            MaterialTheme.colorScheme.surface
                        ),
                        startY = 0f,
                        endY = 800f
                    )
                )
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(48.dp))

                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(animationSpec = tween(600)) +
                            slideInVertically(
                                initialOffsetY = { -40 },
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessLow
                                )
                            )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.secondary,
                                            MaterialTheme.colorScheme.primary
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PersonAdd,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "Crea il tuo account",
                            style = MaterialTheme.typography.displayMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Inizia a trovare i tuoi compagni di allenamento",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(animationSpec = tween(800, delayMillis = 200)) +
                            slideInVertically(
                                initialOffsetY = { 40 },
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessLow
                                )
                            )
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            SFTextField(
                                value = uiState.firstName,
                                onValueChange = viewModel::onFirstNameChanged,
                                label = "Nome",
                                placeholder = "Mario",
                                leadingIcon = Icons.Default.Person,
                                isError = uiState.firstNameError != null,
                                errorMessage = uiState.firstNameError,
                                imeAction = ImeAction.Next,
                                enabled = !uiState.isLoading,
                                modifier = Modifier.weight(1f)
                            )

                            SFTextField(
                                value = uiState.lastName,
                                onValueChange = viewModel::onLastNameChanged,
                                label = "Cognome",
                                placeholder = "Rossi",
                                isError = uiState.lastNameError != null,
                                errorMessage = uiState.lastNameError,
                                imeAction = ImeAction.Next,
                                enabled = !uiState.isLoading,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        SFTextField(
                            value = uiState.email,
                            onValueChange = viewModel::onEmailChanged,
                            label = "Email",
                            placeholder = "mario.rossi@email.com",
                            leadingIcon = Icons.Default.Email,
                            isError = uiState.emailError != null,
                            errorMessage = uiState.emailError,
                            imeAction = ImeAction.Next,
                            enabled = !uiState.isLoading,
                            modifier = Modifier.fillMaxWidth()
                        )

                        SFPasswordTextField(
                            value = uiState.password,
                            onValueChange = viewModel::onPasswordChanged,
                            label = "Password",
                            isError = uiState.passwordError != null,
                            errorMessage = uiState.passwordError ?: "Minimo 8 caratteri",
                            imeAction = ImeAction.Next,
                            enabled = !uiState.isLoading,
                            modifier = Modifier.fillMaxWidth()
                        )

                        SFPasswordTextField(
                            value = uiState.confirmPassword,
                            onValueChange = viewModel::onConfirmPasswordChanged,
                            label = "Conferma Password",
                            isError = uiState.confirmPasswordError != null,
                            errorMessage = uiState.confirmPasswordError,
                            imeAction = ImeAction.Done,
                            enabled = !uiState.isLoading,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        SFButton(
                            text = if (uiState.isLoading) "Registrazione in corso..." else "Registrati",
                            onClick = viewModel::onRegisterClick,
                            enabled = !uiState.isLoading,
                            modifier = Modifier.fillMaxWidth()
                        )

                        SFTextButton(
                            text = "Hai già un account? Accedi",
                            onClick = { navController.popBackStack() },
                            enabled = !uiState.isLoading,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}