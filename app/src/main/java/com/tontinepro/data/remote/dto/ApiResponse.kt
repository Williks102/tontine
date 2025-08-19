package com.tontinepro.data.remote.dto

data class ApiResponse(
    val success: Boolean,
    val message: String,
    val data: Any? = null
)