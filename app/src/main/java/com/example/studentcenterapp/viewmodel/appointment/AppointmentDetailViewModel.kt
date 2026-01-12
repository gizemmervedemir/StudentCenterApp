package com.example.studentcenterapp.viewmodel.appointment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentcenterapp.data.appointment.AppointmentRepository
import com.example.studentcenterapp.model.Appointment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AppointmentDetailUiState(
    val isLoading: Boolean = true,
    val appointment: Appointment? = null,
    val errorMessage: String? = null
)

class AppointmentDetailViewModel(
    private val appointmentId: String,
    private val repository: AppointmentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppointmentDetailUiState())
    val uiState: StateFlow<AppointmentDetailUiState> = _uiState.asStateFlow()

    private val _cancelSuccess = MutableStateFlow(false)
    val cancelSuccess: StateFlow<Boolean> = _cancelSuccess.asStateFlow()

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
    // AppointmentDetailViewModel içindeki fonksiyon şu şekilde olmalı:
    fun cancelAppointment() {
        viewModelScope.launch {
            // Detay ViewModel'ında zaten appointmentId constructor'dan geliyor,
            // o yüzden dışarıdan ID istemez.
            repository.cancelAppointment(appointmentId)
            // Burada da iptal sonrası bir state güncellemesi (cancelSuccess gibi) ekleyebilirsin.
        }
    }

//    /**
//     * ✅ Cancel: repository status'u CANCELLED yapmalı (silmemeli)
//     */
//    fun cancelAppointment() {
//        viewModelScope.launch {
//            _uiState.update { it.copy(isLoading = true) } // Yükleniyor durumuna sok
//
//            val result = repository.cancelAppointment(appointmentId)
//
//            result.onSuccess {
//                _uiState.update { it.copy(isLoading = false) }
//                _cancelSuccess.value = true // ✅ UI bu değişikliği görüp navigasyon yapacak
//            }.onFailure { e ->
//                _uiState.update {
//                    it.copy(
//                        isLoading = false,
//                        errorMessage = e.message ?: "İptal işlemi başarısız oldu."
//                    )
//                }
//            }
//        }
//    }
//
//    // Navigasyon sonrası state'i sıfırlamak için
//    fun resetCancelSuccess() {
//        _cancelSuccess.value = false
//    }
}