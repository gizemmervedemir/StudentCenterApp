package com.example.studentcenterapp.data.service

import kotlinx.coroutines.flow.Flow


interface ServiceRepository {
    fun getServicesByDepartment(departmentId: String): Flow<List<Service>>
}