package com.tontinepro.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.tontinepro.data.repository.AuthRepository
import com.tontinepro.data.repository.UserRepository
import com.tontinepro.data.models.User
import com.tontinepro.data.models.dto.RegistrationData
import com.tontinepro.data.models.dto.VerificationData
import com.tontinepro.data.models.states.AuthState
import com.tontinepro.utils.Result

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    private val _registrationData = MutableLiveData<RegistrationData>()
    val registrationData: LiveData<RegistrationData> = _registrationData

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun register(userData: RegistrationData) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null

                val result = authRepository.register(userData)
                if (result.isSuccess) {
                    _registrationData.value = userData
                    _authState.value = AuthState.RegistrationSuccess
                } else {
                    when (result) {
                        is Result.Error -> _error.value = result.message
                        else -> _error.value = "Erreur inconnue"
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Erreur lors de l'inscription"
            } finally {
                _loading.value = false
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null

                val result = authRepository.login(email, password)
                if (result.isSuccess) {
                    val user = result.getOrNull()
                    if (user != null) {
                        _authState.value = AuthState.LoginSuccess(user)
                    } else {
                        _error.value = "Erreur lors de la récupération des données utilisateur"
                    }
                } else {
                    when (result) {
                        is Result.Error -> _error.value = result.message
                        else -> _error.value = "Email ou mot de passe incorrect"
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Erreur de connexion"
            } finally {
                _loading.value = false
            }
        }
    }

    fun verifyUser(verificationData: VerificationData) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null

                val result = authRepository.verifyUser(verificationData)
                if (result.isSuccess) {
                    _authState.value = AuthState.VerificationSuccess
                } else {
                    when (result) {
                        is Result.Error -> _error.value = result.message
                        else -> _error.value = "Erreur lors de la vérification"
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Erreur lors de la vérification"
            } finally {
                _loading.value = false
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                authRepository.logout()
                _authState.value = AuthState.LoggedOut
                _error.value = null
            } catch (e: Exception) {
                // Même en cas d'erreur, on considère que l'utilisateur est déconnecté localement
                _authState.value = AuthState.LoggedOut
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun resetAuthState() {
        _authState.value = AuthState.Idle
    }
}
        val totalPaid = transactions
            .filter { it.type == TransactionType.PAYMENT && it.isSuccessful }
            .sumOf { it.amount }

        val totalEarned = transactions
            .filter { it.type == TransactionType.WITHDRAWAL && it.isSuccessful }
            .sumOf { it.amount }

        val parrainageEarnings = parrainages
            .filter { it.isActive }
            .sumOf { it.gain }

        val position = bulle?.members?.find { it.userId == _user.value?.id }?.position ?: 0
        val cyclesRemaining = if (position > 0) position - (bulle?.currentCycle ?: 0) else 0

        _dashboardStats.value = DashboardStats(
            totalPaid = totalPaid,
            totalEarned = totalEarned,
            parrainageEarnings = parrainageEarnings,
            activeParrainages = parrainages.size,
            cyclesRemaining = cyclesRemaining,
            bulleProgress = bulle?.progress ?: 0f
        )
    }

    fun refreshData() {
        _user.value?.let { user ->
            loadDashboardData(user.id)
        }
    }
}
