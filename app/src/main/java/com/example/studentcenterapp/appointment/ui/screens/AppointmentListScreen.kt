package com.example.studentcenterapp.appointment.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.studentcenterapp.appointment.ui.list.AppointmentListViewModel
import com.example.studentcenterapp.appointment.ui.components.AppointmentCardView
import androidx.compose.foundation.layout.padding


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentListScreen(
    viewModel: AppointmentListViewModel,
    studentId: String
) {
    val appointments by viewModel.appointments.collectAsState()

    LaunchedEffect(studentId) {
        viewModel.load(studentId)
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Appointments") }) }
    ) { padding ->

        if (appointments.isEmpty()) {
            Text("No Appointments", modifier = Modifier.padding(16.dp))
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(appointments) { appt ->
                    AppointmentCardView(
                        appointment = appt,
                        onCancel = { viewModel.cancel(it) }
                    )
                }
            }
        }
    }
}
