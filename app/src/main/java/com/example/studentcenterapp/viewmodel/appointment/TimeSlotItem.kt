package com.example.studentcenterapp.viewmodel.appointment

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.studentcenterapp.model.TimeSlot

@Composable
fun TimeSlotItem(
    slot: TimeSlot,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // ✅ Basit UI: seçiliyse yazı rengi değişiyor
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = !slot.isReserved) { onClick() }
            .padding(12.dp)
    ) {
        Text(
            text = "${slot.startTime} - ${slot.endTime}",
            style = MaterialTheme.typography.bodyLarge,
            color = when {
                slot.isReserved -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                isSelected -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.onSurface
            }
        )

        if (slot.isReserved) {
            Text(
                text = "Reserved",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}