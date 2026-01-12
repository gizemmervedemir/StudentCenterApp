package com.example.studentcenterapp.data.staff

import com.example.studentcenterapp.data.appointment.FirestoreAppointmentDataSource
import com.example.studentcenterapp.model.Appointment
import kotlinx.coroutines.flow.Flow

class StaffRepositoryImpl(
    private val firestoreDataSource: FirestoreAppointmentDataSource
) : StaffRepository {

    override fun getPendingAppointmentsForStaff(staffId: String): Flow<List<Appointment>> {
        // Artık direkt Firestore'dan canlı akışı alıyoruz
        return firestoreDataSource.observeAppointmentsForStaff(staffId)
    }

    override suspend fun approveAppointment(appointmentId: String): Result<Unit> {
        val success = firestoreDataSource.updateStatus(appointmentId, "approved")
        return if (success) Result.success(Unit)
        else Result.failure(Exception("Onaylama işlemi başarısız oldu."))
    }

    override suspend fun rejectAppointment(appointmentId: String): Result<Unit> {
        val success = firestoreDataSource.updateStatus(appointmentId, "cancelled")
        return if (success) Result.success(Unit)
        else Result.failure(Exception("Reddetme işlemi başarısız oldu."))
    }
}