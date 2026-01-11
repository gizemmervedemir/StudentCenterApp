package com.example.studentcenterapp.viewmodel.appointment

import com.example.studentcenterapp.model.Appointment

data class AppointmentHistoryUiState(
    val isLoading: Boolean = false,
    val appointments: List<Appointment> = emptyList(),
    val errorMessage: String? = null
)