package com.sociusfit.app.domain.model

/**
 * Supported OAuth providers for authentication
 */
enum class OAuthProvider(val providerName: String) {
    GOOGLE("Google"),
    FACEBOOK("Facebook"),
    MICROSOFT("Microsoft"),
    APPLE("Apple");

    companion object {
        /**
         * Get provider from string name
         */
        fun fromString(value: String): OAuthProvider? {
            return entries.find {
                it.name.equals(value, ignoreCase = true) ||
                        it.providerName.equals(value, ignoreCase = true)
            }
        }
    }
}