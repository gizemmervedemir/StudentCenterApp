package com.example.studentcenterapp.data.student

import com.example.studentcenterapp.model.Student

class StudentRepositoryImpl(
    private val dataSource: StudentDataSource
) : StudentRepository {

    override suspend fun getCurrentStudent(): Student {
        return dataSource.getCurrentStudent()
    }

    override suspend fun getStudentById(id: String): Student? {
        return dataSource.getStudentById(id)
    }
}