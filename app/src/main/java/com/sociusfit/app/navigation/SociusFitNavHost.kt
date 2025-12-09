package com.sociusfit.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

/**
 * SociusFit Navigation Host
 *
 * Gestisce la navigazione principale dell'app.
 * Punto di ingresso della navigazione con Auth graph.
 */
@Composable
fun SociusFitNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "auth"
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Auth Navigation Graph
        authNavGraph(
            navController = navController,
            onNavigateToHome = {
                navController.navigate("home") {
                    // Rimuovi auth graph dallo stack quando vai a home
                    popUpTo("auth") { inclusive = true }
                }
            },
            onNavigateToOnboarding = {
                navController.navigate("onboarding") {
                    // Rimuovi auth graph dallo stack quando vai a onboarding
                    popUpTo("auth") { inclusive = true }
                }
            }
        )

        // TODO: Home Navigation Graph (Sprint 3)
        composable("home") {
            // Placeholder per home
            // HomeScreen()
        }

        // TODO: Onboarding Navigation Graph (Sprint 2)
        composable("onboarding") {
            // Placeholder per onboarding
            // OnboardingNavGraph()
        }
    }
}