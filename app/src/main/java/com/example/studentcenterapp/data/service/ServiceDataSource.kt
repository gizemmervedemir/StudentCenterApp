package com.example.studentcenterapp.data.service

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * Data source interface that provides service data.
 * Temporary stub will remain here until Şahan's actual InMemoryDataSource design arrives.
 */
interface ServiceDataSource {
    fun getServicesByDepartment(departmentId: String): Flow<List<Service>>
}

/**
 * TODO: implemented by şahan
 */
class InMemoryServiceDataSource : ServiceDataSource {

    // Dummy service list
    // Your own service stub is used until the Shared Service model is introduced.
    private val services = listOf(
        Service(
            id = "s1",
            departmentId = "dep1",
            name = "dietitian appointment request"
        ),
        Service(
            id = "s2",
            departmentId = "dep1",
            name = "psychologist appointment request"
        ),
        Service(
            id = "s3",
            departmentId = "dep2",
            name = "sport appointment request"
        )
    )

    override fun getServicesByDepartment(departmentId: String): Flow<List<Service>> {
        val filtered = services.filter { it.departmentId == departmentId }
        return flowOf(filtered)
    }
}