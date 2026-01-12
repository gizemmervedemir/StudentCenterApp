package com.example.studentcenterapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.studentcenterapp.model.TimeSlot
import com.example.studentcenterapp.ui.common.AppBottomBar
import com.example.studentcenterapp.ui.common.AppTab
import com.example.studentcenterapp.ui.common.studentBottomTabs
import com.example.studentcenterapp.viewmodel.appointment.TimeSlotCalendarViewModel
import com.example.studentcenterapp.viewmodel.appointment.TimeSlotGrid
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun TimeSlotSelectionScreen(
    serviceName: String,
    viewModel: TimeSlotCalendarViewModel,
    currentRoute: String?,
    onTabSelected: (AppTab) -> Unit,
    onBackClick: () -> Unit,
    onAppointmentCreated: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var showConfirmDialog by remember { mutableStateOf(false) }

    // ✅ Değişkeni Scaffold dışında tanımlayarak scope hatasını çözüyoruz
    val selectedSlot = remember(state.selectedSlotId, state.groupedSlots) {
        state.groupedSlots.values.flatten().find { it.id == state.selectedSlotId }
    }

    Scaffold(
        bottomBar = {
            AppBottomBar(
                tabs = studentBottomTabs,
                currentRoute = currentRoute,
                onTabSelected = onTabSelected
            )
        },
        containerColor = Color(0xFF4FC3F7)
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            // Geri Butonu
            IconButton(onClick = onBackClick, modifier = Modifier.padding(8.dp)) {
                Icon(
                    imageVector = Icons.Default.ChevronLeft,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.White,
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp)) {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(top = 32.dp, bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        state.groupedSlots.forEach { (date, slots) ->
                            item {
                                Text(
                                    text = date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy, EEEE", Locale("tr"))),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(bottom = 12.dp)
                                )
                                TimeSlotGrid(
                                    slots = slots,
                                    selectedSlotId = state.selectedSlotId,
                                    onSlotClick = { viewModel.onSlotClicked(it.id) }
                                )
                            }
                        }
                    }

                    // Alt Buton
                    Button(
                        onClick = { showConfirmDialog = true },
                        enabled = selectedSlot != null && !state.isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp)
                            .height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4FC3F7)),
                        shape = RoundedCornerShape(26.dp)
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text("Randevu Al", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    }

    // ✅ Onay Dialogu
    if (showConfirmDialog && selectedSlot != null) {
        AppointmentConfirmDialog(
            serviceName = serviceName,
            slot = selectedSlot,
            onDismiss = { showConfirmDialog = false },
            onConfirm = {
                viewModel.confirmSelectedSlot(
                    onSuccess = {
                        showConfirmDialog = false
                        onAppointmentCreated()
                    },
                    onError = { /* Opsiyonel: Toast veya Snackbar gösterimi */ }
                )
            }
        )
    }
}

@Composable
fun AppointmentConfirmDialog(
    serviceName: String,
    slot: TimeSlot,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = slot.date, // Gerekirse parse edilip formatlanabilir
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Schedule, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(12.dp))
                    Text(text = "${slot.startTime} - ${slot.endTime}", fontSize = 14.sp, color = Color.DarkGray)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(12.dp))
                    Text(text = serviceName, fontSize = 14.sp, color = Color.DarkGray)
                }

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF48C9B0)),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text("Onayla", color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        border = BorderStroke(1.dp, Color.LightGray),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text("Geri Dön", color = Color.Gray)
                    }
                }
            }
        }
    }
}