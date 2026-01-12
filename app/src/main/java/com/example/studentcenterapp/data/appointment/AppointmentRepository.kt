package com.example.studentcenterapp.data.appointment

import com.example.studentcenterapp.model.Appointment
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

interface AppointmentRepository {

    // Öğrenciye ait randevuları getirir
    fun getAppointmentsForStudent(studentId: String): Flow<List<Appointment>>

    // ID'ye göre tek bir randevu getirir
    fun getAppointmentById(appointmentId: String): Flow<Appointment?>

    // ✅ Güncellendi: Artık direkt Appointment nesnesini alır
    suspend fun createAppointment(appointment: Appointment): Result<Unit>

    // Randevuyu iptal eder
    suspend fun cancelAppointment(appointmentId: String): Result<Unit> {
        return try {
            val db = FirebaseFirestore.getInstance()
            db.collection("appointments")
                .document(appointmentId)
                .update("status", "cancelled")
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}