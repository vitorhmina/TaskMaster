package com.example.taskmaster.retrofit

import java.util.Date
import com.google.gson.annotations.SerializedName

data class Project(
    val id: Int,
    val name: String,
    val description: String,
    @SerializedName("start_date")val startDate: String,
    @SerializedName("planned_end_date") val plannedEndDate: String,
    @SerializedName("actual_end_date") val actualEndDate: String?,
    val status: String,
    @SerializedName("totalTasks")val totalTasks: Int?,
    @SerializedName("completedTasks")val completedTasks: Int?
)