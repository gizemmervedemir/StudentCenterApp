package com.example.studentcenterapp.appointment.data

import com.example.studentcenterapp.appointment.domain.Appointment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Simple in-memory data source. Replace with DB/shared source later.
 */
class AppointmentDataSource {

    // holds all appointments globally (simple shared source)
    private val _appointments = MutableStateFlow<List<Appointment>>(emptyList())
    val appointments: StateFlow<List<Appointment>> = _appointments

    fun getAppointmentsState() = appointments

    fun add(appointment: Appointment) {
        _appointments.value = _appointments.value + appointment
    }

    fun update(appointment: Appointment) {
        _appointments.value = _appointments.value.map {
            if (it.id == appointment.id) appointment else it
        }
    }

    fun findById(id: String): Appointment? = _appointments.value.firstOrNull { it.id == id }
}