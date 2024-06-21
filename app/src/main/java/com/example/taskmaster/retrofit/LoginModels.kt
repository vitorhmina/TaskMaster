package com.example.taskmaster.retrofit

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val name: String,
    val token: String,
    val userId: Int,
    val userType: String
)
