package com.example.studentcenterapp.model

data class Staff(
    val id: String,
    val name: String,
    val role: String,
    val departmentId: String,
    val profilePictureUrl: String? = null
)
