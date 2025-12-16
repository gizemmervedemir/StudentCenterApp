package com.example.studentcenterapp.ui.timeslot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.studentcenterapp.data.timeslot.TimeSlotRepository

class TimeSlotSelectionViewModelFactory(
    private val repository: TimeSlotRepository,
    private val serviceId: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TimeSlotSelectionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TimeSlotSelectionViewModel(
                repository = repository,
                serviceId = serviceId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}