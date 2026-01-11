package com.example.studentcenterapp.viewmodel.appointment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.studentcenterapp.model.Appointment

class AppointmentCardViewModelFactory(
    private val appointment: Appointment,
    private val departmentName: String,
    private val serviceName: String,
    private val dateTimeText: String
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppointmentCardViewModel::class.java)) {
            return AppointmentCardViewModel(
                appointment = appointment,
                departmentName = departmentName,
                serviceName = serviceName,
                dateTimeText = dateTimeText
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
