package com.example.studentcenterapp.model

data class Appointment(
    val id: String,
    val studentId: String,
    val staffId: String,
    val serviceName: String = "", // Listede direkt göstermek için
    val departmentName: String = "", // "Psikolojik Danışmanlık" gibi
    val serviceId: String,
    val timeSlotId: String,
    val appointmentDate: String = "", // Kolay sıralama ve görüntüleme için
    val type: String = "office", // Online mı ofis mi?
    val status: String,// "pending" | "approved" | "cancelled",
    val startTime: String = "",
    val endTime: String = ""
)
