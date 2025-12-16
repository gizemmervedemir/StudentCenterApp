package com.example.studentcenterapp.data.student

import com.example.studentcenterapp.model.Student

interface StudentRepository {
    suspend fun getCurrentStudent(): Student
    suspend fun getStudentById(id: String): Student?
}