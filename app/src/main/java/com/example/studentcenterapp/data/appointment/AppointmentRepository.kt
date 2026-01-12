package com.example.studentcenterapp.data.appointment

import com.example.studentcenterapp.model.Appointment // Senin asıl modelini import ediyoruz
import kotlinx.coroutines.flow.Flow

interface AppointmentRepository {
    // Tüm dönüş tiplerini Appointment olarak güncelledik
    fun getAppointmentsForStudent(studentId: String): Flow<List<Appointment>>

    fun getAppointmentById(appointmentId: String): Flow<Appointment?>

    suspend fun createAppointment(
        studentId: String,
        serviceId: String,
        timeSlotId: String,
        scheduledStartMillis: Long
    ): Result<Unit>

    suspend fun cancelAppointment(appointmentId: String): Result<Unit>
}
