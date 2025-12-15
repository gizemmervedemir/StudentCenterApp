package com.example.studentcenterapp.data

import com.example.studentcenterapp.data.department.DepartmentRepository
import com.example.studentcenterapp.data.department.DepartmentRepositoryImpl
import com.example.studentcenterapp.data.department.InMemoryDepartmentDataSource

import com.example.studentcenterapp.data.student.InMemoryStudentDataSource
import com.example.studentcenterapp.data.student.StudentRepository
import com.example.studentcenterapp.data.student.StudentRepositoryImpl
object AppDI {
    val departmentRepository: DepartmentRepository by lazy {
        DepartmentRepositoryImpl(InMemoryDepartmentDataSource())
    }
    val studentRepository: StudentRepository by lazy {
        StudentRepositoryImpl(InMemoryStudentDataSource())
    }
}