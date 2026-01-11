package com.example.studentcenterapp.ui.appointments.card

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studentcenterapp.model.Appointment
import com.example.studentcenterapp.ui.appointments.components.StatusChip
import com.example.studentcenterapp.viewmodel.appointment.AppointmentCardViewModel
import com.example.studentcenterapp.viewmodel.appointment.AppointmentCardViewModelFactory

@Composable
fun AppointmentCardView(
    appointment: Appointment,
    departmentName: String,
    serviceName: String,
    dateTimeText: String,
    onClick: (appointmentId: String) -> Unit
) {
    // ✅ Her card için ViewModel (senin istediğin şekilde)
    val vm: AppointmentCardViewModel = viewModel(
        key = "appointment_card_${appointment.id}",
        factory = AppointmentCardViewModelFactory(
            appointment = appointment,
            departmentName = departmentName,
            serviceName = serviceName,
            dateTimeText = dateTimeText
        )
    )

    val model by vm.uiModel.collectAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(model.id) },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = model.title,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = model.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(Modifier.width(12.dp))
                StatusChip(status = model.status)
            }

            Spacer(Modifier.height(10.dp))

            Text(
                text = model.dateTimeText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
