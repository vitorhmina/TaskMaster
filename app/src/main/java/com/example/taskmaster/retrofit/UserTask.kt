package com.example.taskmaster.retrofit

import com.google.gson.annotations.SerializedName
import java.util.Date

data class UserTask(
    val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("email") val email: String,
    @SerializedName("date") val date: String?,
    val location: String?,
    @SerializedName("completion_rate") val completionRate: String?,
    @SerializedName("time_spent")val timeSpent: String?,
    @SerializedName("task_id") val taskId: Int
)