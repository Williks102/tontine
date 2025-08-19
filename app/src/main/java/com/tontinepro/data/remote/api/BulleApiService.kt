package com.tontinepro.data.remote.api

import com.tontinepro.data.models.Bulle
import com.tontinepro.data.models.BulleMember
import com.tontinepro.data.models.enums.FormuleType
import com.tontinepro.data.remote.dto.ApiResponse
import retrofit2.Response
import retrofit2.http.*

interface BulleApiService {

    @GET("users/{userId}/bulle")
    suspend fun getUserBulle(@Path("userId") userId: String): Response<Bulle>

    @POST("bulles/join")
    suspend fun joinBulle(
        @Field("userId") userId: String,
        @Field("formule") formule: FormuleType
    ): Response<Bulle>

    @GET("bulles/{id}/members")
    suspend fun getBulleMembers(@Path("id") bulleId: String): Response<List<BulleMember>>

    @PATCH("bulles/{id}/progress")
    suspend fun updateBulleProgress(@Path("id") bulleId: String): Response<Bulle>

    @GET("bulles")
    suspend fun getAllBulles(): Response<List<Bulle>>

    @POST("bulles")
    suspend fun createBulle(@Body formule: FormuleType): Response<Bulle>
}
