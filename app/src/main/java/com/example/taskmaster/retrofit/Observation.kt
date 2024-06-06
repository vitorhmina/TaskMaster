package com.example.taskmaster.retrofit

data class Observation(
    val id: Int,
    val comments: String,
    val photo: String?,
    val taskId: Int?,
    val userId: Int?
)