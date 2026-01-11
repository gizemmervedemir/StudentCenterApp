package com.example.studentcenterapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studentcenterapp.data.appointment.AppointmentRecord
import com.example.studentcenterapp.ui.appointments.AppointmentListFilter
import com.example.studentcenterapp.ui.common.AppTopBar
import com.example.studentcenterapp.ui.common.ErrorView
import com.example.studentcenterapp.ui.common.LoadingView
import com.example.studentcenterapp.viewmodel.appointment.AppointmentListViewModel
import com.example.studentcenterapp.viewmodel.appointment.AppointmentListViewModelFactory

@Composable
fun AppointmentListScreen(
    factory: AppointmentListViewModelFactory,
    onAppointmentClick: (appointmentId: String) -> Unit
) {
    val vm: AppointmentListViewModel = viewModel(factory = factory)
    val state by vm.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(title = "Randevular")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            FilterRow(
                selected = state.selectedFilter,
                onUpcoming = { vm.setFilter(AppointmentListFilter.UPCOMING) },
                onPast = { vm.setFilter(AppointmentListFilter.PAST) }
            )

            Spacer(Modifier.height(12.dp))

            if (state.isLoading) {
                LoadingView(modifier = Modifier.fillMaxWidth())
                return@Column
            }

            if (state.errorMessage != null) {
                ErrorView(
                    message = state.errorMessage ?: "",
                    onRetry = { vm.retry() }
                )
                return@Column
            }

            if (state.isEmpty) {
                Text(
                    text = "Henüz randevu yok.",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
                return@Column
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(state.visibleAppointments, key = { it.id }) { appt ->
                    AppointmentCardItem(
                        appointment = appt,
                        onClick = { onAppointmentClick(appt.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterRow(
    selected: AppointmentListFilter,
    onUpcoming: () -> Unit,
    onPast: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val upcomingSelected = selected == AppointmentListFilter.UPCOMING

        Button(
            onClick = onUpcoming,
            modifier = Modifier.weight(1f),
            enabled = !upcomingSelected
        ) {
            Text("Yaklaşan")
        }

        OutlinedButton(
            onClick = onPast,
            modifier = Modifier.weight(1f),
            enabled = upcomingSelected
        ) {
            Text("Geçmiş")
        }
    }
}

/**
 * ✅ Bu kart şimdilik AppointmentRecord ile çalışıyor.
 * #39 tamamlanınca bunu direkt AppointmentCardView(...) ile değiştirebilirsin.
 */
@Composable
private fun AppointmentCardItem(
    appointment: AppointmentRecord,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Service: ${appointment.serviceId}",
                        fontSize = 16.sp
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Slot: ${appointment.timeSlotId}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(Modifier.width(12.dp))
                SimpleStatusChip(status = appointment.status)
            }

            Spacer(Modifier.height(10.dp))

            // Şimdilik date/time text yoksa burada göstermiyoruz.
            // Eğer AppointmentRecord içinde tarih/saat varsa buraya ekleriz.
        }
    }
}

/**
 * ✅ Basit chip. (#40'da StatusChip component gelince bunu kaldırıp StatusChip'e geçersin.)
 */
@Composable
private fun SimpleStatusChip(status: String) {
    val (bg, fg, label) = when (status.lowercase()) {
        "approved" -> Triple(Color(0xFFD7F5DD), Color(0xFF1B5E20), "Approved")
        "cancelled" -> Triple(Color(0xFFFFE0E0), Color(0xFFB71C1C), "Cancelled")
        "pending" -> Triple(Color(0xFFFFF4D1), Color(0xFF8A6D00), "Pending")
        else -> Triple(Color(0xFFE0E0E0), Color(0xFF424242), status)
    }

    Text(
        text = label,
        color = fg,
        fontSize = 12.sp,
        modifier = Modifier
            .background(bg, RoundedCornerShape(999.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    )
}
