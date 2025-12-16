package com.example.studentcenterapp.ui.timeslot

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun TimeSlotCalendarView(
    viewModel: TimeSlotCalendarViewModel,
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
                Text(state.errorMessage!!)
            }
        }

        state.groupedSlots.isEmpty() -> {
            Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No slots available")
            }
        }

        else -> {
            Column(modifier.padding(16.dp)) {

                // 🗓️ Date picker list
                LazyColumn {
                    items(state.groupedSlots.keys.toList()) { date ->
                        val isSelected = date == state.selectedDate
                        Text(
                            text = date.toString(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.onDateSelected(date) }
                                .padding(8.dp),
                            color = if (isSelected)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onBackground
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                state.selectedDate?.let { selected ->
                    val slotsForDay = state.groupedSlots[selected].orEmpty()

                    TimeSlotGrid(
                        slots = slotsForDay,
                        selectedSlotId = null, // İstersek ileride seçili slot state'i ekleriz
                        onSlotClick = { /* TODO: calendar üzerinden slot seçimi buradan ilerler */ },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}