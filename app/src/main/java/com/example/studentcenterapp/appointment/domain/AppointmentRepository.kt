package com.example.studentcenterapp.appointment.domain

import kotlinx.coroutines.flow.Flow
import kotlin.Result

interface AppointmentRepository {
    fun getAppointmentsForStudent(studentId: String): Flow<List<Appointment>>

    suspend fun createAppointment(
        studentId: String,
        serviceId: String,
        staffId: String?,
        timeSlotId: String
    ): Result<Appointment>

    suspend fun cancelAppointment(appointmentId: String): Result<Unit>
}