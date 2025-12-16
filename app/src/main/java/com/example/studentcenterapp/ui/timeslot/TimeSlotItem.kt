package com.example.studentcenterapp.ui.timeslot

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.studentcenterapp.model.TimeSlot
import java.time.format.DateTimeFormatter

@Composable
fun TimeSlotItem(
    slot: TimeSlot,
    isSelected: Boolean,
    isEnabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    val backgroundColor =
        when {
            !isEnabled -> MaterialTheme.colorScheme.surfaceVariant
            isSelected -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.surface
        }

    val contentColor =
        when {
            !isEnabled -> MaterialTheme.colorScheme.onSurfaceVariant
            isSelected -> MaterialTheme.colorScheme.onPrimary
            else -> MaterialTheme.colorScheme.onSurface
        }

    Surface(
        modifier = modifier
            .wrapContentSize()
            .clip(MaterialTheme.shapes.small)
            .clickable(enabled = isEnabled) { onClick() },
        color = backgroundColor,
        tonalElevation = if (isSelected) 4.dp else 0.dp
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = slot.start.format(timeFormatter),
                style = MaterialTheme.typography.bodyMedium,
                color = contentColor,
                textAlign = TextAlign.Center
            )
        }
    }
}