package com.example.studentcenterapp.ui.staff

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.studentcenterapp.R
import com.example.studentcenterapp.model.Appointment
import com.example.studentcenterapp.ui.common.*
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
        topBar = {
            Box(
                modifier = Modifier.fillMaxWidth().background(PrimaryBlue).padding(top = 16.dp, bottom = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(painter = painterResource(id = R.drawable.logo_oldx), contentDescription = null, modifier = Modifier.size(60.dp, 40.dp))
            }
        },
        bottomBar = {
            AppBottomBar(tabs = bottomTabs, currentRoute = currentRoute, onTabSelected = onTabSelected)
        },
        containerColor = PrimaryBlue
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Text(
                text = "Merhaba, $staffName.",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = Color.White,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
            )

            ContentCard(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.fillMaxSize().padding(top = 20.dp)) {

                    val pendingCount = if (state is UiState.Success) {
                        state.data.count { it.status == "pending" }
                    } else 0

                    StaffFilterRow(selectedFilterId = selectedFilter, pendingCount = pendingCount, onSelect = onFilterChanged)

                    // --- TARİH SEÇİCİ ---
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp, horizontal = 16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { selectedDate.value = selectedDate.value.minusDays(1) }) {
                            Icon(Icons.Default.ChevronLeft, "Geri", modifier = Modifier.size(28.dp))
                        }
                        Text(
                            text = selectedDate.value.format(dateFormatter),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            modifier = Modifier.padding(horizontal = 20.dp),
                            textAlign = TextAlign.Center
                        )
                        IconButton(onClick = { selectedDate.value = selectedDate.value.plusDays(1) }) {
                            Icon(Icons.Default.ChevronRight, "İleri", modifier = Modifier.size(28.dp))
                        }
                    }

                    when (state) {
                        is UiState.Loading -> Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator(color = PrimaryBlue) }
                        is UiState.Success -> {
                            if (state.data.isEmpty()) {
                                EmptyStateScreen(config = EmptyStateConfig("Kayıt Bulunamadı", "Görünecek randevu yok."))
                            } else {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
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
                                    item { Spacer(modifier = Modifier.height(20.dp)) }
                                }
                            }
                        }
                        is UiState.Error -> Box(Modifier.fillMaxSize(), Alignment.Center) { Text(state.message, color = Color.Red) }
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
                    Text(text = appointment.studentName, style = MaterialTheme.typography.titleSmall, color = DarkText)
                    Text(text = appointment.appointmentDate, style = MaterialTheme.typography.bodySmall, color = lightText)
                }

                StatusBadge(status = appointment.status)
                Icon(imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Color.Gray)
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 16.dp, start = 57.dp)) {
                    // Figma'daki Sade Tasarım: Sadece Öğrenci No ve Son Randevu Bilgisi
                    DetailInfoItem(icon = Icons.Default.Badge, text = appointment.studentId)
                    Spacer(modifier = Modifier.height(10.dp))
                    DetailInfoItem(icon = Icons.Default.CalendarToday, text = "Son Randevu Tarihi: --")

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                        if (appointment.status == "pending") {
                            IconButton(onClick = onApprove) { Icon(Icons.Default.CheckCircle, null, Modifier.size(32.dp), Color(0xFF48C9B0)) }
                            IconButton(onClick = onReject) { Icon(Icons.Default.Cancel, null, Modifier.size(32.dp), Color.Red) }
                        }

                        Button(
                            onClick = onShowHistory,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(32.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp)
                        ) {
                            Text("Randevu Geçmişi", fontSize = 10.sp, color = Color.White)
                        }

                        if (appointment.status != "pending") {
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(onClick = { }) { Icon(Icons.Default.Chat, null, tint = Color.Gray) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AppointmentHistoryDialog(
    appointment: Appointment,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(modifier = Modifier.fillMaxWidth().padding(16.dp), shape = RoundedCornerShape(28.dp), color = Color(0xFFF2F2F2)) {
            Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AccountCircle, null, Modifier.size(48.dp), Color.Gray)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = appointment.studentName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(text = appointment.studentId, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.History, null, Modifier.size(18.dp), Color.Black)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Randevu Hareketleri", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }

                HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)

                Text(text = "Daha önce randevu kaydı bulunmamaktadır.", fontSize = 12.sp, color = Color.DarkGray)

                TextButton(onClick = onDismiss, modifier = Modifier.align(Alignment.End)) {
                    Text("Kapat", color = PrimaryBlue, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun DetailInfoItem(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, Modifier.size(16.dp), Color.Gray)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, fontSize = 12.sp, color = Color.DarkGray)
    }
}

@Composable
fun StatusBadge(status: String) {
    val (label, color) = when (status) {
        "approved" -> "Aktif" to Color(0xFF48C9B0)
        "pending" -> "Onay" to Color(0xFFFF7043)
        "cancelled" -> "İptal" to Color(0xFFE91E63)
        else -> "Bilinmiyor" to Color.Gray
    }
    Surface(color = color, shape = RoundedCornerShape(12.dp)) {
        Text(text = label, color = Color.White, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
    }
}