package com.example.studentcenterapp.model

import java.time.LocalDateTime

data class Department(
    val id: String,
    val name: String
)

data class Service(
    val id: String,
    val departmentId: String,
    val name: String,
    val description: String? = null
)

data class TimeSlot(
    val id: String,
    val serviceId: String,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val isReserved: Boolean = false
)

enum class AppointmentStatus {
    PLANNED,
    COMPLETED,
    CANCELLED
}

data class Appointment(
    val id: String,
    val studentId: String,
    val serviceId: String,
    val timeSlotId: String,
    val status: AppointmentStatus,
    val createdAt: LocalDateTime
)