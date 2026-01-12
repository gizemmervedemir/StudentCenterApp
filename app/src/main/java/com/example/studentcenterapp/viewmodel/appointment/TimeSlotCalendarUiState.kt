package com.example.studentcenterapp.viewmodel.appointment

import com.example.studentcenterapp.model.TimeSlot
import java.time.LocalDate

data class TimeSlotCalendarUiState(
    val isLoading: Boolean = false,
    val groupedSlots: Map<LocalDate, List<TimeSlot>> = emptyMap(),
    val selectedDate: LocalDate? = null,
    val selectedSlotId: String? = null, // Seçilen saatin ID'si
    val errorMessage: String? = null
)