package com.example.studentcenterapp.appointment.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.studentcenterapp.appointment.domain.Appointment
import com.example.studentcenterapp.appointment.domain.AppointmentStatus

@Composable
fun AppointmentCardView(
    appointment: Appointment,
    onCancel: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.padding(8.dp)) {

        Column(modifier = Modifier.padding(12.dp)) {

            Text("Service: ${appointment.serviceId}")
            Spacer(Modifier.height(4.dp))

            Text("TimeSlot: ${appointment.timeSlotId}")
            Spacer(Modifier.height(4.dp))

            StatusChip(status = appointment.status)
            Spacer(Modifier.height(8.dp))

            Row {
                Text(appointment.id, modifier = Modifier.weight(1f))

                if (appointment.status != AppointmentStatus.CANCELLED) {
                    TextButton(onClick = { onCancel(appointment.id) }) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}
