package com.example.taskmaster.retrofit

import java.util.Date
import com.google.gson.annotations.SerializedName


data class Project(
    val id: Int,
    val name: String,
    val description: String,
    val startDate: Date,
    @SerializedName("planned_end_date") val plannedEndDate: Date,
    val endDate: Date?,
    val status: String,
    val totalTasks: Int,
    val completedTasks: Int
)