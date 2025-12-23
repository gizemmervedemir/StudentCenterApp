package com.example.studentcenterapp.data.staff

import com.example.studentcenterapp.model.Appointment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StaffRepositoryImpl(
    private val dataSource: AppointmentAdminDataSource
) : StaffRepository {

    override fun getPendingAppointmentsForStaff(staffId: String): Flow<List<Appointment>> {
        return dataSource.observeAppointments().map { list ->
            list.filter { appt ->
                appt.status == InMemoryAppointmentAdminDataSource.STATUS_PENDING &&
                        dataSource.getAssignedStaffId(appt.id) == staffId
            }
        }
    }

    override suspend fun approveAppointment(appointmentId: String): Result<Unit> {
        val ok = dataSource.updateStatus(
            appointmentId,
            InMemoryAppointmentAdminDataSource.STATUS_APPROVED
        )
        return if (ok) Result.success(Unit)
        else Result.failure(IllegalArgumentException("Invalid appointmentId: $appointmentId"))
    }

    override suspend fun rejectAppointment(appointmentId: String): Result<Unit> {
        val ok = dataSource.updateStatus(
            appointmentId,
            InMemoryAppointmentAdminDataSource.STATUS_CANCELLED
        )
        return if (ok) Result.success(Unit)
        else Result.failure(IllegalArgumentException("Invalid appointmentId: $appointmentId"))
    }
}
