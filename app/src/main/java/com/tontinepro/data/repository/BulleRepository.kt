package com.tontinepro.data.repository

import com.tontinepro.data.local.dao.BulleDao
import com.tontinepro.data.models.Bulle
import com.tontinepro.data.models.BulleMember
import com.tontinepro.data.models.enums.FormuleType
import com.tontinepro.data.remote.api.BulleApiService
import com.tontinepro.utils.Result
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BulleRepository @Inject constructor(
    private val bulleApi: BulleApiService,
    private val bulleDao: BulleDao
) {

    suspend fun getUserBulle(userId: String): Bulle? {
        return try {
            val response = bulleApi.getUserBulle(userId)
            if (response.isSuccessful && response.body() != null) {
                val bulle = response.body()!!
                bulleDao.insertBulle(bulle.toEntity())
                bulle
            } else {
                // Récupérer depuis la base locale
                bulleDao.getUserBulle(userId)?.toDomain()
            }
        } catch (e: Exception) {
            bulleDao.getUserBulle(userId)?.toDomain()
        }
    }

    suspend fun joinBulle(userId: String, formule: FormuleType): Result<Bulle> {
        return try {
            val response = bulleApi.joinBulle(userId, formule)
            if (response.isSuccessful && response.body() != null) {
                val bulle = response.body()!!
                bulleDao.insertBulle(bulle.toEntity())
                Result.Success(bulle)
            } else {
                Result.Error(response.message() ?: "Erreur lors de l'inscription à la bulle")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Erreur réseau")
        }
    }

    suspend fun getBulleMembers(bulleId: String): List<BulleMember> {
        return try {
            val response = bulleApi.getBulleMembers(bulleId)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun updateBulleProgress(bulleId: String): Result<Bulle> {
        return try {
            val response = bulleApi.updateBulleProgress(bulleId)
            if (response.isSuccessful && response.body() != null) {
                val bulle = response.body()!!
                bulleDao.updateBulle(bulle.toEntity())
                Result.Success(bulle)
            } else {
                Result.Error(response.message() ?: "Erreur lors de la mise à jour")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Erreur réseau")
        }
    }

    // Méthodes pour l'admin
    suspend fun getAllBulles(): List<Bulle> {
        return try {
            val response = bulleApi.getAllBulles()
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun createBulle(formule: FormuleType): Result<Bulle> {
        return try {
            val response = bulleApi.createBulle(formule)
            if (response.isSuccessful && response.body() != null) {
                val bulle = response.body()!!
                bulleDao.insertBulle(bulle.toEntity())
                Result.Success(bulle)
            } else {
                Result.Error(response.message() ?: "Erreur lors de la création de la bulle")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Erreur réseau")
        }
    }
}