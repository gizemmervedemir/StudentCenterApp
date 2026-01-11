package com.example.studentcenterapp.data.service

import com.example.studentcenterapp.data.local.database.AppDatabase
import com.example.studentcenterapp.data.local.database.entity.ServiceEntity
import com.example.studentcenterapp.model.Service
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Room-based implementation of ServiceRepository
 * Reads services from Room Database (populated on first app launch)
 */
class RoomServiceRepository(
    private val database: AppDatabase
) : ServiceRepository {

    override fun getServicesByDepartment(departmentId: String): Flow<List<Service>> {
        return database.serviceDao().getServicesByDepartmentId(departmentId)
            .map { entities -> entities.map { entity -> entity.toModel() } }
    }
}

