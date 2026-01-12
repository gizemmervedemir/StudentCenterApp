package com.example.studentcenterapp.data.student

import com.example.studentcenterapp.model.Student

interface StudentRepository {
    suspend fun getCurrentStudent(): Student
    suspend fun getStudentById(id: String): Student?

    suspend fun login(email: String, password: String): Result<Student>
    suspend fun signup(
        name: String,
        surname: String,
        schoolNumber: String,
        email: String,
        password: String,
        birthDay: String,
        birthMonth: String,
        birthYear: String
    ): Result<Student>
    // Mevcutların altına ekle
    suspend fun updateStudentProfile(id: String, name: String, email: String): Result<Unit>
}