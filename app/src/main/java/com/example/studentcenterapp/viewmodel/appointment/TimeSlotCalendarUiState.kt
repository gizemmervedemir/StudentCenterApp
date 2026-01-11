package com.example.studentcenterapp.ui.timeslot

import com.example.studentcenterapp.model.TimeSlot
import java.time.LocalDate

data class TimeSlotCalendarUiState(
    val isLoading: Boolean = false,
    val groupedSlots: Map<LocalDate, List<TimeSlot>> = emptyMap(),
    val selectedDate: LocalDate? = null,
    val errorMessage: String? = null
)