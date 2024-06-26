package com.example.taskmaster.retrofit

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Task(
    val id: Int,
    val name: String,
    val description: String?,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("planned_end_date") val plannedEndDate: String,
    val actualEndDate: String?,
    val status: String?,
    @SerializedName("project_id") val projectId: Int
)