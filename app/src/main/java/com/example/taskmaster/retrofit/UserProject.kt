package com.example.taskmaster.retrofit

import com.google.gson.annotations.SerializedName

data class UserProject(
    val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("email") val email: String,
    @SerializedName("role") val role: String,
    val rating: String?,
    @SerializedName("project_id") val projectId: Int
)