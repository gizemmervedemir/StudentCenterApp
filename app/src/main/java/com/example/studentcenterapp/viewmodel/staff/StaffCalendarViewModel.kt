package com.example.studentcenterapp.viewmodel.staff

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentcenterapp.data.appointment.AppointmentRepository
import com.example.studentcenterapp.model.Appointment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class StaffCalendarViewModel(
    private val staffId: String,
    private val repository: AppointmentRepository
) : ViewModel() {

    private val _allAppointments = MutableStateFlow<List<Appointment>>(emptyList())

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate = _selectedDate.asStateFlow()

    // Seçili tarihe göre filtrelenmiş liste
    val filteredAppointments = combine(_allAppointments, _selectedDate) { apps, date ->
        apps.filter { it.appointmentDate == date.toString() }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        loadAppointments()
    }

    private fun loadAppointments() {
        viewModelScope.launch {
            repository.observeApprovedAppointments(staffId).collect {
                _allAppointments.value = it
            }
        }
    }

    fun onDateSelected(date: LocalDate) {
        _selectedDate.value = date
    }

    fun approveAppointment(appointment: Appointment) {
        viewModelScope.launch {
            // Repository üzerinden status'ü güncelle
            repository.updateAppointmentStatus(appointment.id, "approved")
        }
    }
    fun rejectAppointment(appointment: Appointment) {
        viewModelScope.launch {
            // Bu işlem randevuyu silmez, Firestore'da status alanını günceller
            repository.updateAppointmentStatus(appointment.id, "rejected")
        }
    }
}