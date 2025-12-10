package com.sociusfit.app.navigation

import com.sociusfit.core.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Auth State Manager
 *
 * Gestisce lo stato di autenticazione globale dell'app.
 * Mantiene l'utente corrente e il suo stato di completamento profilo.
 *
 * Usato da:
 * - SociusFitNavHost per routing intelligente
 * - ProfileNavigation per getUserId/getUserName
 * - LoginScreen/RegisterScreen per aggiornare stato post-login
 */
object AuthStateManager {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    /**
     * Aggiorna l'utente corrente dopo login/register
     */
    fun setUser(user: User) {
        _currentUser.value = user
    }

    /**
     * Pulisce l'utente corrente dopo logout
     */
    fun clearUser() {
        _currentUser.value = null
    }

    /**
     * Controlla se l'utente ha completato il profilo
     */
    fun isProfileComplete(): Boolean {
        return _currentUser.value?.profileComplete ?: false
    }

    /**
     * Ottiene l'ID dell'utente corrente
     */
    fun getUserId(): String {
        return _currentUser.value?.id ?: ""
    }

    /**
     * Ottiene il nome completo dell'utente corrente
     */
    fun getUserName(): String {
        val user = _currentUser.value
        return if (user != null) {
            "${user.firstName} ${user.lastName}"
        } else {
            "User"
        }
    }
}