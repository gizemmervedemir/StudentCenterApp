package com.example.studentcenterapp.viewmodel.appointment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studentcenterapp.model.TimeSlot

@Composable
fun TimeSlotItem(
    slot: TimeSlot,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Görseldeki tasarım: Rezerve ise pasif, seçiliyse mavi, değilse açık gri
    val backgroundColor = when {
        slot.isReserved -> Color(0xFFE0E0E0) // Daha koyu gri (pasif)
        isSelected -> Color(0xFF4FC3F7)      // Görseldeki canlı mavi (PrimaryBlue)
        else -> Color(0xFFF2F2F2)           // Açık gri arka plan
    }

    val textColor = when {
        slot.isReserved -> Color.Gray.copy(alpha = 0.6f)
        isSelected -> Color.White
        else -> Color.Gray
    }

    Surface(
        modifier = modifier
            .padding(4.dp)
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(enabled = !slot.isReserved) { onClick() },
        color = backgroundColor,
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = slot.startTime,
                    fontSize = 15.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = textColor
                )

                // Opsiyonel: Eğer alt kısımda rezerve metni istersen küçükçe ekleyebilirsin
                if (slot.isReserved) {
                    Text(
                        text = "Dolu",
                        fontSize = 10.sp,
                        color = textColor,
                        lineHeight = 10.sp
                    )
                }
            }
        }
    }
}