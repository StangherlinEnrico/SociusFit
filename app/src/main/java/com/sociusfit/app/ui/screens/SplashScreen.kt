package com.sociusfit.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sociusfit.app.R
import com.sociusfit.core.storage.PreferencesManager
import kotlinx.coroutines.delay
import org.koin.compose.koinInject

@Composable
fun SplashScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit,
    preferencesManager: PreferencesManager = koinInject() // Inietta con Koin
) {
    LaunchedEffect(Unit) {
        delay(2000) // Splash minimo 2 secondi

        // Verifica se esiste un token salvato
        val token = preferencesManager.getToken()
        val isLoggedIn = !token.isNullOrEmpty()

        if (isLoggedIn) {
            onNavigateToHome()
        } else {
            onNavigateToLogin()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.mipmap.ic_launcher),
            contentDescription = "SociusFit Logo",
            modifier = Modifier.size(120.dp)
        )
    }
}