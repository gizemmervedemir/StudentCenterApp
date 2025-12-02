package com.example.studentcenterapp.appointment.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.studentcenterapp.appointment.domain.AppointmentStatus

@Composable
fun StatusChip(status: AppointmentStatus) {

    val color = when (status) {
        AppointmentStatus.PENDING -> Color(0xFFFFE082)
        AppointmentStatus.CONFIRMED -> Color(0xFFA5D6A7)
        AppointmentStatus.CANCELLED -> Color(0xFFEF9A9A)
    }

    Box(
        modifier = Modifier
            .background(color, RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(status.name)
    }
}
