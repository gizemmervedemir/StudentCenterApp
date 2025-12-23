package com.example.studentcenterapp.ui.staff

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

enum class StaffAppointmentFilter(val label: String) {
    ACTIVE("Aktif\nRandevular"),
    PENDING("Onay\nBekliyor"),
    CANCELLED("İptal Edilen\nRandevular"),
    ALL("Tüm\nRandevular")
}

@Composable
fun StaffFilterRow(
    selected: StaffAppointmentFilter,
    onSelect: (StaffAppointmentFilter) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.padding(top = 12.dp, bottom = 10.dp)
    ) {
        StaffFilterChip(
            text = StaffAppointmentFilter.ACTIVE.label,
            selected = selected == StaffAppointmentFilter.ACTIVE,
            onClick = { onSelect(StaffAppointmentFilter.ACTIVE) }
        )
        StaffFilterChip(
            text = StaffAppointmentFilter.PENDING.label,
            selected = selected == StaffAppointmentFilter.PENDING,
            onClick = { onSelect(StaffAppointmentFilter.PENDING) }
        )
        StaffFilterChip(
            text = StaffAppointmentFilter.CANCELLED.label,
            selected = selected == StaffAppointmentFilter.CANCELLED,
            onClick = { onSelect(StaffAppointmentFilter.CANCELLED) }
        )
        StaffFilterChip(
            text = StaffAppointmentFilter.ALL.label,
            selected = selected == StaffAppointmentFilter.ALL,
            onClick = { onSelect(StaffAppointmentFilter.ALL) }
        )
    }
}

@Composable
private fun StaffFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val container = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    val content = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary

    Surface(
        modifier = Modifier
            .widthIn(min = 72.dp)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.small,
        color = container,
        contentColor = content,
        border = if (selected) null else BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
        )
    }
}
