package com.sociusfit.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.sociusfit.app.navigation.SociusFitNavHost
import com.sociusfit.core.ui.theme.SFTheme
import timber.log.Timber

/**
 * MainActivity
 *
 * Unica Activity dell'app (Single Activity Architecture).
 * Responsabilità:
 * - Setup Compose UI
 * - Installazione SplashScreen nativa
 * - Hosting Navigation
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Install native splash screen PRIMA di super.onCreate()
        installSplashScreen()

        super.onCreate(savedInstanceState)

        Timber.d("MainActivity created")

        // Enable edge-to-edge display
        enableEdgeToEdge()

        setContent {
            SFTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    SociusFitNavHost(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Timber.d("MainActivity started")
    }

    override fun onStop() {
        super.onStop()
        Timber.d("MainActivity stopped")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("MainActivity destroyed")
    }
}