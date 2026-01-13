package com.example.studentcenterapp.data.student

object StudentSession {
    var currentStudentId: String? = null     // Firebase UID
    var studentNumber: String? = null        // Gerçek Okul No (Örn: 2024001)
    var currentStudentName: String? = null   // Ad Soyad (Örn: Ahmet Yılmaz)
}