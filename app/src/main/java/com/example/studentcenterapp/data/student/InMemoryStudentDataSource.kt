package com.example.studentcenterapp.data.student

import com.example.studentcenterapp.model.Student

class InMemoryStudentDataSource : StudentDataSource {

    // “logged in” mock student
    private val current = Student(
        id = "stu1",
        name = "Gizem Merve Demir",
        email = "gizem@example.com",
        studentNumber = "20201234",
    )

    private val students = listOf(
        current,
        Student(
            id = "stu2",
            name = "Eylül Akboğa",
            email = "eylul@example.com",
            studentNumber = "20211234",
        )
    )

    override suspend fun getCurrentStudent(): Student = current

    override suspend fun getStudentById(id: String): Student? {
        return students.find { it.id == id }
    }
}