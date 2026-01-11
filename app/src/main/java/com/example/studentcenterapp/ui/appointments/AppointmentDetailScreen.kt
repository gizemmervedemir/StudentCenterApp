package com.example.studentcenterapp.ui.appointments.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.studentcenterapp.ui.common.AppTopBar
import com.example.studentcenterapp.ui.common.ErrorView
import com.example.studentcenterapp.ui.common.LoadingView
import com.example.studentcenterapp.viewmodel.appointment.AppointmentDetailUiState

@Composable
fun AppointmentDetailScreen(
    state: AppointmentDetailUiState,
    onBack: () -> Unit,
    onCancelClick: () -> Unit,
    onRetry: (() -> Unit)? = null // ✅ opsiyonel
) {
    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(
            title = "Randevu Detayı"
            // Eğer AppTopBar'ın back icon desteği varsa:
            // onBackClick = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            when {
                state.isLoading -> {
                    LoadingView(modifier = Modifier)
                    return@Column
                }

                state.errorMessage != null -> {
                    ErrorView(
                        message = state.errorMessage,
                        onRetry = { onRetry?.invoke() } // ✅ varsa retry çalışır
                    )
                    return@Column
                }

                state.appointment == null -> {
                    Text(
                        text = "Appointment not found.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(Modifier.height(24.dp))
                    OutlinedButton(onClick = onBack) { Text("Geri") }
                    return@Column
                }
            }

            val appt = state.appointment

            Text(
                text = "Service: ${appt!!.serviceId}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(8.dp))

            Text(
                text = "Slot: ${appt.timeSlotId}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(8.dp))

            Text(
                text = "Status: ${appt.status}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(16.dp))

            // ✅ Cancel butonu sadece uygun statüste
            val canCancel = !appt.status.equals("cancelled", ignoreCase = true)

            if (canCancel) {
                OutlinedButton(onClick = onCancelClick) {
                    Text("Randevuyu İptal Et")
                }
            } else {
                Text(
                    text = "Bu randevu zaten iptal edilmiş.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.height(24.dp))
            OutlinedButton(onClick = onBack) { Text("Geri") }
        }
    }
}