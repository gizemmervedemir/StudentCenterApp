package com.example.studentcenterapp.ui.timeslot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentcenterapp.model.Appointment
import com.example.studentcenterapp.model.AppointmentStatus
import kotlinx.coroutines.launch

class AppointmentHistoryViewModel(
    private val appointmentRepository: AppointmentRepository
) : ViewModel() {

    private val _uiState =
        kotlinx.coroutines.flow.MutableStateFlow(
            AppointmentHistoryUiState(isLoading = true)
        )
    val uiState: kotlinx.coroutines.flow.StateFlow<AppointmentHistoryUiState> =
        _uiState

    init {
        loadHistory()
    }

    fun loadHistory() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                // Current student filtresi repository içinde yapılacak
                val allAppointments =
                    appointmentRepository.getAppointmentsForCurrentStudent()

                val pastAppointments = filterPastAppointments(allAppointments)

                _uiState.value = AppointmentHistoryUiState(
                    isLoading = false,
                    appointments = pastAppointments,
                    errorMessage = null
                )
            } catch (t: Throwable) {
                _uiState.value = AppointmentHistoryUiState(
                    isLoading = false,
                    appointments = emptyList(),
                    errorMessage = t.message ?: "Failed to load appointment history"
                )
            }
        }
    }

    private fun filterPastAppointments(
        all: List<Appointment>
    ): List<Appointment> {
        // Şimdilik COMPLETED olanları "geçmiş" sayıyoruz.
        // TODO: Tarih alanı netleşince gerçek tarih bazlı filtre eklenecek.
        return all.filter { it.status == AppointmentStatus.COMPLETED }
    }
}

/**
 * !!! DİKKAT !!!
 * Bunlar GEÇİCİ STUB interface'ler.
 * Gerçek implementation ve asıl interface'leri
 * Selimcan + Eylül kendi paketlerinde yazacak.
 */
interface AppointmentRepository {
    /**
     * Current öğrenci için tüm randevuları döner.
     * Gerçek implementasyonu Selimcan yazacak.
     */
    suspend fun getAppointmentsForCurrentStudent(): List<Appointment>
}