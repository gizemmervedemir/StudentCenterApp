package com.example.studentcenterapp.model

data class TimeSlot(
    val id: String,
    val serviceId: String,
    val date: String,
    val startTime: String,
    val endTime: String,
    val type: String = "office",
    val isReserved: Boolean = false
)