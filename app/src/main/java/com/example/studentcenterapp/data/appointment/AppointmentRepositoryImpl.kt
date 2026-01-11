package com.example.studentcenterapp.data.appointment

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppointmentRepositoryImpl(
    private val dataSource: InMemoryAppointmentDataSource
) : AppointmentRepository {

    override fun getAppointmentsForStudent(studentId: String): Flow<List<AppointmentRecord>> {
        return dataSource.appointmentsFlow.map { list ->
            list.filter { it.studentId == studentId }
        }
    }

    override suspend fun createAppointment(
        studentId: String,
        serviceId: String,
        timeSlotId: String,
        scheduledStartMillis: Long
    ): Result<Unit> {
        return runCatching {
            dataSource.add(
                studentId = studentId,
                serviceId = serviceId,
                timeSlotId = timeSlotId,
                scheduledStartMillis = scheduledStartMillis
            )
        }
    }

    override suspend fun cancelAppointment(appointmentId: String): Result<Unit> {
        return runCatching {
            dataSource.remove(appointmentId)
        }
    }
}
