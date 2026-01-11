package com.example.studentcenterapp.viewmodel.appointment

import androidx.lifecycle.ViewModel
import com.example.studentcenterapp.model.Appointment
import com.example.studentcenterapp.ui.appointments.card.AppointmentCardUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppointmentCardViewModel(
    appointment: Appointment,
    departmentName: String,
    serviceName: String,
    dateTimeText: String
) : ViewModel() {

    private val _uiModel = MutableStateFlow(
        AppointmentCardUiModel(
            id = appointment.id,
            title = serviceName.ifBlank { "Service: ${appointment.serviceId}" },
            subtitle = departmentName.ifBlank { "Department" },
            dateTimeText = dateTimeText.ifBlank { "Tarih/Saat: - " },
            status = appointment.status
        )
    )
    val uiModel: StateFlow<AppointmentCardUiModel> = _uiModel.asStateFlow()
}
