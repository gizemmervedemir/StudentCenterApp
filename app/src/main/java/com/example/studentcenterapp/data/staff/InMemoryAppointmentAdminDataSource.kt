package com.example.studentcenterapp.data.staff

import com.example.studentcenterapp.model.Appointment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class InMemoryAppointmentAdminDataSource : AppointmentAdminDataSource {

    private val _appointments = MutableStateFlow(
        listOf(
            Appointment(
                id = "a1",
                studentId = "s1",
                serviceId = "srv1",
                timeSlotId = "t1",
                status = STATUS_PENDING
            ),
            Appointment(
                id = "a2",
                studentId = "s2",
                serviceId = "srv2",
                timeSlotId = "t2",
                status = STATUS_PENDING
            ),
            Appointment(
                id = "a3",
                studentId = "s3",
                serviceId = "srv3",
                timeSlotId = "t3",
                status = STATUS_APPROVED
            )
        )
    )

    // Appointment modelinde staffId yok → staff assignment’ı burada tutuyoruz.
    // TODO: shared InMemoryDataSource / AppointmentRepository ile birleşecek (Şahan + Selimcan)
    private val assignment: Map<String, String> = mapOf(
        "a1" to "staff1",
        "a2" to "staff1",
        "a3" to "staff2"
    )

    override fun observeAppointments(): Flow<List<Appointment>> = _appointments.asStateFlow()

    override fun getAssignedStaffId(appointmentId: String): String? = assignment[appointmentId]

    override suspend fun updateStatus(appointmentId: String, newStatus: String): Boolean {
        val current = _appointments.value
        if (current.none { it.id == appointmentId }) return false

        _appointments.value = current.map { appt ->
            if (appt.id == appointmentId) appt.copy(status = newStatus) else appt
        }
        return true
    }

    companion object {
        const val STATUS_PENDING = "pending"
        const val STATUS_APPROVED = "approved"
        const val STATUS_CANCELLED = "cancelled" // reject için bunu kullanıyoruz
    }
}
