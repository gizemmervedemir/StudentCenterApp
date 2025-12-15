package com.example.studentcenterapp.data.department

import com.example.studentcenterapp.model.Department
import kotlinx.coroutines.flow.Flow

interface DepartmentDataSource {
    fun getDepartments(): Flow<List<Department>>
}