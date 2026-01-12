package com.example.studentcenterapp.ui.student

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.studentcenterapp.R
import com.example.studentcenterapp.model.Appointment
import com.example.studentcenterapp.navigation.Screen
import com.example.studentcenterapp.ui.appointments.AppointmentListFilter
import com.example.studentcenterapp.ui.common.AppBottomBar
import com.example.studentcenterapp.ui.common.AppTopBar
import com.example.studentcenterapp.ui.common.bottomTabs
import com.example.studentcenterapp.ui.theme.ButtonStudentCalendar
import com.example.studentcenterapp.ui.theme.Figtree
import com.example.studentcenterapp.ui.theme.GreyBox
import com.example.studentcenterapp.ui.theme.PrimaryBlue
import com.example.studentcenterapp.ui.theme.SurfaceLight
import com.example.studentcenterapp.ui.theme.lightText
import com.example.studentcenterapp.viewmodel.appointment.AppointmentListViewModel

@Composable
fun StudentCalendarScreen(
    navController: NavHostController,
    viewModel: AppointmentListViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    var showCancelDialog by remember { mutableStateOf(false) }
    var appointmentToCancel by remember { mutableStateOf<Appointment?>(null) }
    val cancelSuccess by viewModel.cancelSuccess.collectAsState()

    LaunchedEffect(cancelSuccess) {
        if (cancelSuccess) {
            navController.navigate("cancelSuccess") {
                popUpTo(Screen.StudentCalendar.route) { inclusive = false }
            }
            viewModel.resetCancelSuccess()
        }
    }

    // Figma'daki arka plan rengi (Mavi)
    Box(modifier = Modifier.fillMaxSize().background(PrimaryBlue)) {
        Scaffold(
            topBar = { AppTopBar(title = "Takvim") },
            bottomBar = {
                AppBottomBar(
                    tabs = bottomTabs,
                    currentRoute = Screen.StudentCalendar.route,
                    onTabSelected = { tab ->
                        navController.navigate(tab.route) { launchSingleTop = true }
                    }
                )
            },
            containerColor = Color.Transparent
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    // Beyaz kartın üstten yuvarlatılması
                    .background(Color.White, RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // --- TAB SEÇENEKLERİ (Aktif / Geçmiş) ---
                // --- GÜNCELLENMİŞ FIGMA TAB YAPISI ---
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        // Dış çerçeve ve genel arka plan
                        .border(1.dp, ButtonStudentCalendar, RoundedCornerShape(25.dp))
                        .background(Color.Transparent) // Arka plan yok, sadece çerçeve
                        .padding(0.dp) // İçerik çerçeveye tam otursun
                ) {
                    val filters = listOf(AppointmentListFilter.UPCOMING, AppointmentListFilter.PAST)
                    val labels = listOf("Aktif\nRandevularım", "Geçmiş\nRandevularım")

                    filters.forEachIndexed { index, filter ->
                        val isSelected = uiState.selectedFilter == filter

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                // Seçili ise senin tanımladığın ButtonStudentCalendar rengiyle boya
                                .background(
                                    if (isSelected) ButtonStudentCalendar else Color.Transparent,
                                    RoundedCornerShape(25.dp) // Tam yuvarlak hap görünümü
                                )
                                .clickable { viewModel.setFilter(filter) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = labels[index],
                                textAlign = TextAlign.Center,
                                style = TextStyle(
                                    // Seçili ise SurfaceLight (Beyazımsı), değilse ButtonStudentCalendar (Açık Mavi)
                                    color = if (isSelected) SurfaceLight else ButtonStudentCalendar,
                                    fontSize = 13.sp,
                                    lineHeight = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = Figtree
                                )
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))

                // --- RANDEVU LİSTESİ ---
                if (uiState.isLoading) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Yükleniyor...")
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 20.dp)
                    ) {
                        val currentList = if (uiState.selectedFilter == AppointmentListFilter.UPCOMING) {
                            uiState.upcomingAppointments
                        } else {
                            uiState.pastAppointments
                        }

                        items(currentList) { appointment ->
                            AppointmentCard(
                                appointment = appointment,
                                onEditClick = { /* Düzenle navigasyonu */ },
                                onCancelClick = {
                                    appointmentToCancel = appointment
                                    showCancelDialog = true
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showCancelDialog && appointmentToCancel != null) {
        CancelConfirmationDialog(
            appointment = appointmentToCancel!!,
            onConfirm = {
                showCancelDialog = false
                viewModel.cancelAppointment(appointmentToCancel!!.id)
            },
            onDismiss = { showCancelDialog = false }
        )
    }
}