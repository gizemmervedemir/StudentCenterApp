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
     * Yeni randevu oluşturma
     */
    suspend fun addAppointment(appointment: Appointment): Result<Unit> {
        return try {
            collection.document(appointment.id).set(appointment).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Departman bazlı canlı dinleme (Dashboard için)
     */
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

    /**
     * Öğrenci bazlı canlı dinleme
     */
    fun observeAppointmentsForStudent(studentUid: String): Flow<List<Appointment>> = callbackFlow {
        val listener = collection
            .whereEqualTo("studentUid", studentUid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) { close(error); return@addSnapshotListener }
                val items = snapshot?.toObjects(Appointment::class.java) ?: emptyList()
                trySend(items)
            }
        awaitClose { listener.remove() }
    }

    /**
     * Personelin takviminde görünecek randevuları dinleme.
     * Burada 'approved' filtresini kaldırdım çünkü personel hem bekleyenleri
     * hem de onayladıklarını takvimde görebilmeli (senin istediğin UI mantığına göre).
     */
    // FirestoreAppointmentDataSource.kt
    fun observeApprovedAppointments(staffId: String): Flow<List<Appointment>> = callbackFlow {
        val listener = collection
            .whereEqualTo("staffId", staffId)
            // .whereEqualTo("status", "approved")  <-- BU SATIRI SİLDİK/YORUMA ALDIK
            // Böylece hem 'pending' hem 'approved' olanlar akışa (flow) dahil olur.
            .addSnapshotListener { snapshot, error ->
                if (error != null) { close(error); return@addSnapshotListener }
                val items = snapshot?.toObjects(Appointment::class.java) ?: emptyList()
                trySend(items)
            }
        awaitClose { listener.remove() }
    }
    /**
     * Temel Durum Güncelleme
     */
    suspend fun updateStatus(appointmentId: String, newStatus: String): Boolean {
        return try {
            collection.document(appointmentId).update("status", newStatus).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Personel işlemi (Approve/Reject) yaparken staffId'yi de güncelleyen metod
     */
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
}