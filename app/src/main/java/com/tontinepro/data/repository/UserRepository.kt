package com.tontinepro.data.repository

import com.tontinepro.data.local.dao.UserDao
import com.tontinepro.data.models.User
import com.tontinepro.data.models.Parrainage
import com.tontinepro.data.models.enums.UserStatus
import com.tontinepro.data.models.enums.FormuleType
import com.tontinepro.data.remote.api.UserApiService
import com.tontinepro.utils.Result
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userApi: UserApiService,
    private val userDao: UserDao
) {

    suspend fun getUserById(userId: String): User? {
        return try {
            // Essayer d'abord en local
            val localUser = userDao.getUserById(userId)?.toDomain()
            if (localUser != null) {
                localUser
            } else {
                // Récupérer depuis l'API
                val response = userApi.getUserById(userId)
                if (response.isSuccessful && response.body() != null) {
                    val user = response.body()!!
                    userDao.insertUser(user.toEntity())
                    user
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            // Retourner les données locales en cas d'erreur réseau
            userDao.getUserById(userId)?.toDomain()
        }
    }

    suspend fun updateUser(user: User): Result<User> {
        return try {
            val response = userApi.updateUser(user.id, user)
            if (response.isSuccessful && response.body() != null) {
                val updatedUser = response.body()!!
                userDao.updateUser(updatedUser.toEntity())
                Result.Success(updatedUser)
            } else {
                Result.Error(response.message() ?: "Erreur lors de la mise à jour")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Erreur réseau")
        }
    }

    suspend fun getUserParrainages(userId: String): List<Parrainage> {
        return try {
            val response = userApi.getUserParrainages(userId)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun generateParrainCode(userId: String): Result<String> {
        return try {
            val response = userApi.generateParrainCode(userId)
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!.parrainCode)
            } else {
                Result.Error(response.message() ?: "Erreur lors de la génération du code")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Erreur réseau")
        }
    }

    // Méthodes pour l'admin
    suspend fun getAllUsers(): List<User> {
        return try {
            val response = userApi.getAllUsers()
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun updateUserStatus(userId: String, status: UserStatus): Result<Boolean> {
        return try {
            val response = userApi.updateUserStatus(userId, status)
            if (response.isSuccessful) {
                Result.Success(true)
            } else {
                Result.Error(response.message() ?: "Erreur lors de la mise à jour du statut")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Erreur réseau")
        }
    }

    suspend fun updateFormuleStatus(formule: FormuleType, isActive: Boolean): Result<Boolean> {
        return try {
            val response = userApi.updateFormuleStatus(formule, isActive)
            if (response.isSuccessful) {
                Result.Success(true)
            } else {
                Result.Error(response.message() ?: "Erreur lors de la mise à jour de la formule")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Erreur réseau")
        }
    }
}
