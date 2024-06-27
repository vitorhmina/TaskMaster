package com.example.taskmaster.retrofit

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val name: String,
    val token: String,
    @SerializedName("user_id")val userId: Int,
    @SerializedName("user_type") val userType: String
)
