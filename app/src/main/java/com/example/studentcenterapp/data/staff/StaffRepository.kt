package com.example.studentcenterapp.data.staff

import com.example.studentcenterapp.model.Appointment
import com.example.studentcenterapp.model.Staff
import kotlinx.coroutines.flow.Flow

interface StaffRepository {
    fun getPendingAppointmentsForStaff(staffId: String): Flow<List<Appointment>>
    suspend fun approveAppointment(appointmentId: String): Result<Unit>
    suspend fun rejectAppointment(appointmentId: String): Result<Unit>

    suspend fun getStaffById(id: String): Staff?
    suspend fun updateStaffProfile(id: String, name: String): Result<Unit>
}
