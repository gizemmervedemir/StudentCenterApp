package com.example.studentcenterapp.data.student

/**
 * Basit session holder.
 * Projede gerçek auth/session yapısı yoksa studentId taşımak için kullanılır.
 */
object StudentSession {
    var currentStudentId: String = ""
}
