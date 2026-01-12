package com.example.studentcenterapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studentcenterapp.model.Appointment
import com.example.studentcenterapp.ui.appointments.AppointmentListFilter
import com.example.studentcenterapp.ui.appointments.components.StatusChip
import com.example.studentcenterapp.ui.common.AppBottomBar
import com.example.studentcenterapp.ui.common.AppTab
import com.example.studentcenterapp.ui.common.AppTopBar
import com.example.studentcenterapp.ui.common.ErrorView
import com.example.studentcenterapp.ui.common.LoadingView
import com.example.studentcenterapp.ui.common.bottomTabs
import com.example.studentcenterapp.viewmodel.appointment.AppointmentListViewModel
import com.example.studentcenterapp.viewmodel.appointment.AppointmentListViewModelFactory

@Composable
fun AppointmentListScreen(
    factory: AppointmentListViewModelFactory,
    currentRoute: String?,            // NavHost'tan gelen rota bilgisi
    onTabSelected: (AppTab) -> Unit,   // Alt bar tıklama yönetimi
    onAppointmentClick: (appointmentId: String) -> Unit
) {
    val vm: AppointmentListViewModel = viewModel(factory = factory)
    val state by vm.uiState.collectAsState()

    // Scaffold kullanımı alt barın (bottomBar) ekranın en altında sabit kalmasını sağlar
    Scaffold(
        bottomBar = {
            AppBottomBar(
                tabs = bottomTabs,
                currentRoute = currentRoute,
                onTabSelected = onTabSelected
            )
        },
        containerColor = Color(0xFF4FC3F7) // Üstteki mavi alanın rengi
    ) { paddingValues ->
        // paddingValues, Scaffold'ın alt bar için ayırdığı boşluğu Column'a uygular
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFF4FC3F7))
        ) {
            // Figma Tasarımı: Üst bar (VA logosu vb.)
            AppTopBar(title = "")

            // Figma Tasarımı: Beyaz, üst köşeleri yuvarlatılmış ana gövde
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.White,
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = "Randevularım",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        ),
                        modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)
                    )

                    // Yaklaşan / Geçmiş filtresi
                    FilterRow(
                        selected = state.selectedFilter,
                        onUpcoming = { vm.setFilter(AppointmentListFilter.UPCOMING) },
                        onPast = { vm.setFilter(AppointmentListFilter.PAST) }
                    )

                    Spacer(Modifier.height(16.dp))

                    // Liste veya Durum Görünümleri
                    when {
                        state.isLoading -> LoadingView(modifier = Modifier.fillMaxWidth())
                        state.errorMessage != null -> ErrorView(
                            message = state.errorMessage ?: "Bir hata oluştu",
                            onRetry = { vm.retry() }
                        )
                        state.isEmpty -> {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text("Henüz randevunuz bulunmuyor.", color = Color.Gray)
                            }
                        }
                        else -> {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(bottom = 20.dp)
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
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        val isUpcoming = selected == AppointmentListFilter.UPCOMING

        FilterButton(
            text = "Yaklaşan",
            isSelected = isUpcoming,
            onClick = onUpcoming,
            modifier = Modifier.weight(1f)
        )
        FilterButton(
            text = "Geçmiş",
            isSelected = !isUpcoming,
            onClick = onPast,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun FilterButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(40.dp),
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) Color.White else Color.Transparent,
        shadowElevation = if (isSelected) 2.dp else 0.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) Color.Black else Color.Gray
            )
        }
    }
}

@Composable
private fun AppointmentCardItem(
    appointment: Appointment,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFBFBFB)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Hizmet: ${appointment.serviceId}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Tarih: ${appointment.timeSlotId}",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }
            StatusChip(status = appointment.status)
        }
    }
}