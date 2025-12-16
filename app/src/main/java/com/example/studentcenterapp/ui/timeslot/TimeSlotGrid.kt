package com.example.studentcenterapp.ui.timeslot

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.studentcenterapp.model.TimeSlot

@Composable
fun TimeSlotGrid(
    slots: List<TimeSlot>,
    selectedSlotId: String?,
    onSlotClick: (TimeSlot) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3), // 3 sütun: 09:00 10:00 11:00 gibi
        modifier = modifier,
        contentPadding = PaddingValues(8.dp)
    ) {
        items(slots) { slot ->
            val isSelected = slot.id == selectedSlotId
            val isEnabled = !slot.isReserved

            TimeSlotItem(
                slot = slot,
                isSelected = isSelected,
                isEnabled = isEnabled,
                onClick = { onSlotClick(slot) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}