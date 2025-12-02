package com.example.studentcenterapp.appointment.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentcenterapp.appointment.domain.Appointment
import com.example.studentcenterapp.appointment.domain.AppointmentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppointmentListViewModel(
    private val repository: AppointmentRepository
) : ViewModel() {

    private val _appointments = MutableStateFlow<List<Appointment>>(emptyList())
    val appointments = _appointments.asStateFlow()

    fun load(studentId: String) {
        viewModelScope.launch {
            repository.getAppointmentsForStudent(studentId)
                .collect { _appointments.value = it }
        }
    }

    fun cancel(id: String) {
        viewModelScope.launch {
            repository.cancelAppointment(id)
        }
    }
}
