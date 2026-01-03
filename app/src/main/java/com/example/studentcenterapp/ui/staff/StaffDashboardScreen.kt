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
import androidx.compose.material.icons.filled.KeyboardArrowDown
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
import com.example.studentcenterapp.ui.common.EmptyStateConfig
import com.example.studentcenterapp.ui.common.EmptyStateScreen
import com.example.studentcenterapp.ui.common.LoadingView
import com.example.studentcenterapp.ui.common.ErrorView



private val GrayPanel = Color(0xFFE9E9E9)     // büyük gri zemin
private val GrayCard  = Color(0xFFDADADA)     // list item kart gri
private val ChipBlue  = Color(0xFF2EA7D8)     // sekme outline
private val ChipFill  = Color(0xFFDFF3FB)     // seçili sekme dolu (açık mavi)

private enum class StaffTab { ACTIVE, PENDING, CANCELLED, ALL }

@Composable
fun StaffDashboardScreen(
    state: UiState<List<Appointment>>,
    actionLoading: Boolean,
    actionError: String?,
    currentRoute: String?,
    onTabSelected: (AppTab) -> Unit,
    onApprove: (appointmentId: String) -> Unit,
    onReject: (appointmentId: String) -> Unit
) {
    var selected by remember { mutableStateOf(StaffTab.PENDING) }

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
                // Header row (Merhaba + sağda avatar gri)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Merhaba, Elif.", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.weight(1f))
                    Box(
                        Modifier
                            .size(36.dp)
                            .background(Color(0xFFD0D0D0), CircleShape)
                    )
                }

                // Büyük gri panel (sekme + tarih + liste)
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp),
                    shape = RoundedCornerShape(18.dp),
                    color = GrayPanel
                ) {
                    Column(Modifier.padding(12.dp)) {

                        // 4'lü kutular (outline + seçili dolu)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            SegBox("Aktif\nRandevular", selected == StaffTab.ACTIVE) { selected = StaffTab.ACTIVE }
                            SegBox("Onay\nBekliyor", selected == StaffTab.PENDING, badge = true) { selected = StaffTab.PENDING }
                            SegBox("İptal Edilen\nRandevular", selected == StaffTab.CANCELLED) { selected = StaffTab.CANCELLED }
                            SegBox("Tüm\nRandevular", selected == StaffTab.ALL) { selected = StaffTab.ALL }
                        }

                        // Tarih satırı
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp, bottom = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { /* prev day */ }) {
                                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Prev")
                            }
                            Spacer(Modifier.weight(1f))
                            Text("27 Ekim, Pazartesi", style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.weight(1f))
                            IconButton(onClick = { /* next day */ }) {
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
                            is UiState.Loading -> {
                                LoadingView(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 30.dp)
                                )
                            }

                            is UiState.Error -> {
                                ErrorView(
                                    message = state.message,
                                    onRetry = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 18.dp)
                                )
                            }

                            is UiState.Success -> {
                                val all = state.data

                                val filtered = when (selected) {
                                    StaffTab.PENDING -> all.filter { it.status == "pending" }
                                    StaffTab.ACTIVE -> all.filter { it.status == "approved" }
                                    StaffTab.CANCELLED -> all.filter { it.status == "cancelled" }
                                    StaffTab.ALL -> all
                                }

                                if (filtered.isEmpty()) {
                                    val (title, message) = when (selected) {
                                        StaffTab.PENDING -> "No pending appointments" to "There are no pending requests right now."
                                        StaffTab.ACTIVE -> "No active appointments" to "There are no active appointments right now."
                                        StaffTab.CANCELLED -> "No cancelled appointments" to "There are no cancelled appointments right now."
                                        StaffTab.ALL -> "No appointments" to "There are no appointments to show."
                                    }

                                    EmptyStateScreen(
                                        config = EmptyStateConfig(
                                            title = title,
                                            message = message
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 18.dp)
                                    )
                                } else {
                                    LazyColumn(
                                        verticalArrangement = Arrangement.spacedBy(12.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        items(filtered, key = { it.id }) { appt ->
                                            StaffAppointmentCard(
                                                name = appt.studentId,
                                                time = appt.timeSlotId,
                                                status = appt.status,
                                                actionLoading = actionLoading,
                                                showActions = (selected == StaffTab.PENDING),
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
private fun SegBox(
    text: String,
    selected: Boolean,
    badge: Boolean = false,
    onClick: () -> Unit
) {
    Box {
        Surface(
            modifier = Modifier
                .width(86.dp)
                .height(46.dp)
                .clickable(onClick = onClick),
            shape = RoundedCornerShape(12.dp),
            color = if (selected) ChipFill else Color.Transparent,
            border = BorderStroke(1.dp, ChipBlue)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(text = text, style = MaterialTheme.typography.labelSmall, color = ChipBlue, lineHeight = MaterialTheme.typography.labelSmall.lineHeight)
            }
        }

        if (badge) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 6.dp, y = (-6).dp)
                    .background(Color(0xFFE53935), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("4", color = Color.White, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
private fun StaffAppointmentCard(
    name: String,
    time: String,
    status: String,
    actionLoading: Boolean,
    showActions: Boolean,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = GrayCard
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // sol avatar
            Box(Modifier.size(38.dp).background(Color(0xFFBDBDBD), CircleShape))
            Spacer(Modifier.width(10.dp))

            Column(Modifier.weight(1f)) {
                Text(name, style = MaterialTheme.typography.titleMedium)
                Text(time, style = MaterialTheme.typography.bodySmall, color = Color(0xFF6A6A6A))
            }

            StatusPill(status)

            Spacer(Modifier.width(8.dp))
            Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Expand")
        }
    }
}

@Composable
private fun StatusPill(status: String) {
    val (label, color) = when (status) {
        "pending" -> "Onay" to Color(0xFF2EA7D8)
        "approved" -> "Aktif" to Color(0xFF19B39A)
        "cancelled" -> "İptal" to Color(0xFFE53935)
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
