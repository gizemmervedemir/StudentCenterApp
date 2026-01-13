package com.example.studentcenterapp.ui.staff

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.studentcenterapp.model.Appointment
import com.example.studentcenterapp.ui.common.AppBottomBar
import com.example.studentcenterapp.ui.common.AppTopBar
import com.example.studentcenterapp.ui.common.staffBottomTabs
import com.example.studentcenterapp.viewmodel.staff.StaffCalendarViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaffCalendarScreen(
    viewModel: StaffCalendarViewModel,
    currentRoute: String?,
    onTabSelected: (String) -> Unit,
    onApprove: (Appointment) -> Unit,
    onReject: (Appointment) -> Unit
) {
    val selectedDate by viewModel.selectedDate.collectAsState()
    val appointments by viewModel.filteredAppointments.collectAsState()

    Scaffold(
        topBar = { AppTopBar(title = "Takvim") }, // Senin Top Barın
        bottomBar = {
            AppBottomBar(
                tabs = staffBottomTabs,
                currentRoute = currentRoute,
                onTabSelected = { tab -> onTabSelected(tab.route) }
            )
        } // Senin Bottom Barın
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF2F2F2))
        ) {
            // --- TAKVİM KARTI ---
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(25.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8E8E8)),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    // Ay İsmi ve İleri/Geri Okları
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { viewModel.onDateSelected(selectedDate.minusMonths(1)) }) {
                            Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Geri", tint = Color.Black)
                        }

                        Text(
                            text = getSelectedMonthName(selectedDate),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        )

                        IconButton(onClick = { viewModel.onDateSelected(selectedDate.plusMonths(1)) }) {
                            Icon(Icons.Default.KeyboardArrowRight, contentDescription = "İleri", tint = Color.Black)
                        }
                    }

                    // Takvim Görünümü
                    AndroidView(
                        modifier = Modifier.fillMaxWidth().height(260.dp),
                        factory = { context ->
                            android.widget.CalendarView(context).apply {
                                setOnDateChangeListener { _, year, month, dayOfMonth ->
                                    viewModel.onDateSelected(LocalDate.of(year, month + 1, dayOfMonth))
                                }
                            }
                        },
                        update = { view ->
                            // Oklarla ay değiştiğinde takvimi güncelle
                            val timeInMilli = selectedDate.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
                            if (view.date != timeInMilli) view.date = timeInMilli
                        }
                    )
                }
            }

            // Seçili Gün Başlığı
            Text(
                text = "${selectedDate.dayOfMonth} ${getSelectedMonthName(selectedDate)}, ${getSelectedDayName(selectedDate)}",
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            // --- RANDEVU LİSTESİ ---
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(appointments) { app ->
                    StaffAppointmentCalendarItem(
                        app = app,
                        onApprove = { onApprove(app) },
                        onReject = { onReject(app) }
                    )
                }
            }
        }
    }
}

@Composable
fun StaffAppointmentCalendarItem(
    app: Appointment,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E0E0))
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ... (Profil ikonu ve isim kısımları aynı) ...
            Column(modifier = Modifier.weight(1f)) {
                Text(text = app.studentName, fontWeight = FontWeight.Bold, color = Color.Black)
                Text(text = app.startTime, color = Color.DarkGray, fontSize = 13.sp)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // MANTIK: Sadece 'pending' ise onay butonu göster
                if (app.status == "pending") {
                    IconButton(onClick = onApprove) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Onayla",
                            tint = Color.Black
                        )
                    }
                }

                // MANTIK: 'pending' veya 'approved' ise iptal (X) butonu HER ZAMAN gösterilir
                // Bu buton randevuyu reddetmek veya onaylanmış randevuyu iptal etmek için kullanılır.
                IconButton(onClick = onReject) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = "Reddet/İptal Et",
                        tint = Color.Black
                    )
                }

                Icon(Icons.Default.KeyboardArrowDown, null, tint = Color.Black)
            }
        }
    }
}
private fun getSelectedMonthName(date: LocalDate): String {
    return when(date.monthValue) {
        1 -> "Ocak" 2 -> "Şubat" 3 -> "Mart" 4 -> "Nisan"
        5 -> "Mayıs" 6 -> "Haziran" 7 -> "Temmuz" 8 -> "Ağustos"
        9 -> "Eylül" 10 -> "Ekim" 11 -> "Kasım" 12 -> "Aralık"
        else -> date.month.name
    }
}

private fun getSelectedDayName(date: LocalDate): String {
    return when(date.dayOfWeek.value) {
        1 -> "Pazartesi" 2 -> "Salı" 3 -> "Çarşamba" 4 -> "Perşembe"
        5 -> "Cuma" 6 -> "Cumartesi" 7 -> "Pazar"
        else -> "Gün"
    }
}