package com.example.studentcenterapp.ui.confirmation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.studentcenterapp.data.appointment.AppointmentRepository

class AppointmentConfirmationViewModelFactory(
    private val appointmentRepository: AppointmentRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppointmentConfirmationViewModel::class.java)) {
            return AppointmentConfirmationViewModel(appointmentRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
