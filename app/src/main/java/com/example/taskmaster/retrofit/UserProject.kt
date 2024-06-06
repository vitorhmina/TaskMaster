package com.example.taskmaster.retrofit

data class UserProject(
    val id: Int,
    val role: String,
    val rating: Float?,
    val userId: Int?,
    val projectId: Int?
)