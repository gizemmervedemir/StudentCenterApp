package com.example.studentcenterapp.viewmodel.appointment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentcenterapp.data.timeslot.TimeSlotRepository
import com.example.studentcenterapp.model.TimeSlot
import com.example.studentcenterapp.ui.timeslot.TimeSlotSelectionUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TimeSlotSelectionViewModel(
    private val repository: TimeSlotRepository,
    private val serviceId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        TimeSlotSelectionUiState(isLoading = true)
    )
    val uiState: StateFlow<TimeSlotSelectionUiState> = _uiState.asStateFlow()

    init {
        loadSlots()
    }

    fun loadSlots() {
        viewModelScope.launch {
            repository.getAvailableSlots(serviceId)
                .onStart {
                    _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                }
                .catch { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            slots = emptyList(),
                            errorMessage = throwable.message ?: "Unexpected error"
                        )
                    }
                }
                .collect { slots ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            slots = slots,
                            errorMessage = null
                        )
                    }
                }
        }
    }

    fun onSlotClicked(slot: TimeSlot) {
        _uiState.update { it.copy(selectedSlotId = slot.id) }
    }

    /**
     * Confirmation flow'da çağrılacak.
     * Slot rezerve edilirse true döner.
     */
    suspend fun confirmSelectedSlot(): Boolean {
        val id = _uiState.value.selectedSlotId ?: return false
        return repository.reserveSlot(id)
    }
}