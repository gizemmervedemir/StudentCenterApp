package com.example.studentcenterapp.model

import com.google.firebase.Timestamp

data class ChatMessage(
    val senderId: String = "",
    val text: String = "",
    val timestamp: Timestamp? = null
)

data class Conversation(
    val id: String = "",           // studentId_serviceId
    val studentId: String = "",
    val studentName: String = "",
    val serviceId: String = "",
    val serviceName: String = "",   // Öğrenci ekranında başlık olacak
    val lastMessage: String = "",
    val timestamp: Timestamp? = null,
    val participants: List<String> = emptyList() // Firebase araması için
)