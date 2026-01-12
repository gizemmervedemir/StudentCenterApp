package com.example.studentcenterapp.ui.staff

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.studentcenterapp.R
import com.example.studentcenterapp.model.Appointment
import com.example.studentcenterapp.ui.common.*
import com.example.studentcenterapp.ui.common.staffBottomTabs // Merkezi tab listesi
import com.example.studentcenterapp.ui.state.UiState
import com.example.studentcenterapp.ui.theme.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun StaffDashboardScreen(
    state: UiState<List<Appointment>>,
    staffName: String,
    selectedFilter: String,
    onFilterChanged: (String) -> Unit,
    actionLoading: Boolean,
    actionError: String?,
    currentRoute: String?,
    onTabSelected: (AppTab) -> Unit,
    onApprove: (String) -> Unit,
    onReject: (String) -> Unit
) {
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }
    val showHistoryDialog = remember { mutableStateOf(false) }
    val selectedApptForHistory = remember { mutableStateOf<Appointment?>(null) }
    val dateFormatter = remember { DateTimeFormatter.ofPattern("d MMMM, EEEE", Locale("tr")) }

    Scaffold(
        bottomBar = {
            AppBottomBar(
                tabs = staffBottomTabs,
                currentRoute = currentRoute,
                onTabSelected = onTabSelected
            )
        },
        containerColor = Color(0xFF4FC3F7)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFF4FC3F7))
        ) {
            // Logo
            Box(
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_oldx),
                    contentDescription = null,
                    modifier = Modifier.size(60.dp, 40.dp)
                )
            }

            // Beyaz Gövde
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.White,
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
            ) {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Text(
                        text = "Merhaba, $staffName.",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(top = 24.dp, bottom = 12.dp)
                    )

                    StaffFilterRow(
                        selectedFilterId = selectedFilter,
                        onSelect = onFilterChanged
                    )

                    // Tarih Seçici
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { selectedDate.value = selectedDate.value.minusDays(1) }) {
                            Icon(Icons.Default.ChevronLeft, null)
                        }
                        Text(
                            text = selectedDate.value.format(dateFormatter),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        IconButton(onClick = { selectedDate.value = selectedDate.value.plusDays(1) }) {
                            Icon(Icons.Default.ChevronRight, null)
                        }
                    }

                    // Liste
                    when (state) {
                        is UiState.Loading -> Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }
                        is UiState.Success -> {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(bottom = 16.dp)
                            ) {
                                items(state.data) { appt ->
                                    StaffAppointmentCard(
                                        appointment = appt,
                                        onApprove = { onApprove(appt.id) },
                                        onReject = { onReject(appt.id) },
                                        onShowHistory = {
                                            selectedApptForHistory.value = appt
                                            showHistoryDialog.value = true
                                        }
                                    )
                                }
                            }
                        }
                        is UiState.Error -> ErrorView(message = state.message)
                    }
                }
            }
        }
    }

    if (showHistoryDialog.value && selectedApptForHistory.value != null) {
        AppointmentHistoryDialog(
            appointment = selectedApptForHistory.value!!,
            onDismiss = { showHistoryDialog.value = false }
        )
    }
}

// --- Alt Bileşenler (UI Components) ---

@Composable
fun StaffAppointmentCard(
    appointment: Appointment,
    onApprove: () -> Unit,
    onReject: () -> Unit,
    onShowHistory: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth().clickable { isExpanded = !isExpanded },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F2F2))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AccountCircle, null, Modifier.size(45.dp), Color.Gray)
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = appointment.studentName, fontWeight = FontWeight.Bold)
                    Text(text = appointment.appointmentDate, fontSize = 12.sp, color = Color.Gray)
                }
                StatusBadge(status = appointment.status)
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    Text("Öğrenci ID: ${appointment.studentId}", fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        if (appointment.status == "pending") {
                            IconButton(onClick = onApprove) { Icon(Icons.Default.CheckCircle, null, tint = Color.Green) }
                            IconButton(onClick = onReject) { Icon(Icons.Default.Cancel, null, tint = Color.Red) }
                        }
                        Button(onClick = onShowHistory) {
                            Text("Geçmiş", fontSize = 10.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatusBadge(status: String) {
    val color = when(status) {
        "approved" -> Color(0xFF48C9B0)
        "pending" -> Color(0xFFFF7043)
        else -> Color.Gray
    }
    Surface(color = color, shape = RoundedCornerShape(8.dp)) {
        Text(status, color = Color.White, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), fontSize = 10.sp)
    }
}

@Composable
fun AppointmentHistoryDialog(appointment: Appointment, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(16.dp), color = Color.White) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("${appointment.studentName} - Randevu Geçmişi", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Daha önce kaydı bulunmamaktadır.", fontSize = 14.sp)
                TextButton(onClick = onDismiss, modifier = Modifier.align(Alignment.End)) {
                    Text("Kapat")
                }
            }
        }
    }
}