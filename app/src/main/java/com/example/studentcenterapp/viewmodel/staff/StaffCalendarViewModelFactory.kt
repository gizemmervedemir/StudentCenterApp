package com.example.studentcenterapp.viewmodel.staff

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.studentcenterapp.data.appointment.AppointmentRepository

class StaffCalendarViewModelFactory(
    private val staffId: String,
    private val repository: AppointmentRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StaffCalendarViewModel::class.java)) {
            return StaffCalendarViewModel(staffId, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}