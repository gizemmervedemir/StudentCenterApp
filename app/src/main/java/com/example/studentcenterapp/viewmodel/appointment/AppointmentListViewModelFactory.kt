package com.example.studentcenterapp.viewmodel.appointment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.studentcenterapp.data.appointment.AppointmentRepository

class AppointmentListViewModelFactory(
    private val studentId: String,
    private val appointmentRepository: AppointmentRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppointmentListViewModel::class.java)) {
            return AppointmentListViewModel(
                studentId = studentId,
                appointmentRepository = appointmentRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
