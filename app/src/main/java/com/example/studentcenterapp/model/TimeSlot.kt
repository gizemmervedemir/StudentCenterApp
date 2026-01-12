package com.example.studentcenterapp.model

data class TimeSlot(
    val id: String,
    val serviceId: String,
    val date: String,      // Örn: "2025-10-20"
    val startTime: String, // Örn: "09:00"
    val endTime: String,
    val type: String = "office", // "office" veya "online"
    val isReserved: Boolean = false,
    val dateLabel: String = "" // Örn: "20 Ekim, Pazartesi" (UI'da başlık olarak kullanacağız)
)