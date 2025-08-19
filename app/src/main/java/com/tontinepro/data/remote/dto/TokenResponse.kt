package com.tontinepro.data.remote.dto

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long
)
