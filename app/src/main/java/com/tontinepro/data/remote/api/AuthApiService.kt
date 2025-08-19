package com.tontinepro.data.remote.api

import com.tontinepro.data.models.User
import com.tontinepro.data.models.dto.RegistrationData
import com.tontinepro.data.models.dto.VerificationData
import com.tontinepro.data.remote.dto.ApiResponse
import retrofit2.Response
import retrofit2.http.*

interface AuthApiService {

    @POST("auth/register")
    suspend fun register(@Body registrationData: RegistrationData): Response<User>

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<User>

    @POST("auth/verify")
    suspend fun verifyUser(@Body verificationData: VerificationData): Response<User>

    @POST("auth/logout")
    suspend fun logout(): Response<ApiResponse>

    @FormUrlEncoded
    @POST("auth/refresh-token")
    suspend fun refreshToken(@Field("refresh_token") refreshToken: String): Response<TokenResponse>

    @FormUrlEncoded
    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Field("email") email: String): Response<ApiResponse>
}
