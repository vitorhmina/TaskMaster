package com.example.taskmaster.retrofit

import com.google.gson.annotations.SerializedName

data class User(
    val id: Int,
    val email: String,
    val password: String,
    val name: String,
    val photo: String?,
    @SerializedName("user_type") val userType: String?
)