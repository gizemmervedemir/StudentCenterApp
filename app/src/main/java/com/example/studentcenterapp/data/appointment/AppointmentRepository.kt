package com.example.studentcenterapp.data.appointment

import com.example.studentcenterapp.model.Appointment // Senin asıl modelini import ediyoruz
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

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

    suspend fun cancelAppointment(appointmentId: String): Result<Unit> {
        return try {
            val db = FirebaseFirestore.getInstance()
            db.collection("appointments")
                .document(appointmentId)
                .update("status", "cancelled") // Veriyi silmiyoruz, durumunu güncelliyoruz
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
