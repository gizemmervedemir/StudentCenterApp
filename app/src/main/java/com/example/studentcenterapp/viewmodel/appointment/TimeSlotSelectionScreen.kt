package com.example.studentcenterapp.viewmodel.appointment

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.studentcenterapp.model.TimeSlot
import com.example.studentcenterapp.viewmodel.appointment.TimeSlotGrid
import com.example.studentcenterapp.viewmodel.appointment.TimeSlotSelectionViewModel

@Composable
fun TimeSlotSelectionScreen(
    viewModel: TimeSlotSelectionViewModel,
    onSlotConfirmed: (TimeSlot) -> Unit,
    onOpenCalendar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    when {
        state.isLoading -> {
            Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        state.errorMessage != null -> {
            Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(state.errorMessage ?: "Unexpected error")
            }
        }

        state.slots.isEmpty() -> {
            Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No available time slots")
            }
        }

        else -> {
            Column(modifier = modifier.padding(16.dp)) {

                Button(onClick = onOpenCalendar) {
                    Text("Open Calendar")
                }

                Spacer(modifier = Modifier.height(12.dp))

                TimeSlotGrid(
                    slots = state.slots,
                    selectedSlotId = state.selectedSlotId,
                    onSlotClick = { slot ->
                        viewModel.onSlotClicked(slot)
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                val selectedSlot =
                    state.slots.firstOrNull { it.id == state.selectedSlotId }

                Button(
                    onClick = {
                        selectedSlot?.let { onSlotConfirmed(it) }
                    },
                    enabled = selectedSlot != null
                ) {
                    Text("Confirm Slot")
                }
            }
        }
    }
}