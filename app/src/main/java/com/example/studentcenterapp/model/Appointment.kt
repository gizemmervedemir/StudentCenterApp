package com.example.studentcenterapp.model

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Appointment(
    val id: String = "",
    val studentUid: String = "",
    val studentId: String = "",
    val studentName: String = "",
    val staffId: String = "not_assigned",
    val serviceName: String = "",
    val departmentName: String = "",
    val departmentId: String = "",
    val serviceId: String = "",
    val timeSlotId: String = "",
    val appointmentDate: String = "",
    val type: String = "office",
    val status: String = "pending",
    val startTime: String = "",
    val endTime: String = ""
) {
    // Firebase için boş constructor (Parametre sırasına dikkat ederek güncelledik)
    constructor() : this("", "", "", "not_assigned", "", "", "", "", "", "", "office", "pending", "", "")
}