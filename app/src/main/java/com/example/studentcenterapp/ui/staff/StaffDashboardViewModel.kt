package com.example.studentcenterapp.ui.staff

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentcenterapp.data.staff.StaffRepository
import com.example.studentcenterapp.model.Appointment
import com.example.studentcenterapp.ui.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StaffDashboardViewModel(
    private val staffId: String,
    private val repository: StaffRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Appointment>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Appointment>>> = _uiState.asStateFlow()

    private val _actionError = MutableStateFlow<String?>(null)
    val actionError: StateFlow<String?> = _actionError.asStateFlow()

    private val _actionLoading = MutableStateFlow(false)
    val actionLoading: StateFlow<Boolean> = _actionLoading.asStateFlow()

    init {
        observePending()
    }

    private fun observePending() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                repository.getPendingAppointmentsForStaff(staffId).collect { list ->
                    _uiState.value = UiState.Success(list)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Failed to load pending appointments")
            }
        }
    }

    fun approve(appointmentId: String) = update(appointmentId, approve = true)
    fun reject(appointmentId: String) = update(appointmentId, approve = false)

    private fun update(appointmentId: String, approve: Boolean) {
        viewModelScope.launch {
            _actionError.value = null
            _actionLoading.value = true

            val result = if (approve) {
                repository.approveAppointment(appointmentId)
            } else {
                repository.rejectAppointment(appointmentId)
            }

            _actionLoading.value = false
            if (result.isFailure) {
                _actionError.value = result.exceptionOrNull()?.message ?: "Action failed"
            }
        }
    }
}
