package com.example.studentcenterapp.data.appointment

import com.example.studentcenterapp.model.Appointment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*

class InMemoryAppointmentDataSource {

    private val _appointments = MutableStateFlow<List<Appointment>>(emptyList())
    val appointmentsFlow: StateFlow<List<Appointment>> = _appointments.asStateFlow()

    fun getAppointments(): StateFlow<List<Appointment>> = appointmentsFlow

    /**
     * ✅ Create — Yeni randevuyu senin Appointment modeline göre oluşturur.
     */
    suspend fun createAppointment(
        studentId: String,
        serviceId: String,
        timeSlotId: String,
        scheduledStartMillis: Long
    ): Result<Unit> {
        // Millis değerini modelindeki String 'appointmentDate' formatına çeviriyoruz
        val sdf = SimpleDateFormat("dd MMMM, Eeee", Locale("tr"))
        val formattedDate = sdf.format(Date(scheduledStartMillis))

        val newItem = Appointment(
            id = UUID.randomUUID().toString(),
            studentId = studentId,
            staffId = "", // Başlangıçta boş
            serviceId = serviceId,
            timeSlotId = timeSlotId,
            appointmentDate = formattedDate, // Senin modelindeki alan
            status = "pending",
            type = "office"
        )

        _appointments.value = _appointments.value + newItem
        return Result.success(Unit)
    }

    /**
     * ✅ Cancel — Durumu "cancelled" olarak günceller.
     */
    suspend fun updateStatus(appointmentId: String, newStatus: String): Boolean {
        val current = _appointments.value
        val index = current.indexOfFirst { it.id == appointmentId }

        if (index == -1) return false

        val item = current[index]
        val updated = item.copy(status = newStatus)

        val newList = current.toMutableList()
        newList[index] = updated
        _appointments.value = newList

        return true
    }

    // Geriye dönük uyumluluk için cancelAppointment
    suspend fun cancelAppointment(appointmentId: String): Result<Unit> {
        val success = updateStatus(appointmentId, "cancelled")
        return if (success) Result.success(Unit)
        else Result.failure(IllegalArgumentException("Appointment not found"))
    }

    fun remove(appointmentId: String) {
        _appointments.value = _appointments.value.filterNot { it.id == appointmentId }
    }
}