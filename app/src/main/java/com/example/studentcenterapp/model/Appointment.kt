package com.example.studentcenterapp.model

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Appointment(
    val id: String = "",
    val studentId: String = "",
    val studentName: String = "",
    val staffId: String = "not_assigned", // Varsayılan değer eklendi
    val serviceName: String = "",
    val departmentName: String = "",
    val serviceId: String = "",
    val timeSlotId: String = "",
    val appointmentDate: String = "",
    val type: String = "office",
    val status: String = "pending", // Varsayılan değer eklendi
    val startTime: String = "",
    val endTime: String = ""
) {
    // Firebase için boş constructor
    constructor() : this("", "", "", "not_assigned", "", "", "", "", "", "office", "pending", "", "")
}