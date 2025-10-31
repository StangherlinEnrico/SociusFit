package com.sociusfit.app.core.util

import android.content.Context
import android.widget.Toast
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

fun String.isValidEmail(): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
    return this.matches(emailRegex)
}

fun String.isValidPassword(): Boolean {
    return this.length >= 8 &&
            this.any { it.isLetter() } &&
            this.any { it.isDigit() }
}

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun DayOfWeek.toItalianString(): String {
    return this.getDisplayName(TextStyle.FULL, Locale.ITALIAN)
}

fun DayOfWeek.toShortItalianString(): String {
    return this.getDisplayName(TextStyle.SHORT, Locale.ITALIAN)
}

fun Float.toPercentage(): Int {
    return (this * 100).toInt()
}

fun Double.formatDistance(): String {
    return if (this < 1.0) {
        "${(this * 1000).toInt()} m"
    } else {
        "%.1f km".format(this)
    }
}

fun <T> Collection<T>.isNotNullOrEmpty(): Boolean {
    return this.isNotEmpty()
}