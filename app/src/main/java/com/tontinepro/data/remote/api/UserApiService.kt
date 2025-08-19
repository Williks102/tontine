package com.tontinepro.data.remote.api

import com.tontinepro.data.models.User
import com.tontinepro.data.models.Parrainage
import com.tontinepro.data.models.enums.UserStatus
import com.tontinepro.data.models.enums.FormuleType
import com.tontinepro.data.remote.dto.ApiResponse
import retrofit2.Response
import retrofit2.http.*

interface UserApiService {

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") userId: String): Response<User>

    @PUT("users/{id}")
    suspend fun updateUser(@Path("id") userId: String, @Body user: User): Response<User>

    @GET("users/{id}/parrainages")
    suspend fun getUserParrainages(@Path("id") userId: String): Response<List<Parrainage>>

    @POST("users/{id}/parrain-code")
    suspend fun generateParrainCode(@Path("id") userId: String): Response<ParrainCodeResponse>

    @GET("users")
    suspend fun getAllUsers(): Response<List<User>>

    @PATCH("users/{id}/status")
    suspend fun updateUserStatus(
        @Path("id") userId: String,
        @Body status: UserStatus
    ): Response<ApiResponse>

    @PATCH("formules/{formule}/status")
    suspend fun updateFormuleStatus(
        @Path("formule") formule: FormuleType,
        @Body isActive: Boolean
    ): Response<ApiResponse>
}