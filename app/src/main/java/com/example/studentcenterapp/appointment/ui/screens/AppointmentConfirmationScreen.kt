package com.example.studentcenterapp.appointment.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.studentcenterapp.appointment.ui.confirmation.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentConfirmationScreen(
    viewModel: AppointmentConfirmationViewModel,
    studentId: String
) {
    val state by viewModel.state.collectAsState()

    var serviceId by remember { mutableStateOf("") }
    var staffId by remember { mutableStateOf("") }
    var timeSlotId by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Confirm Appointment") }) }
    ) { padding ->

        Column(Modifier.padding(padding).padding(16.dp)) {

            OutlinedTextField(
                serviceId, { serviceId = it },
                label = { Text("Service ID") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                staffId, { staffId = it },
                label = { Text("Staff ID (optional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                timeSlotId, { timeSlotId = it },
                label = { Text("TimeSlot ID") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = {
                    viewModel.create(
                        studentId,
                        serviceId,
                        staffId.ifEmpty { null },
                        timeSlotId
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create")
            }

            Spacer(Modifier.height(16.dp))

            when (state) {
                ConfirmationState.Idle -> {}
                ConfirmationState.Loading -> CircularProgressIndicator()
                is ConfirmationState.Success -> Text("Created!")
                is ConfirmationState.Error -> Text("Error: ${(state as ConfirmationState.Error).message}")
            }
        }
    }
}
