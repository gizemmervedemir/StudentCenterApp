package com.example.studentcenterapp.data

import com.example.studentcenterapp.data.department.DepartmentRepository
import com.example.studentcenterapp.data.department.DepartmentRepositoryImpl
import com.example.studentcenterapp.data.department.InMemoryDepartmentDataSource

object AppDI {
    val departmentRepository: DepartmentRepository by lazy {
        DepartmentRepositoryImpl(InMemoryDepartmentDataSource())
    }
}