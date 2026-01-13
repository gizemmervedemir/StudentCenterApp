package com.example.studentcenterapp.data.staff

import com.example.studentcenterapp.data.appointment.FirestoreAppointmentDataSource
import com.example.studentcenterapp.model.Appointment
import com.example.studentcenterapp.model.Staff
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class StaffRepositoryImpl(
    private val firestoreDataSource: FirestoreAppointmentDataSource,
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : StaffRepository {

    // Departman bazlı randevuları DataSource'dan al
    override fun getAppointmentsByDepartment(departmentId: String): Flow<List<Appointment>> {
        return firestoreDataSource.observeAppointmentsByDepartment(departmentId)
    }

    // Personelin kendisine atananları DataSource'dan al
    override fun getPendingAppointmentsForStaff(staffId: String): Flow<List<Appointment>> {
        return firestoreDataSource.observeAppointmentsForStudent(staffId) // Mevcut metodun adını buna göre kullanabilirsin
    }

    // Onaylama: Hem durumu değiştirir hem staffId atar
    override suspend fun approveAppointment(appointmentId: String, staffId: String): Result<Unit> {
        val success = firestoreDataSource.updateAppointmentStatusWithStaff(
            appointmentId = appointmentId,
            newStatus = "approved",
            staffId = staffId
        )
        return if (success) Result.success(Unit)
        else Result.failure(Exception("Onaylama işlemi başarısız oldu."))
    }

    // Reddetme
    override suspend fun rejectAppointment(appointmentId: String): Result<Unit> {
        val success = firestoreDataSource.updateStatus(appointmentId, "cancelled")
        return if (success) Result.success(Unit)
        else Result.failure(Exception("Reddetme işlemi başarısız oldu."))
    }

    // Profil Getirme
    override suspend fun getStaffById(id: String): Staff? {
        return try {
            val doc = firestore.collection("users").document(id).get().await()
            if (!doc.exists()) return null

            Staff(
                id = id,
                name = doc.getString("fullName") ?: doc.getString("name") ?: "",
                email = doc.getString("email") ?: "", // Artık modelde var, hata vermez
                role = doc.getString("role") ?: "staff",
                departmentId = doc.getString("departmentId") ?: "",
                profilePictureUrl = doc.getString("profilePictureUrl")
            )
        } catch (e: Exception) {
            null
        }
    }

    // Profil Güncelleme
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