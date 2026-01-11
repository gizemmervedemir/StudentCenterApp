package com.example.studentcenterapp.data.appointment

import kotlinx.coroutines.flow.Flow

interface AppointmentRepository {
    fun getAppointmentsForStudent(studentId: String): Flow<List<AppointmentRecord>>

    // ✅ NEW: Detail ekranı için ID ile tek randevu
    fun getAppointmentById(appointmentId: String): Flow<AppointmentRecord?>


    suspend fun createAppointment(
        studentId: String,
        serviceId: String,
        timeSlotId: String,
        scheduledStartMillis: Long
    ): Result<Unit>

    suspend fun cancelAppointment(appointmentId: String): Result<Unit>
}

/**
 * Domain/model sınıfına dokunmadan ilerlemek için basit record.
 * (İstersen sonra model.Appointment ile değiştirirsin.)
 */
data class AppointmentRecord(
    val id: String,
    val studentId: String,
    val serviceId: String,
    val timeSlotId: String,
    val scheduledStartMillis: Long,
    val status: String
)