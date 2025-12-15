package com.example.studentcenterapp.data.department

import com.example.studentcenterapp.model.Department
import kotlinx.coroutines.flow.Flow

interface DepartmentRepository {
    fun getDepartments(): Flow<List<Department>>
}