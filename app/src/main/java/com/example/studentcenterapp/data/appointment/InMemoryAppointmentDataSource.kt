package com.example.studentcenterapp.data.appointment

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class InMemoryAppointmentDataSource {
    private val _appointments = MutableStateFlow<List<AppointmentRecord>>(emptyList())
    val appointmentsFlow = _appointments.asStateFlow()

    fun add(
        studentId: String,
        serviceId: String,
        timeSlotId: String,
        scheduledStartMillis: Long
    ) {
        val newItem = AppointmentRecord(
            id = UUID.randomUUID().toString(),
            studentId = studentId,
            serviceId = serviceId,
            timeSlotId = timeSlotId,
            scheduledStartMillis = scheduledStartMillis,
            status = "PENDING"
        )

        _appointments.value = _appointments.value + newItem
    }

    fun remove(appointmentId: String) {
        _appointments.value = _appointments.value.filterNot { it.id == appointmentId }
    }
}
