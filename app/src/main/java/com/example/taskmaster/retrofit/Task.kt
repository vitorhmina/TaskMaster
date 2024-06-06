package com.example.taskmaster.retrofit

import java.util.Date

data class Task(
    val id: Int,
    val name: String,
    val description: String?,
    val startDate: Date,
    val plannedEndDate: Date,
    val actualEndDate: Date?,
    val status: String?,
    val projectId: Int?
)