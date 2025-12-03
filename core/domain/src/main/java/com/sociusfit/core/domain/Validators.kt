package com.sociusfit.core.domain

object Validators {
    private val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()

    fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && EMAIL_REGEX.matches(email)
    }

    fun isValidPassword(password: String): Boolean {
        return password.length in 8..100 && password.any { it.isDigit() }
    }

    fun isValidAge(age: Int): Boolean {
        return age in 18..100
    }

    fun isValidName(name: String): Boolean {
        return name.isNotBlank() && name.length <= 50
    }

    fun isValidBio(bio: String): Boolean {
        return bio.length <= 500
    }

    fun isValidMessageContent(content: String): Boolean {
        return content.isNotBlank() && content.length <= 2000
    }
}