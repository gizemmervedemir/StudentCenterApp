package com.example.studentcenterapp.data.student

import com.example.studentcenterapp.model.Student

interface StudentDataSource {
    suspend fun getCurrentStudent(): Student
    suspend fun getStudentById(id: String): Student?
}