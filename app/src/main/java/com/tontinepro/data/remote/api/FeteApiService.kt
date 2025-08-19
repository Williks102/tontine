package com.tontinepro.data.remote.api

import com.tontinepro.data.models.FeteEvent
import com.tontinepro.data.models.FeteParticipant
import com.tontinepro.data.remote.dto.ApiResponse
import retrofit2.Response
import retrofit2.http.*

interface FeteApiService {

    @GET("fete/events")
    suspend fun getAllFeteEvents(): Response<List<FeteEvent>>

    @GET("fete/events/active")
    suspend fun getActiveFeteEvents(): Response<List<FeteEvent>>

    @POST("fete/events/{eventId}/participate")
    suspend fun participateInFete(
        @Path("eventId") eventId: String,
        @Field("userId") userId: String
    ): Response<FeteParticipant>

    @GET("users/{userId}/fete-participations")
    suspend fun getUserFeteParticipations(@Path("userId") userId: String): Response<List<FeteParticipant>>

    @PATCH("fete/events")
    suspend fun updateFeteEventStatus(
        @Field("month") month: Int,
        @Field("year") year: Int,
        @Field("isActive") isActive: Boolean
    ): Response<ApiResponse>
}