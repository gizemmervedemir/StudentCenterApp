package com.example.studentcenterapp.viewmodel.appointment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentcenterapp.data.appointment.AppointmentRecord
import com.example.studentcenterapp.data.appointment.AppointmentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AppointmentDetailUiState(
    val isLoading: Boolean = true,
    val appointment: AppointmentRecord? = null,
    val errorMessage: String? = null
)

class AppointmentDetailViewModel(
    private val appointmentId: String,
    private val repository: AppointmentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppointmentDetailUiState())
    val uiState: StateFlow<AppointmentDetailUiState> = _uiState.asStateFlow()

    init {
        observeAppointment()
    }

    private fun observeAppointment() {
        viewModelScope.launch {
            _uiState.value = AppointmentDetailUiState(isLoading = true)

            // Repo'da "getById" yoksa: demo amaçlı listeden çekiyoruz (en stabil yöntem)
            // Not: Senin repo şimdilik getAppointmentsForStudent üzerinden akıyor.
            // Eğer sende dataSource.getById() eklediysen, burada daha temiz hale getiririz.
            // Şimdilik simple: öğrenci id bilinmiyorsa "all" gibi bir akış yok -> demo yaklaşımı:
            _uiState.value = AppointmentDetailUiState(
                isLoading = false,
                appointment = null,
                errorMessage = "Appointment lookup requires an ID-based source. Add getById() to data source or repository."
            )
        }
    }

    /**
     * ✅ Cancel: repository status'u CANCELLED yapmalı (silmemeli)
     */
    fun cancelAppointment() {
        viewModelScope.launch {
            val result = repository.cancelAppointment(appointmentId)
            result.onFailure { e ->
                _uiState.value = _uiState.value.copy(errorMessage = e.message ?: "Cancel failed")
            }
            // başarılıysa: datasource flow update edeceği için UI otomatik güncellenir (getById akışı varsa)
        }
    }
}