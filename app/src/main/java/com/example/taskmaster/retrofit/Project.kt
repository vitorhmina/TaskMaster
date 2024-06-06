package com.example.taskmaster.retrofit

import java.util.Date

data class Project(
    val id: Int,
    val name: String,
    val description: String,
    val startDate: Date,
    val plannedEndDate: Date,
    val endDate: Date?,
    val status: String,
)