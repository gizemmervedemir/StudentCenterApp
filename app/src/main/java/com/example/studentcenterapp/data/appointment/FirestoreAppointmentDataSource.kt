package com.example.studentcenterapp.data.appointment

import com.example.studentcenterapp.model.Appointment
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreAppointmentDataSource {
    private val db = FirebaseFirestore.getInstance()

    // Personelin randevularını canlı dinle
    fun observeAppointmentsForStaff(staffId: String): Flow<List<Appointment>> = callbackFlow {
        val listener = db.collection("appointments")
            .whereEqualTo("staffId", staffId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val items = snapshot?.toObjects(Appointment::class.java) ?: emptyList()
                trySend(items)
            }
        awaitClose { listener.remove() }
    }
    // FirestoreAppointmentDataSource.kt içine ekle:
    // FirestoreAppointmentDataSource.kt içindeki ilgili kısımlar:

    fun observeAppointmentsForStudent(studentId: String): Flow<List<Appointment>> = callbackFlow {
        val listener = db.collection("appointments")
            .whereEqualTo("studentId", studentId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) { close(error); return@addSnapshotListener }

                // AppointmentRecord::class.java yerine Appointment::class.java yazdık
                val items = snapshot?.toObjects(Appointment::class.java) ?: emptyList()
                trySend(items)
            }
        awaitClose { listener.remove() }
    }
    // Durum güncelleme (Onayla/İptal)
    suspend fun updateStatus(appointmentId: String, newStatus: String): Boolean {
        return try {
            db.collection("appointments").document(appointmentId)
                .update("status", newStatus)
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }
}