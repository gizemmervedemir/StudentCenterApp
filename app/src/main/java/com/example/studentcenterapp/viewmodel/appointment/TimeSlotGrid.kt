package com.example.studentcenterapp.viewmodel.appointment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.studentcenterapp.model.TimeSlot
import com.example.studentcenterapp.viewmodel.appointment.TimeSlotItem

@Composable
fun TimeSlotGrid(
    slots: List<TimeSlot>,
    selectedSlotId: String?,
    onSlotClick: (TimeSlot) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(slots) { slot ->
            val isSelected = slot.id == selectedSlotId

            TimeSlotItem(
                slot = slot,
                isSelected = isSelected,
                onClick = { onSlotClick(slot) }
            )
        }
    }
}