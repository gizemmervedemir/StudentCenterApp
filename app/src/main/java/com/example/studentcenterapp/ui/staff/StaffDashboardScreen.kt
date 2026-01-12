package com.example.studentcenterapp.ui.staff

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.studentcenterapp.model.Appointment
import com.example.studentcenterapp.ui.common.*
import com.example.studentcenterapp.ui.state.UiState
import com.example.studentcenterapp.ui.theme.PrimaryBlue

private val GrayPanel = Color(0xFFE9E9E9)
private val GrayCard  = Color(0xFFDADADA)
private val ChipBlue  = Color(0xFF2EA7D8)
private val ChipFill  = Color(0xFFDFF3FB)

@Composable
fun StaffDashboardScreen(
    state: UiState<List<Appointment>>,
    staffName: String, // ViewModel'den gelen isim
    selectedFilter: String, // ViewModel'deki currentFilter
    onFilterChanged: (String) -> Unit, // ViewModel'deki onFilterChanged
    actionLoading: Boolean,
    actionError: String?,
    currentRoute: String?,
    onTabSelected: (AppTab) -> Unit,
    onApprove: (appointmentId: String) -> Unit,
    onReject: (appointmentId: String) -> Unit
) {
    Scaffold(
        topBar = { AppTopBar(title = "") },
        bottomBar = {
            AppBottomBar(
                tabs = bottomTabs,
                currentRoute = currentRoute,
                onTabSelected = onTabSelected
            )
        },
        containerColor = PrimaryBlue
    ) { padding ->
        ContentCard(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp, vertical = 16.dp)
            ) {
                // Header row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Merhaba, $staffName.", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.weight(1f))
                    Box(
                        Modifier
                            .size(36.dp)
                            .background(Color(0xFFD0D0D0), CircleShape)
                    )
                }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp),
                    shape = RoundedCornerShape(18.dp),
                    color = GrayPanel
                ) {
                    Column(Modifier.padding(12.dp)) {

                        // Filtreleme Kutuları
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            val pendingCount = if (state is UiState.Success) {
                                state.data.count { it.status == "pending" }
                            } else 0

                            SegBox("Aktif\nRandevular", selectedFilter == "approved") { onFilterChanged("approved") }
                            SegBox("Onay\nBekliyor", selectedFilter == "pending", badgeCount = pendingCount) { onFilterChanged("pending") }
                            SegBox("İptal Edilen\nRandevular", selectedFilter == "cancelled") { onFilterChanged("cancelled") }
                            SegBox("Tüm\nRandevular", selectedFilter == "all") { onFilterChanged("all") }
                        }

                        // Tarih Satırı (Statik kalsın veya dinamikleştirilebilir)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp, bottom = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { }) {
                                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Prev")
                            }
                            Spacer(Modifier.weight(1f))
                            Text("Bugün", style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.weight(1f))
                            IconButton(onClick = { }) {
                                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Next")
                            }
                        }

                        if (!actionError.isNullOrBlank()) {
                            Text(
                                text = actionError,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }

                        when (state) {
                            is UiState.Loading -> LoadingView(Modifier.fillMaxWidth().padding(vertical = 30.dp))
                            is UiState.Error -> ErrorView(message = state.message, onRetry = null)
                            is UiState.Success -> {
                                if (state.data.isEmpty()) {
                                    EmptyStateScreen(
                                        config = EmptyStateConfig(
                                            title = "Kayıt Bulunamadı",
                                            message = "Bu kategoriye ait randevu bulunmamaktadır."
                                        ),
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 18.dp)
                                    )
                                } else {
                                    LazyColumn(
                                        verticalArrangement = Arrangement.spacedBy(12.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        items(state.data, key = { it.id }) { appt ->
                                            StaffAppointmentCard(
                                                name = appt.studentId, // İleride studentName ile değiştirilebilir
                                                time = appt.appointmentDate,
                                                status = appt.status,
                                                showActions = (selectedFilter == "pending"),
                                                onApprove = { onApprove(appt.id) },
                                                onReject = { onReject(appt.id) }
                                            )
                                        }
                                        item { Spacer(Modifier.height(8.dp)) }
                                    }
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
private fun RowScope.SegBox( // ✅ RowScope ekledik
    text: String,
    selected: Boolean,
    badgeCount: Int = 0,
    onClick: () -> Unit
) {
    Box(modifier = Modifier.weight(1f)) { // ✅ weight'i Box'a taşıdık
        Surface(
            modifier = Modifier
                .fillMaxWidth() // Genişliği Box'a yay
                .height(46.dp)
                .clickable(onClick = onClick),
            shape = RoundedCornerShape(12.dp),
            color = if (selected) ChipFill else Color.Transparent,
            border = BorderStroke(1.dp, ChipBlue)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelSmall,
                    color = ChipBlue,
                    lineHeight = MaterialTheme.typography.labelSmall.lineHeight
                )
            }
        }

        if (badgeCount > 0) {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 4.dp, y = (-4).dp)
                    .background(Color(0xFFE53935), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = badgeCount.toString(),
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@Composable
private fun StaffAppointmentCard(
    name: String,
    time: String,
    status: String,
    showActions: Boolean,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = GrayCard
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(38.dp).background(Color(0xFFBDBDBD), CircleShape))
                Spacer(Modifier.width(10.dp))

                Column(Modifier.weight(1f)) {
                    Text(name, style = MaterialTheme.typography.titleMedium)
                    Text(time, style = MaterialTheme.typography.bodySmall, color = Color(0xFF6A6A6A))
                }
                StatusPill(status)
            }

            if (showActions) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onReject) {
                        Text("Reddet", color = Color(0xFFE53935))
                    }
                    Button(
                        onClick = onApprove,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF19B39A)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Onayla")
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusPill(status: String) {
    val (label, color) = when (status) {
        "pending" -> "Onay Bekliyor" to Color(0xFF2EA7D8)
        "approved" -> "Aktif" to Color(0xFF19B39A)
        "cancelled" -> "İptal Edildi" to Color(0xFFE53935)
        else -> status to Color(0xFF2EA7D8)
    }

    Surface(
        shape = RoundedCornerShape(50),
        color = Color.Transparent,
        border = BorderStroke(1.dp, color)
    ) {
        Text(
            text = label,
            color = color,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}