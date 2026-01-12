package com.example.studentcenterapp.viewmodel.appointment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentcenterapp.data.timeslot.TimeSlotRepository
import com.example.studentcenterapp.model.TimeSlot
import com.example.studentcenterapp.viewmodel.appointment.TimeSlotCalendarUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

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

    // TimeSlotCalendarViewModel.kt içinde loadSlots fonksiyonuna log ekleyelim
    fun loadSlots() {
        viewModelScope.launch {
            // serviceId'nin boş gelip gelmediğini kontrol edin
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
                    // Filtreleme: Eğer repository tüm slotları dönüyorsa burada türe göre ayıklama gerekebilir.
                    // Şu an tüm slotları tarihe göre grupluyoruz.
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

    // Kullanıcı listeden bir tarih seçtiğinde
    fun onDateSelected(date: LocalDate) {
        _uiState.update { it.copy(selectedDate = date) }
    }

    // Kullanıcı bir saat dilimine (slot) tıkladığında
    fun onSlotClicked(slotId: String) {
        _uiState.update { it.copy(selectedSlotId = slotId) }
    }

    /**
     * Dialog içindeki "Onayla" butonuna basıldığında çağrılır.
     */
    fun confirmSelectedSlot(onSuccess: () -> Unit, onError: (String) -> Unit) {
        val selectedId = _uiState.value.selectedSlotId
        if (selectedId == null) {
            onError("Lütfen bir saat dilimi seçin")
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val isSuccess = repository.reserveSlot(selectedId)

            if (isSuccess) {
                _uiState.update { it.copy(isLoading = false) }
                onSuccess()
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Seçilen slot artık müsait değil."
                    )
                }
                onError("Rezervasyon başarısız oldu.")
            }
        }
    }
}