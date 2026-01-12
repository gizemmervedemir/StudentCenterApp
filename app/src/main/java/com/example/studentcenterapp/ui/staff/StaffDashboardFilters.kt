package com.example.studentcenterapp.ui.staff

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studentcenterapp.ui.theme.PrimaryBlue

enum class StaffAppointmentFilter(val label: String, val id: String) {
    ACTIVE("Aktif\nRandevular", "approved"),
    PENDING("Onay\nBekliyor", "pending"),
    CANCELLED("İptal Edilen\nRandevular", "cancelled"),
    ALL("Tüm\nRandevular", "all")
}

@Composable
fun StaffFilterRow(
    selectedFilterId: String,
    pendingCount: Int = 0,
    onSelect: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        StaffAppointmentFilter.entries.forEach { filter ->
            FilterTabItem(
                text = filter.label,
                isSelected = selectedFilterId == filter.id,
                modifier = Modifier.weight(1f),
                hasBadge = filter == StaffAppointmentFilter.PENDING && pendingCount > 0,
                badgeCount = pendingCount,
                onClick = { onSelect(filter.id) }
            )
        }
    }
}

@Composable
fun FilterTabItem(
    text: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    hasBadge: Boolean = false,
    badgeCount: Int = 0,
    onClick: () -> Unit
) {
    Box(modifier = modifier) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clickable { onClick() },
            color = if (isSelected) PrimaryBlue.copy(alpha = 0.1f) else Color.Transparent,
            border = BorderStroke(1.dp, PrimaryBlue),
            shape = RoundedCornerShape(8.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = text,
                    fontSize = 9.sp,
                    lineHeight = 11.sp,
                    textAlign = TextAlign.Center,
                    color = PrimaryBlue,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
        if (hasBadge && badgeCount > 0) {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 4.dp, y = (-4).dp)
                    .background(Color.Red, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = badgeCount.toString(),
                    color = Color.White,
                    fontSize = 10.sp,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}