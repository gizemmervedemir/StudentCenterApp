package com.example.studentcenterapp.model

data class Department(
    val id: String,
    val name: String,
    val description: String,
    val location: String = ""
)