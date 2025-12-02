package com.example.studentcenterapp.appointment.data

import com.example.studentcenterapp.appointment.domain.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import kotlin.Result

class AppointmentRepositoryImpl(
    private val dataSource: AppointmentDataSource
) : AppointmentRepository {

    override fun getAppointmentsForStudent(studentId: String): Flow<List<Appointment>> {
        return dataSource.getAppointmentsState().map { list ->
            list.filter { it.studentId == studentId }
        }
    }

    override suspend fun createAppointment(
        studentId: String,
        serviceId: String,
        staffId: String?,
        timeSlotId: String
    ): Result<Appointment> {
        return try {
            // note: validation of service/time/staff belongs to other repos (stubs exist)
            val id = UUID.randomUUID().toString()
            val appt = Appointment(
                id = id,
                studentId = studentId,
                serviceId = serviceId,
                staffId = staffId, // TODO: handled by me(developer) if staff logic missing
                timeSlotId = timeSlotId,
                timestamp = System.currentTimeMillis(),
                status = AppointmentStatusRules.nextAfterCreate()
            )
            dataSource.add(appt)
            Result.success(appt)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    override suspend fun cancelAppointment(appointmentId: String): Result<Unit> {
        val current = dataSource.findById(appointmentId)
            ?: return Result.failure(Exception("Appointment not found"))

        if (!AppointmentStatusRules.canCancel(current.status)) {
            return Result.failure(Exception("Cannot cancel appointment from status: ${current.status}"))
        }

        val updated = current.copy(status = AppointmentStatus.CANCELLED)
        dataSource.update(updated)
        return Result.success(Unit)
    }
}