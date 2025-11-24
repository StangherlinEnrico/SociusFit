package com.sociusfit.app.utils

/**
 * Utility per formattare e mascherare informazioni sensibili
 */
object StringUtils {

    /**
     * Maschera l'email mostrando solo le prime 3 lettere della parte locale
     * Esempi:
     * - "mario.rossi@gmail.com" -> "mar***@gmail.com"
     * - "ab@test.it" -> "ab***@test.it"
     * - "a@domain.com" -> "a***@domain.com"
     *
     * @param email Email da mascherare
     * @return Email mascherata
     */
    fun maskEmail(email: String): String {
        if (!email.contains("@")) {
            return email // Return as is if not a valid email format
        }

        val parts = email.split("@")
        if (parts.size != 2) {
            return email
        }

        val localPart = parts[0]
        val domain = parts[1]

        val visibleChars = localPart.take(3)
        return "$visibleChars***@$domain"
    }

    /**
     * Capitalizza la prima lettera di ogni parola
     * Esempi:
     * - "mario rossi" -> "Mario Rossi"
     * - "MARIO ROSSI" -> "Mario Rossi"
     *
     * @param text Testo da capitalizzare
     * @return Testo con prima lettera maiuscola per ogni parola
     */
    fun capitalizeWords(text: String): String {
        return text.split(" ")
            .joinToString(" ") { word ->
                word.lowercase().replaceFirstChar { it.uppercase() }
            }
    }

    /**
     * Formatta distanza in km
     * Esempi:
     * - 1 -> "1 km"
     * - 25 -> "25 km"
     * - 100 -> "100 km"
     *
     * @param distanceKm Distanza in km
     * @return Distanza formattata
     */
    fun formatDistance(distanceKm: Int): String {
        return "$distanceKm km"
    }

    /**
     * Formatta il nome completo
     * Esempi:
     * - ("Mario", "Rossi") -> "Mario Rossi"
     * - ("LUIGI", "VERDI") -> "Luigi Verdi"
     *
     * @param firstName Nome
     * @param lastName Cognome
     * @return Nome completo capitalizzato
     */
    fun formatFullName(firstName: String, lastName: String): String {
        val capitalizedFirst = capitalizeWords(firstName.trim())
        val capitalizedLast = capitalizeWords(lastName.trim())
        return "$capitalizedFirst $capitalizedLast"
    }

    /**
     * Ottieni le iniziali dal nome completo
     * Esempi:
     * - "Mario Rossi" -> "MR"
     * - "Luigi Verdi" -> "LV"
     *
     * @param firstName Nome
     * @param lastName Cognome
     * @return Iniziali maiuscole
     */
    fun getInitials(firstName: String, lastName: String): String {
        val firstInitial = firstName.trim().firstOrNull()?.uppercaseChar() ?: ""
        val lastInitial = lastName.trim().firstOrNull()?.uppercaseChar() ?: ""
        return "$firstInitial$lastInitial"
    }

    /**
     * Tronca testo lungo con ellipsis
     * Esempi:
     * - ("Questo è un testo molto lungo", 15) -> "Questo è un..."
     * - ("Breve", 15) -> "Breve"
     *
     * @param text Testo da troncare
     * @param maxLength Lunghezza massima
     * @return Testo troncato con "..." se necessario
     */
    fun truncate(text: String, maxLength: Int): String {
        return if (text.length <= maxLength) {
            text
        } else {
            text.take(maxLength - 3) + "..."
        }
    }

    /**
     * Valida formato email base
     *
     * @param email Email da validare
     * @return true se il formato è valido
     */
    fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$".toRegex()
        return email.matches(emailRegex)
    }

    /**
     * Rimuovi spazi extra e normalizza
     * Esempi:
     * - "  Mario   Rossi  " -> "Mario Rossi"
     * - "Luigi    Verdi" -> "Luigi Verdi"
     *
     * @param text Testo da normalizzare
     * @return Testo normalizzato
     */
    fun normalizeSpaces(text: String): String {
        return text.trim().replace(Regex("\\s+"), " ")
    }

    /**
     * Formatta numero telefono italiano
     * Esempi:
     * - "3331234567" -> "+39 333 123 4567"
     * - "+393331234567" -> "+39 333 123 4567"
     *
     * @param phoneNumber Numero di telefono
     * @return Numero formattato
     */
    fun formatPhoneNumber(phoneNumber: String): String {
        val cleaned = phoneNumber.replace(Regex("[^0-9+]"), "")

        return when {
            cleaned.startsWith("+39") && cleaned.length == 13 -> {
                "+39 ${cleaned.substring(3, 6)} ${cleaned.substring(6, 9)} ${cleaned.substring(9)}"
            }
            cleaned.length == 10 -> {
                "+39 ${cleaned.substring(0, 3)} ${cleaned.substring(3, 6)} ${cleaned.substring(6)}"
            }
            else -> phoneNumber
        }
    }

    /**
     * Genera username suggerito da nome e cognome
     * Esempi:
     * - ("Mario", "Rossi") -> "mariorossi"
     * - ("Luigi", "Verdi") -> "luigiverdi"
     *
     * @param firstName Nome
     * @param lastName Cognome
     * @return Username suggerito
     */
    fun generateUsername(firstName: String, lastName: String): String {
        val cleanFirst = firstName.trim().lowercase().replace(Regex("[^a-z]"), "")
        val cleanLast = lastName.trim().lowercase().replace(Regex("[^a-z]"), "")
        return "$cleanFirst$cleanLast"
    }

    /**
     * Formatta data relativa (es. "2 giorni fa")
     *
     * @param minutesAgo Minuti trascorsi
     * @return Stringa formattata
     */
    fun formatRelativeTime(minutesAgo: Long): String {
        return when {
            minutesAgo < 1 -> "Adesso"
            minutesAgo < 60 -> "$minutesAgo minuti fa"
            minutesAgo < 1440 -> {
                val hours = minutesAgo / 60
                if (hours == 1L) "1 ora fa" else "$hours ore fa"
            }
            minutesAgo < 43200 -> {
                val days = minutesAgo / 1440
                if (days == 1L) "1 giorno fa" else "$days giorni fa"
            }
            else -> {
                val weeks = minutesAgo / 10080
                if (weeks == 1L) "1 settimana fa" else "$weeks settimane fa"
            }
        }
    }
}