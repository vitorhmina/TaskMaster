package com.example.taskmaster.retrofit

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Task(
    val id: Int,
    val name: String,
    val description: String?,
    val startDate: Date,
    @SerializedName("planned_end_date") val plannedEndDate: Date,
    val actualEndDate: Date?,
    val status: String?,
    val projectId: Int?
)