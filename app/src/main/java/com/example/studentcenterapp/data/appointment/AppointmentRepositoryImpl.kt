package com.example.studentcenterapp.data.appointment

import com.example.studentcenterapp.model.Appointment
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class AppointmentRepositoryImpl(
    private val firestoreDataSource: FirestoreAppointmentDataSource
) : AppointmentRepository {

    private val db = FirebaseFirestore.getInstance()

    /**
     * Öğrencinin randevularını DataSource üzerinden canlı dinler.
     */
    override fun getAppointmentsForStudent(studentId: String): Flow<List<Appointment>> {
        return firestoreDataSource.observeAppointmentsForStudent(studentId)
    }

    /**
     * Belirli bir randevuyu ID ile Firestore'dan çeker.
     */
    override fun getAppointmentById(appointmentId: String): Flow<Appointment?> = flow {
        try {
            val snapshot = db.collection("appointments").document(appointmentId).get().await()
            emit(snapshot.toObject(Appointment::class.java))
        } catch (e: Exception) {
            emit(null)
        }
    }

    override fun observeApprovedAppointments(staffId: String): Flow<List<Appointment>> {
        return firestoreDataSource.observeApprovedAppointments(staffId)
    }

    override suspend fun createAppointment(appointment: Appointment): Result<Unit> {
        return try {
            // DataSource içindeki addAppointment metodunu çağırıyoruz
            firestoreDataSource.addAppointment(appointment)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateAppointmentStatus(appointmentId: String, newStatus: String) {
        // DataSource içindeki mevcut updateStatus fonksiyonunu kullanıyoruz
        firestoreDataSource.updateStatus(appointmentId, newStatus)
    }

    override suspend fun cancelAppointment(id: String): Result<Unit> {
        val success = firestoreDataSource.updateStatus(id, "cancelled")
        return if (success) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Randevu iptal edilemedi."))
        }
    }
}