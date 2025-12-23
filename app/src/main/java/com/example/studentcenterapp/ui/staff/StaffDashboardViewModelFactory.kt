package com.example.studentcenterapp.ui.staff

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.studentcenterapp.data.staff.StaffRepository

class StaffDashboardViewModelFactory(
    private val staffId: String,
    private val repository: StaffRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StaffDashboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StaffDashboardViewModel(staffId, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
