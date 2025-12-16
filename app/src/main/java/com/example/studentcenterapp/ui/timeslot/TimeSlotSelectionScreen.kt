package com.example.studentcenterapp.ui.timeslot

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.studentcenterapp.model.TimeSlot
import java.time.format.DateTimeFormatter

@Composable
fun TimeSlotSelectionScreen(
    viewModel: TimeSlotSelectionViewModel,
    onSlotConfirmed: (TimeSlot) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    when {
        state.isLoading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        state.errorMessage != null -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = state.errorMessage!!)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { viewModel.loadSlots() }) {
                        Text("Retry")
                    }
                }
            }
        }

        state.slots.isEmpty() -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No available time slots")
            }
        }

        else -> {
            TimeSlotGrid(
                slots = state.slots,
                selectedSlotId = state.selectedSlotId,
                onSlotClick = { slot ->
                    viewModel.onSlotClicked(slot)
                    onSlotConfirmed(slot)
                },
                modifier = modifier.fillMaxSize()
            )
        }
    }
}


