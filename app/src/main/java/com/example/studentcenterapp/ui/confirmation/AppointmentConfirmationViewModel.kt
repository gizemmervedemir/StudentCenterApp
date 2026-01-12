package com.example.studentcenterapp.ui.confirmation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentcenterapp.data.appointment.AppointmentRepository
import com.example.studentcenterapp.model.Appointment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class AppointmentConfirmationViewModel(
    private val appointmentRepository: AppointmentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppointmentConfirmationUiState())
    val uiState: StateFlow<AppointmentConfirmationUiState> = _uiState.asStateFlow()

    fun setSelection(
        studentId: String,
        departmentName: String,
        serviceId: String,
        serviceName: String,
        timeSlotId: String,
        scheduledStartMillis: Long,
        dateTimeText: String
    ) {
        _uiState.update {
            it.copy(
                studentId = studentId,
                departmentName = departmentName,
                serviceId = serviceId,
                serviceName = serviceName,
                timeSlotId = timeSlotId,
                scheduledStartMillis = scheduledStartMillis,
                dateTimeText = dateTimeText,
                isLoading = false,
                isSuccess = false,
                errorMessage = null
            )
        }
    }

    fun confirm() {
        val s = _uiState.value

        if (!s.isValid()) {
            _uiState.update { it.copy(errorMessage = "Seçim bilgileri eksik. Lütfen tekrar deneyin.") }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null, isSuccess = false) }

        viewModelScope.launch {
            // ✅ 1. ADIM: Appointment nesnesini modeline uygun şekilde hazırla
            val newAppointment = Appointment(
                id = UUID.randomUUID().toString(),
                studentId = s.studentId,
                studentName = "Öğrenci", // Gerekirse ekle
                staffId = "not_assigned",
                serviceName = s.serviceName,
                departmentName = s.departmentName,
                serviceId = s.serviceId,
                timeSlotId = s.timeSlotId,
                appointmentDate = s.dateTimeText, // Görseldeki tarih metni
                type = "office", // Veya seçimden gelen tip
                status = "pending",
                startTime = s.dateTimeText.split(" ").lastOrNull() ?: "" // Saat kısmını ayırabilirsin
            )

            // ✅ 2. ADIM: Tek tek parametre yerine nesneyi gönder
            val result = appointmentRepository.createAppointment(newAppointment)

            if (result.isSuccess) {
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            } else {
                val msg = result.exceptionOrNull()?.message ?: "Randevu oluşturulamadı."
                _uiState.update { it.copy(isLoading = false, errorMessage = msg) }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun consumeSuccess() {
        _uiState.update { it.copy(isSuccess = false) }
    }
}