package com.example.studentcenterapp.appointment.ui.confirmation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentcenterapp.appointment.domain.AppointmentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ConfirmationState {
    object Idle : ConfirmationState()
    object Loading : ConfirmationState()
    data class Success(val id: String) : ConfirmationState()
    data class Error(val message: String) : ConfirmationState()
}

class AppointmentConfirmationViewModel(
    private val repository: AppointmentRepository
) : ViewModel() {

    private val _state = MutableStateFlow<ConfirmationState>(ConfirmationState.Idle)
    val state = _state.asStateFlow()

    fun create(studentId: String, serviceId: String, staffId: String?, timeSlotId: String) {
        viewModelScope.launch {
            _state.value = ConfirmationState.Loading

            val result = repository.createAppointment(studentId, serviceId, staffId, timeSlotId)

            result.fold(
                onSuccess = { _state.value = ConfirmationState.Success(it.id) },
                onFailure = { _state.value = ConfirmationState.Error(it.message ?: "Unknown error") }
            )
        }
    }
}
