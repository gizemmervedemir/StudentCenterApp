package com.example.studentcenterapp.viewmodel.appointment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentcenterapp.data.AppDI
import com.example.studentcenterapp.data.student.StudentSession
import com.example.studentcenterapp.data.timeslot.TimeSlotRepository
import com.example.studentcenterapp.model.Appointment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID

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
            if (serviceId.isBlank()) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Hatalı Hizmet Kimliği") }
                return@launch
            }

            repository.getAvailableSlots(serviceId)
                .onStart {
                    _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                }
                .catch { e ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = e.message ?: "Hata oluştu")
                    }
                }
                .collect { slots ->
                    val grouped = try {
                        slots.groupBy { LocalDate.parse(it.date) }
                    } catch (e: Exception) {
                        emptyMap()
                    }

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            groupedSlots = grouped,
                            selectedDate = grouped.keys.sorted().firstOrNull(),
                            errorMessage = if (grouped.isEmpty()) "Müsait randevu bulunamadı" else null
                        )
                    }
                }
        }
    }

    fun onDateSelected(date: LocalDate) {
        _uiState.update { it.copy(selectedDate = date) }
    }

    fun onSlotClicked(slotId: String) {
        _uiState.update { it.copy(selectedSlotId = slotId) }
    }

    fun confirmSelectedSlot(
        serviceName: String,
        type: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val uiVal = _uiState.value
        val selectedId = uiVal.selectedSlotId
        val selectedSlot = uiVal.groupedSlots.values.flatten().find { it.id == selectedId }

        val studentId = StudentSession.currentStudentId ?: ""
        val studentName = StudentSession.currentStudentName ?: "Bilinmeyen Öğrenci"

        if (selectedId == null || selectedSlot == null) {
            onError("Lütfen bir saat dilimi seçin")
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val isLocalSuccess = repository.reserveSlot(selectedId)

            if (isLocalSuccess) {
                // serviceId "101" ise departmentId "1" olur.
                val resolvedDeptId = if (serviceId.length >= 1) serviceId.take(1) else "1"

                val newAppointment = Appointment(
                    id = UUID.randomUUID().toString(),
                    studentUid = StudentSession.currentStudentId ?: "",
                    studentId = StudentSession.studentNumber ?: "No Alınamadı",
                    studentName = StudentSession.currentStudentName ?: "Öğrenci",
                    staffId = "not_assigned",
                    serviceName = serviceName,
                    serviceId = serviceId,
                    departmentId = serviceId.take(1), // Artık Firestore'da personel görebilecek!
                    timeSlotId = selectedId,
                    appointmentDate = selectedSlot.date,
                    type = type,
                    status = "pending",
                    startTime = selectedSlot.startTime,
                    endTime = selectedSlot.endTime
                )

                try {
                    val result = AppDI.appointmentRepository.createAppointment(newAppointment)

                    if (result.isSuccess) {
                        _uiState.update { it.copy(isLoading = false) }
                        onSuccess()
                    } else {
                        _uiState.update { it.copy(isLoading = false) }
                        onError("Randevu oluşturulamadı: ${result.exceptionOrNull()?.message}")
                    }
                } catch (e: Exception) {
                    _uiState.update { it.copy(isLoading = false) }
                    onError("Veritabanı hatası: ${e.message}")
                }
            } else {
                _uiState.update { it.copy(isLoading = false) }
                onError("Seçilen slot artık müsait değil.")
            }
        }
    }
}