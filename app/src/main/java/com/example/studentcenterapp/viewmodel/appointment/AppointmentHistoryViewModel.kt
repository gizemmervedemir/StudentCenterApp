package com.example.studentcenterapp.viewmodel.appointment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentcenterapp.model.Appointment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * NOTE:
 * Bu ViewModel, "past appointments" filtresini status üzerinden yapar.
 * MVP için: pending => geçmiş değildir (history'de görünmez).
 */
class AppointmentHistoryViewModel(
    private val appointmentRepository: AppointmentRepository
) : ViewModel() {

    data class UiState(
        val isLoading: Boolean = false,
        val appointments: List<Appointment> = emptyList(),
        val errorMessage: String? = null
    )

    private val _uiState = MutableStateFlow(UiState(isLoading = true))
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadHistory()
    }

    fun loadHistory() {
        viewModelScope.launch {
            appointmentRepository.getAppointments()
                .onStart {
                    _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                }
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            appointments = emptyList(),
                            errorMessage = e.message ?: "Unexpected error"
                        )
                    }
                }
                .collect { allAppointments ->
                    val history = allAppointments.filter { it.isPastForHistory() }

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            appointments = history,
                            errorMessage = null
                        )
                    }
                }
        }
    }

    /**
     * "Past" filtre:
     * - pending => değil
     * - approved/cancelled/completed => history
     *
     * Eğer ileride "timeSlot date/time" ile gerçek zaman filtresi istenecekse
     * burayı TimeSlot verisiyle değiştireceğiz.
     */
    private fun Appointment.isPastForHistory(): Boolean {
        val s = status.trim().lowercase()
        return s != "pending"
    }
}

interface AppointmentRepository {
    fun getAppointments(): kotlinx.coroutines.flow.Flow<List<Appointment>>
}