package com.example.studentcenterapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Schedule
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun TimeSlotSelectionScreen(
    serviceName: String,
    appointmentType: String = "office", // Varsayılan olarak "office", navigasyondan da gelebilir
    viewModel: TimeSlotCalendarViewModel,
    currentRoute: String?,
    onTabSelected: (AppTab) -> Unit,
    onBackClick: () -> Unit,
    onAppointmentCreated: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var showConfirmDialog by remember { mutableStateOf(false) }

    // Seçili slotu state içinden buluyoruz
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
            // Üst Navigasyon (Geri Butonu)
            IconButton(onClick = onBackClick, modifier = Modifier.padding(8.dp)) {
                Icon(
                    imageVector = Icons.Default.ChevronLeft,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }

            // İçerik Kartı
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

                    // Onaylama Butonu
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

    // ✅ Onay Dialogu ve Firebase Kaydı
    if (showConfirmDialog && selectedSlot != null) {
        AppointmentConfirmDialog(
            serviceName = serviceName,
            slot = selectedSlot,
            onDismiss = { showConfirmDialog = false },
            onConfirm = {
                // ViewModel artık bu parametreleri bekliyor
                viewModel.confirmSelectedSlot(
                    serviceName = serviceName,
                    type = appointmentType,
                    onSuccess = {
                        showConfirmDialog = false
                        onAppointmentCreated() // Success ekranına yönlendirir
                    },
                    onError = { /* Hata yönetimi burada yapılabilir */ }
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
    val dateText = try {
        val date = LocalDate.parse(slot.date)
        date.format(DateTimeFormatter.ofPattern("dd MMMM, EEEE", Locale("tr")))
    } catch (e: Exception) {
        slot.date
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = Color(0xFFD1D1D1),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp).fillMaxWidth()) {
                Text(
                    text = dateText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Schedule, contentDescription = null, tint = Color(0xFF333333))
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = slot.startTime, fontSize = 18.sp, color = Color(0xFF333333))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.Top) {
                    Icon(Icons.Outlined.CalendarMonth, contentDescription = null, tint = Color(0xFF333333))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(text = "Uzman Personel", fontWeight = FontWeight.Medium)
                        Text(text = serviceName, fontSize = 14.sp, color = Color(0xFF555555))
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier.weight(1.1f).height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF33C29D)),
                        shape = RoundedCornerShape(25.dp)
                    ) {
                        Text("Onayla", color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(0.9f).height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEBEBEB)),
                        shape = RoundedCornerShape(25.dp)
                    ) {
                        Text("Geri Dön", color = Color(0xFF666666), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}