package com.sociusfit.app.core.util

import android.content.Context
import android.widget.Toast
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

/**
 * Extension functions per String
 */
fun String.isValidEmail(): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
    return this.matches(emailRegex)
}

fun String.isValidPassword(): Boolean {
    // Minimo 8 caratteri, almeno una lettera e un numero
    return this.length >= 8 &&
            this.any { it.isLetter() } &&
            this.any { it.isDigit() }
}

/**
 * Extension functions per Context
 */
fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

/**
 * Extension functions per DayOfWeek
 */
fun DayOfWeek.toItalianString(): String {
    return this.getDisplayName(TextStyle.FULL, Locale.ITALIAN)
}

fun DayOfWeek.toShortItalianString(): String {
    return this.getDisplayName(TextStyle.SHORT, Locale.ITALIAN)
}

/**
 * Extension functions per Float
 */
fun Float.toPercentage(): Int {
    return (this * 100).toInt()
}

/**
 * Extension functions per Double
 */
fun Double.formatDistance(): String {
    return if (this < 1.0) {
        "${(this * 1000).toInt()} m"
    } else {
        "%.1f km".format(this)
    }
}

/**
 * Extension functions per Collection
 */
fun <T> Collection<T>.isNotNullOrEmpty(): Boolean {
    return this.isNotEmpty()
}