package com.example.studentcenterapp.viewmodel.appointment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentcenterapp.data.timeslot.TimeSlotRepository
import com.example.studentcenterapp.ui.timeslot.TimeSlotCalendarUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class TimeSlotCalendarViewModel(
    private val repository: TimeSlotRepository,
    private val serviceId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        TimeSlotCalendarUiState(isLoading = true)
    )
    val uiState: StateFlow<TimeSlotCalendarUiState> = _uiState.asStateFlow()

    init {
        loadSlots()
    }

    fun loadSlots() {
        viewModelScope.launch {
            repository.getAvailableSlots(serviceId)
                .onStart {
                    _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                }
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.message ?: "Unexpected error"
                        )
                    }
                }
                .collect { slots ->
                    val grouped = slots.groupBy { LocalDate.parse(it.date) }

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            groupedSlots = grouped,
                            selectedDate = grouped.keys.firstOrNull(),
                            errorMessage = null
                        )
                    }
                }
        }
    }

    fun onDateSelected(date: LocalDate) {
        _uiState.update { it.copy(selectedDate = date) }
    }
}