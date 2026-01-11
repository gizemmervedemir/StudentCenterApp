package com.example.studentcenterapp.data.department

import com.example.studentcenterapp.data.local.database.AppDatabase
import com.example.studentcenterapp.data.local.database.entity.DepartmentEntity
import com.example.studentcenterapp.model.Department
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Room-based implementation of DepartmentRepository
 * Reads departments from Room Database (populated on first app launch)
 */
class RoomDepartmentRepository(
    private val database: AppDatabase
) : DepartmentRepository {

    override fun getDepartments(): Flow<List<Department>> {
        return database.departmentDao().getAllDepartments()
            .map { entities -> entities.map { entity -> entity.toModel() } }
    }
}

