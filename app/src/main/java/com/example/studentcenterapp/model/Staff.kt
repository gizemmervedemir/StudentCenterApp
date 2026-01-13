package com.example.studentcenterapp.model

// Staff.kt
data class Staff(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val staffNo: String = "", // <--- Personel Numarası (Örn: 1001)
    val departmentId: String = "", // <--- StaffNo'nun son hanesi (Örn: 1)
    val role: String = "staff",
    val profilePictureUrl: String? = null
)