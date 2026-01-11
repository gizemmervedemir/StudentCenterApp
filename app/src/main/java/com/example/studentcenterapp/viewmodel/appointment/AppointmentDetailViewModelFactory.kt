package com.example.studentcenterapp.viewmodel.appointment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.studentcenterapp.data.appointment.AppointmentRepository

class AppointmentDetailViewModelFactory(
    private val appointmentId: String,
    private val repository: AppointmentRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppointmentDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AppointmentDetailViewModel(
                appointmentId = appointmentId,
                repository = repository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}