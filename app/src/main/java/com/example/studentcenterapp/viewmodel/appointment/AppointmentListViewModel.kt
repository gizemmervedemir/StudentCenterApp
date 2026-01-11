package com.example.studentcenterapp.viewmodel.appointment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentcenterapp.data.appointment.AppointmentRecord
import com.example.studentcenterapp.data.appointment.AppointmentRepository
import com.example.studentcenterapp.ui.appointments.AppointmentListFilter
import com.example.studentcenterapp.ui.appointments.AppointmentListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppointmentListViewModel(
    private val studentId: String,
    private val appointmentRepository: AppointmentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppointmentListUiState(isLoading = true))
    val uiState: StateFlow<AppointmentListUiState> = _uiState.asStateFlow()

    init {
        observeAppointments()
    }

    private fun observeAppointments() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            appointmentRepository
                .getAppointmentsForStudent(studentId)
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.message ?: "Randevular yüklenemedi."
                        )
                    }
                }
                .collectLatest { list: List<AppointmentRecord> ->
                    // ✅ Time alanı yok → UPCOMING/PAST ayrımı status ile
                    val upcoming = list
                        .filter { it.status.isUpcomingStatus() }
                        .sortedBy { it.id } // stabil sıralama (time yok)

                    val past = list
                        .filter { it.status.isPastStatus() }
                        .sortedByDescending { it.id }

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = null,
                            upcomingAppointments = upcoming,
                            pastAppointments = past
                        )
                    }
                }
        }
    }

    fun setFilter(filter: AppointmentListFilter) {
        _uiState.update { it.copy(selectedFilter = filter) }
    }

    fun retry() {
        observeAppointments()
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}

// -------------------------
// status helpers
// -------------------------
private fun String.isUpcomingStatus(): Boolean {
    val s = lowercase()
    return s == "pending" || s == "approved"
}

private fun String.isPastStatus(): Boolean {
    val s = lowercase()
    return s == "cancelled"
}
