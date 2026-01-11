package com.example.studentcenterapp.data.appointment

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class InMemoryAppointmentDataSource {

    private val _appointments = MutableStateFlow<List<AppointmentRecord>>(emptyList())
    val appointmentsFlow: StateFlow<List<AppointmentRecord>> = _appointments.asStateFlow()

    /**
     * Repo bu flow'u kullanarak listeleri ve detail'i besler.
     */
    fun getAppointments(): StateFlow<List<AppointmentRecord>> = appointmentsFlow

    /**
     * ✅ Create — Confirm ekranı vs. için Result döndürüyoruz.
     * (İstersen Result<Unit> yerine Result<AppointmentRecord> da yapabilirsin ama şimdilik Unit uyumlu.)
     */
    suspend fun createAppointment(
        studentId: String,
        serviceId: String,
        timeSlotId: String,
        scheduledStartMillis: Long
    ): Result<Unit> {
        val newItem = AppointmentRecord(
            id = UUID.randomUUID().toString(),
            studentId = studentId,
            serviceId = serviceId,
            timeSlotId = timeSlotId,
            scheduledStartMillis = scheduledStartMillis,
            status = "PENDING"
        )

        _appointments.value = _appointments.value + newItem
        return Result.success(Unit)
    }

    /**
     * ✅ Cancel — appointment'ı silmeyelim; status'u CANCELLED yapalım.
     * Böylece:
     * - detail ekranında status güncellenir
     * - listede de iptal olduğu görünür
     */
    suspend fun cancelAppointment(appointmentId: String): Result<Unit> {
        val current = _appointments.value
        val index = current.indexOfFirst { it.id == appointmentId }

        if (index == -1) {
            return Result.failure(IllegalArgumentException("Appointment not found: $appointmentId"))
        }

        val item = current[index]
        val updated = item.copy(status = "CANCELLED")

        val newList = current.toMutableList()
        newList[index] = updated
        _appointments.value = newList

        return Result.success(Unit)
    }

    /**
     * (Opsiyonel) Eğer gerçekten silmek istersen kullanırsın.
     * Ama issue mantığında cancel = status update daha doğru.
     */
    fun remove(appointmentId: String) {
        _appointments.value = _appointments.value.filterNot { it.id == appointmentId }
    }
}