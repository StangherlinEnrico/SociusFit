package com.sociusfit.app.navigation

/**
 * App Routes
 *
 * Definisce tutte le route principali dell'applicazione.
 * Le route dei singoli moduli sono definite nei rispettivi packages.
 */
object AppRoutes {

    // ========== MAIN GRAPHS ==========

    /**
     * Auth Graph
     * Contiene: Splash, Login, Register
     */
    const val AUTH_GRAPH = "auth_graph"

    /**
     * Home Graph (TODO Sprint 3)
     * Contiene: Bottom Navigation principale
     */
    const val HOME_GRAPH = "home_graph"

    // ========== AUTH ROUTES ==========

    /**
     * Splash Screen
     * Auto-login check e routing iniziale
     */
    const val SPLASH = "splash"

    /**
     * Login Screen
     */
    const val LOGIN = "login"

    /**
     * Register Screen
     */
    const val REGISTER = "register"

    // Note: Le route Profile, Discovery, Match, Chat
    // sono definite nei rispettivi moduli:
    // - ProfileRoutes (già implementato)
    // - DiscoveryRoutes (TODO Sprint 3)
    // - MatchRoutes (TODO Sprint 4)
    // - ChatRoutes (TODO Sprint 5)
}