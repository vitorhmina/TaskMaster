package com.example.taskmaster.retrofit

data class User(
    val id: Int,
    val email: String,
    val password: String,
    val name: String,
    val photo: String?,
    val userTypeId: Int?
)