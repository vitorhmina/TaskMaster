package com.example.taskmaster.retrofit

import java.util.Date

data class UserTask(
    val id: Int,
    val date: Date?,
    val location: String?,
    val completionRate: Float?,
    val timeSpent: Float?,
    val userId: Int?,
    val taskId: Int?
)