package com.example.studentcenterapp.ui.appointments.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StatusChip(
    status: String,
    modifier: Modifier = Modifier
) {
    val s = status.lowercase()

    val (bg, fg, label) = when (s) {
        "approved" -> Triple(Color(0xFFD7F5DD), Color(0xFF1B5E20), "Approved")
        "cancelled" -> Triple(Color(0xFFFFE0E0), Color(0xFFB71C1C), "Cancelled")
        "pending" -> Triple(Color(0xFFFFF4D1), Color(0xFF8A6D00), "Pending")
        else -> Triple(Color(0xFFE0E0E0), Color(0xFF424242), status)
    }

    Text(
        text = label,
        color = fg,
        fontSize = 12.sp,
        style = MaterialTheme.typography.labelMedium,
        modifier = modifier
            .background(bg, RoundedCornerShape(999.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    )
}
