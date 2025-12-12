package com.sociusfit.app

/**
 * App Configuration
 *
 * Configurazione statica dell'applicazione.
 * Alternativa a BuildConfig quando buildConfig non è abilitato.
 *
 * Per release builds, cambia IS_DEBUG a false manualmente.
 */
object AppConfig {

    /**
     * Debug Mode
     *
     * true = Development environment
     * - Logging abilitato
     * - Dev API endpoint
     * - Http logging interceptor
     *
     * false = Production environment
     * - Logging disabilitato
     * - Prod API endpoint
     * - No http logging
     *
     * TODO: Cambia a false prima del release build
     */
    const val IS_DEBUG = true

    /**
     * App Version
     */
    const val VERSION_NAME = "1.0.0"
    const val VERSION_CODE = 1

    /**
     * API Configuration
     *
     * IMPORTANTE:
     * - Per emulatore Android: usa "http://10.0.2.2:3000/api/"
     * - Per dispositivo fisico: usa "http://TUO_IP_LOCALE:3000/api/" (es: "http://192.168.1.100:3000/api/")
     * - Per production: usa URL reale del backend
     */
    val BASE_URL: String
        get() = if (IS_DEBUG) {
            // DEVELOPMENT - Backend locale
            // "http://10.0.2.2:5000/api/"  // Emulatore Android → localhost:5000

            // Se usi dispositivo fisico, cambia con il tuo IP:
            "http://192.168.1.27:5000/api/"  // Sostituisci con il tuo IP locale
        } else {
            // PRODUCTION - Backend reale
            "https://api.sociusfit.com/api/"
        }
}