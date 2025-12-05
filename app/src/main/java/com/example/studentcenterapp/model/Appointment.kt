package com.example.studentcenterapp.model

data class Appointment(
    val id: String,
    val studentId: String,
    val serviceId: String,
    val timeSlotId: String,
    val status: String // "pending" | "approved" | "cancelled"
)