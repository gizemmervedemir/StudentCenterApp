package com.example.studentcenterapp.viewmodel.appointment

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.studentcenterapp.model.Appointment
import com.example.studentcenterapp.viewmodel.appointment.AppointmentHistoryViewModel

@Composable
fun AppointmentHistoryScreen(
    viewModel: AppointmentHistoryViewModel,
    modifier: Modifier = Modifier,
    onAppointmentClick: ((Appointment) -> Unit)? = null
) {
    val state by viewModel.uiState.collectAsState()

    when {
        state.isLoading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        state.errorMessage != null -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = state.errorMessage ?: "Unexpected error")
            }
        }

        state.appointments.isEmpty() -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "No appointment history")
            }
        }

        else -> {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(state.appointments, key = { it.id }) { appointment ->
                    AppointmentHistoryItem(
                        appointment = appointment,
                        onClick = {
                            onAppointmentClick?.invoke(appointment)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun AppointmentHistoryItem(
    appointment: Appointment,
    onClick: () -> Unit
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "Service: ${appointment.serviceId}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Status: ${appointment.status}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "TimeSlotId: ${appointment.timeSlotId}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}