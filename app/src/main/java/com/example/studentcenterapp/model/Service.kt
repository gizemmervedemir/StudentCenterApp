package com.example.studentcenterapp.model

data class Service(
    val id: String,
    val departmentId: String,
    val name: String,
    val description: String,
    val durationMinutes: Int
)