package com.example.studentcenterapp.data.appointment

import com.example.studentcenterapp.model.Appointment
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreAppointmentDataSource {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("appointments")

    /**
     * ✅ YENİ: Randevuyu Firestore'a kaydeder.
     * ViewModel'dan gelen Appointment nesnesini direkt döküman olarak yazar.
     */
    suspend fun addAppointment(appointment: Appointment): Result<Unit> {
        return try {
            collection
                .document(appointment.id) // Kendi ürettiğimiz UUID'yi doküman ID'si yapıyoruz
                .set(appointment)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Öğrencinin randevularını canlı dinle
     */
    fun observeAppointmentsForStudent(studentId: String): Flow<List<Appointment>> = callbackFlow {
        val listener = collection
            .whereEqualTo("studentId", studentId)
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

    /**
     * Personelin randevularını canlı dinle
     */
    fun observeAppointmentsForStaff(staffId: String): Flow<List<Appointment>> = callbackFlow {
        val listener = collection
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

    /**
     * Durum güncelleme (Onayla/İptal)
     */
    suspend fun updateStatus(appointmentId: String, newStatus: String): Boolean {
        return try {
            collection.document(appointmentId)
                .update("status", newStatus)
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }
}