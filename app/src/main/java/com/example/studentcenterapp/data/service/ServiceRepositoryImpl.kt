package com.example.studentcenterapp.data.service

import com.example.studentcenterapp.model.Service
import kotlinx.coroutines.flow.Flow

class ServiceRepositoryImpl(
    private val dataSource: ServiceDataSource
) : ServiceRepository {

    override fun getServicesByDepartment(departmentId: String): Flow<List<Service>> {
        return dataSource.getServicesByDepartment(departmentId)
    }
}