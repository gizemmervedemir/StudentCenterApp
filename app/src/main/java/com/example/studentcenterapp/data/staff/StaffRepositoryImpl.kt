package com.example.studentcenterapp.data.staff

import com.example.studentcenterapp.data.appointment.FirestoreAppointmentDataSource
import com.example.studentcenterapp.model.Appointment
import com.example.studentcenterapp.model.Staff
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class StaffRepositoryImpl(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()) : StaffRepository {

    override fun getPendingAppointmentsForStaff(staffId: String) = TODO("Implement")

    override suspend fun approveAppointment(appointmentId: String) = TODO("Implement")


    override suspend fun rejectAppointment(appointmentId: String) = TODO("Implement")


    override suspend fun getStaffById(id: String): Staff? {
        return try {
            val doc = firestore.collection("users").document(id).get().await()
            if (!doc.exists()) return null

            // Eğer staff mı öğrenci mi ayıran alanın varsa (ör: role, userType, isStaff)
            // burada kontrol edebilirsin. Yoksa şimdilik sadece Staff mapleyelim.
            Staff(
                id = id,
                name = doc.getString("fullName") ?: "",
                profilePictureUrl = doc.getString("profilePictureUrl"),
                role = doc.getString("role") ?: "staff",
                departmentId = doc.getString("departmentId") ?: ""
            )
        } catch (e: Exception) {
            null
        }
    }
    // StaffRepositoryImpl içinde TODO olan kısmı güncelle:
    override suspend fun updateStaffProfile(id: String, name: String): Result<Unit> {
        return try {
            firestore.collection("users").document(id)
                .update("fullName", name)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}