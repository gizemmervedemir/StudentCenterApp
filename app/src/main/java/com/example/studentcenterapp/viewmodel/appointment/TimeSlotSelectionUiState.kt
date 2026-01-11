package com.example.studentcenterapp.ui.timeslot

import com.example.studentcenterapp.model.TimeSlot

data class TimeSlotSelectionUiState(
    val isLoading: Boolean = false,
    val slots: List<TimeSlot> = emptyList(),
    val errorMessage: String? = null,
    val selectedSlotId: String? = null
)