package com.tontinepro.data.models.states

import com.tontinepro.data.models.User

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object RegistrationSuccess : AuthState()
    object VerificationSuccess : AuthState()
    data class LoginSuccess(val user: User) : AuthState()
    object LoggedOut : AuthState()
    data class Error(val message: String) : AuthState()
}

