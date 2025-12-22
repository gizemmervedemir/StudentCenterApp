package com.example.studentcenterapp.data.service

import com.example.studentcenterapp.model.Service
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

    private val services = listOf(
        Service(
            id = "s1",
            departmentId = "dep1",
            name = "Dietitian Appointment",
            description = "Nutrition consultation and meal planning support.",
            durationMinutes = 30
        ),
        Service(
            id = "s2",
            departmentId = "dep1",
            name = "Psychologist Appointment",
            description = "Counseling session with university psychologist.",
            durationMinutes = 45
        ),
        Service(
            id = "s3",
            departmentId = "dep2",
            name = "Sports Facility Session",
            description = "Book a time at the sports facility.",
            durationMinutes = 60
        )
    )

    override fun getServicesByDepartment(departmentId: String): Flow<List<Service>> {
        return flowOf(services.filter { it.departmentId == departmentId })
    }
}