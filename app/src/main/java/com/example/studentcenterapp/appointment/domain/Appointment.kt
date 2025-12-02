package com.example.studentcenterapp.appointment.domain


data class Appointment(
    val id: String,
    val studentId: String,
    val serviceId: String,
    val staffId: String?,
    val timeSlotId: String,
    val timestamp: Long,
    val status: AppointmentStatus
)
