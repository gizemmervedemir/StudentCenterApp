package com.example.studentcenterapp.ui.appointment

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studentcenterapp.data.appointment.AppointmentRepository
import com.example.studentcenterapp.ui.appointments.components.AppointmentDetailScreen
import com.example.studentcenterapp.viewmodel.appointment.AppointmentDetailViewModel
import com.example.studentcenterapp.viewmodel.appointment.AppointmentDetailViewModelFactory

@Composable
fun AppointmentDetailRoute(
    appointmentId: String,
    repository: AppointmentRepository,
    onBack: () -> Unit
) {
    val vm: AppointmentDetailViewModel = viewModel(
        factory = AppointmentDetailViewModelFactory(
            appointmentId = appointmentId,
            repository = repository
        )
    )

    val state by vm.uiState.collectAsState()

    AppointmentDetailScreen(
        state = state,
        onBack = onBack,
        onCancelClick = { vm.cancelAppointment() }
    )
}