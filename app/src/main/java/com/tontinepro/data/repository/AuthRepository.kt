package com.tontinepro.data.repository

import com.tontinepro.data.local.dao.UserDao
import com.tontinepro.data.models.User
import com.tontinepro.data.models.dto.RegistrationData
import com.tontinepro.data.models.dto.VerificationData
import com.tontinepro.data.models.enums.UserStatus
import com.tontinepro.data.remote.api.AuthApiService
import com.tontinepro.utils.Result
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val authApi: AuthApiService,
    private val userDao: UserDao
) {

    suspend fun register(registrationData: RegistrationData): Result<User> {
        return try {
            val response = authApi.register(registrationData)
            if (response.isSuccessful && response.body() != null) {
                val user = response.body()!!
                // Sauvegarder en local
                userDao.insertUser(user.toEntity())
                Result.Success(user)
            } else {
                Result.Error(response.message() ?: "Erreur lors de l'inscription")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Erreur réseau")
        }
    }

    suspend fun login(email: String, password: String): Result<User> {
        return try {
            val response = authApi.login(email, password)
            if (response.isSuccessful && response.body() != null) {
                val user = response.body()!!
                userDao.insertUser(user.toEntity())
                Result.Success(user)
            } else {
                Result.Error(response.message() ?: "Email ou mot de passe incorrect")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Erreur de connexion")
        }
    }

    suspend fun verifyUser(verificationData: VerificationData): Result<User> {
        return try {
            val response = authApi.verifyUser(verificationData)
            if (response.isSuccessful && response.body() != null) {
                val user = response.body()!!
                userDao.updateUser(user.toEntity())
                Result.Success(user)
            } else {
                Result.Error(response.message() ?: "Erreur lors de la vérification")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Erreur réseau")
        }
    }

    suspend fun logout() {
        try {
            authApi.logout()
            userDao.clearAll()
        } catch (e: Exception) {
            // Log l'erreur mais continue le logout local
            userDao.clearAll()
        }
    }

    suspend fun getCurrentUser(): User? {
        return userDao.getCurrentUser()?.toDomain()
    }
}
