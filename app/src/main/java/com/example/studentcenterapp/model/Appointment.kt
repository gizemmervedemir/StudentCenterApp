package com.example.studentcenterapp.model

data class Appointment(
    val id: String,
    val studentId: String, // Okul numarası (Örn: 20230604024)
    val studentName: String = "",
    val staffId: String,
    val serviceName: String = "",
    val departmentName: String = "",
    val serviceId: String,
    val timeSlotId: String,
    val appointmentDate: String = "",
    val type: String = "office", // "office" veya "online"
    val status: String // "pending", "approved", "cancelled"
)