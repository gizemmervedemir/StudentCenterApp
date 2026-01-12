package com.example.studentcenterapp.data.appointment

import com.example.studentcenterapp.model.Appointment
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

class AppointmentRepositoryImpl(
    private val firestoreDataSource: FirestoreAppointmentDataSource
) : AppointmentRepository {

    private val db = FirebaseFirestore.getInstance()

    override fun getAppointmentsForStudent(studentId: String): Flow<List<Appointment>> {
        return firestoreDataSource.observeAppointmentsForStudent(studentId)
    }

    override fun getAppointmentById(appointmentId: String): Flow<Appointment?> = flow {
        try {
            val snapshot = db.collection("appointments").document(appointmentId).get().await()
            emit(snapshot.toObject(Appointment::class.java))
        } catch (e: Exception) {
            emit(null)
        }
    }

    override suspend fun createAppointment(
        studentId: String,
        serviceId: String,
        timeSlotId: String,
        scheduledStartMillis: Long
    ): Result<Unit> {
        return try {
            val id = db.collection("appointments").document().id

            // Millis bilgisini senin modelindeki String date formatına çevirelim
            val sdf = SimpleDateFormat("dd MMMM, Eeee", Locale("tr"))
            val formattedDate = sdf.format(Date(scheduledStartMillis))

            val appointment = Appointment(
                id = id,
                studentId = studentId,
                staffId = "", // Başlangıçta boş, personel atanınca dolacak
                serviceId = serviceId,
                timeSlotId = timeSlotId,
                appointmentDate = formattedDate, // Senin modelindeki alan
                status = "pending"
            )

            db.collection("appointments").document(id).set(appointment).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun cancelAppointment(id: String): Result<Unit> {
        val success = firestoreDataSource.updateStatus(id, "cancelled")
        return if (success) Result.success(Unit) else Result.failure(Exception("İptal edilemedi"))
    }
}