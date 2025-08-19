package com.tontinepro.data.repository

import com.tontinepro.data.models.FeteEvent
import com.tontinepro.data.models.FeteParticipant
import com.tontinepro.data.remote.api.FeteApiService
import com.tontinepro.utils.Result
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeteRepository @Inject constructor(
    private val feteApi: FeteApiService
) {

    suspend fun getAllFeteEvents(): List<FeteEvent> {
        return try {
            val response = feteApi.getAllFeteEvents()
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getActiveFeteEvents(): List<FeteEvent> {
        return try {
            val response = feteApi.getActiveFeteEvents()
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun participateInFete(eventId: String, userId: String): Result<FeteParticipant> {
        return try {
            val response = feteApi.participateInFete(eventId, userId)
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                Result.Error(response.message() ?: "Erreur lors de l'inscription à la fête")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Erreur réseau")
        }
    }

    suspend fun getUserFeteParticipations(userId: String): List<FeteParticipant> {
        return try {
            val response = feteApi.getUserFeteParticipations(userId)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun updateFeteEventStatus(month: Int, year: Int, isActive: Boolean): Result<Boolean> {
        return try {
            val response = feteApi.updateFeteEventStatus(month, year, isActive)
            if (response.isSuccessful) {
                Result.Success(true)
            } else {
                Result.Error(response.message() ?: "Erreur lors de la mise à jour")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Erreur réseau")
        }
    }
}
