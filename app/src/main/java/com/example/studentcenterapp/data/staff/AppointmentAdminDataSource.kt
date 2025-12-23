package com.example.studentcenterapp.data.staff

import com.example.studentcenterapp.model.Appointment
import kotlinx.coroutines.flow.Flow

interface AppointmentAdminDataSource {
    fun observeAppointments(): Flow<List<Appointment>>
    fun getAssignedStaffId(appointmentId: String): String?
    suspend fun updateStatus(appointmentId: String, newStatus: String): Boolean
}
