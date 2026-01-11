package com.example.studentcenterapp.ui.confirmation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.studentcenterapp.ui.appointments.components.StatusChip
import com.example.studentcenterapp.ui.common.ContentCard

@Composable
fun AppointmentSummaryCard(
    departmentName: String,
    serviceText: String,
    dateTimeText: String,
    status: String = "pending",
    modifier: Modifier = Modifier
) {
    ContentCard(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Randevu Özeti", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                if (status.isNotBlank()) {
                    StatusChip(status = status)
                }
            }

            Spacer(Modifier.height(10.dp))
            Text("Bölüm: ${departmentName.ifBlank { "-" }}")
            Spacer(Modifier.height(6.dp))
            Text("Hizmet: ${serviceText.ifBlank { "-" }}")
            Spacer(Modifier.height(6.dp))
            Text("Tarih/Saat: ${dateTimeText.ifBlank { "-" }}")
        }
    }
}
