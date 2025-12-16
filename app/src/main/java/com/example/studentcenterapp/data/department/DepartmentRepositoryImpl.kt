package com.example.studentcenterapp.data.department

import com.example.studentcenterapp.model.Department
import kotlinx.coroutines.flow.Flow

class DepartmentRepositoryImpl(
    private val dataSource: DepartmentDataSource
) : DepartmentRepository {

    override fun getDepartments(): Flow<List<Department>> {
        return dataSource.getDepartments()
    }
}