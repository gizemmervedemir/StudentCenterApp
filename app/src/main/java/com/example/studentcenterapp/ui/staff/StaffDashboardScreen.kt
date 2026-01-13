package com.example.studentcenterapp.ui.staff

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.example.studentcenterapp.ui.state.UiState
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun StaffDashboardScreen(
    state: UiState<List<Appointment>>,
    staffName: String,
    selectedFilter: String,
    onFilterChanged: (String) -> Unit,
    currentRoute: String?,
    onTabSelected: (AppTab) -> Unit,
    onApprove: (String) -> Unit,
    onReject: (String) -> Unit,
    // GÜNCELLEME: studentUid, studentName ve serviceName artık beraber gidiyor
    onChatClick: (String, String, String) -> Unit
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
                                        },
                                        // GÜNCELLEME: appt.departmentName (serviceName) eklendi
                                        onChatClick = {
                                            onChatClick(appt.studentUid, appt.studentName, appt.departmentName)
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

@Composable
fun StaffAppointmentCard(
    appointment: Appointment,
    onApprove: () -> Unit,
    onReject: () -> Unit,
    onShowHistory: () -> Unit,
    onChatClick: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded },
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F2F2))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AccountCircle, null, Modifier.size(45.dp), Color.Gray)
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = appointment.studentName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(text = appointment.startTime, fontSize = 13.sp, color = Color.Gray)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (appointment.status == "pending") {
                        IconButton(onClick = onApprove) {
                            Icon(Icons.Default.CheckCircle, null, tint = Color.Black)
                        }
                    }
                    IconButton(onClick = onReject) {
                        Icon(Icons.Default.Cancel, null, tint = Color.Black)
                    }
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    InfoItem(Icons.Default.Business, appointment.departmentName)
                    InfoItem(Icons.Default.Tag, "Öğrenci No: ${appointment.studentId}")

                    // GÜNCELLEME: Sadece veri varsa "Son Randevu Tarihi" satırı gözükür
                    if (!appointment.appointmentDate.isNullOrEmpty()) {
                        InfoItem(Icons.Default.DateRange, "Son Randevu Tarihi: ${appointment.appointmentDate}")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = onShowHistory,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF06292)),
                            shape = RoundedCornerShape(15.dp),
                            modifier = Modifier.height(38.dp)
                        ) {
                            Text("Randevu Geçmişi", color = Color.White, fontSize = 12.sp)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        IconButton(
                            onClick = onChatClick,
                            modifier = Modifier
                                .size(38.dp)
                                .background(Color(0xFFF06292), CircleShape)
                        ) {
                            Icon(Icons.Default.ChatBubble, null, tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoItem(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
        Icon(icon, null, modifier = Modifier.size(16.dp), tint = Color.Black)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, fontSize = 13.sp, color = Color.Black)
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