package com.example.taskmaster.retrofit

import com.google.gson.annotations.SerializedName

data class UserProject(
    val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("role") val role: String,
    val rating: Float?,
    val userId: Int?,
    val projectId: Int?
)