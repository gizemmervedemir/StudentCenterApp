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

    suspend fun addAppointment(appointment: Appointment): Result<Unit> {
        return try {
            collection.document(appointment.id).set(appointment).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Departman bazlı canlı dinleme
    fun observeAppointmentsByDepartment(departmentId: String): Flow<List<Appointment>> = callbackFlow {
        val listener = collection
            .whereEqualTo("departmentId", departmentId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) { close(error); return@addSnapshotListener }
                val items = snapshot?.toObjects(Appointment::class.java) ?: emptyList()
                trySend(items)
            }
        awaitClose { listener.remove() }
    }

    // Parametre ismini studentUid yapıyoruz ki karmaşa olmasın
    fun observeAppointmentsForStudent(studentUid: String): Flow<List<Appointment>> = callbackFlow {
        val listener = collection
            .whereEqualTo("studentUid", studentUid) // Artık buradaki studentUid yukarıdakiyle eşleşiyor
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

    // Onaylama sırasında staffId işleme
    suspend fun updateAppointmentStatusWithStaff(
        appointmentId: String,
        newStatus: String,
        staffId: String
    ): Boolean {
        return try {
            collection.document(appointmentId)
                .update(
                    "status", newStatus,
                    "staffId", staffId
                ).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun updateStatus(appointmentId: String, newStatus: String): Boolean {
        return try {
            collection.document(appointmentId).update("status", newStatus).await()
            true
        } catch (e: Exception) {
            false
        }
    }
}