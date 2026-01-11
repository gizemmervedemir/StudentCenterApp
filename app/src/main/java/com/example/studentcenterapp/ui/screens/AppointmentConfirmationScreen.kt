package com.example.studentcenterapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studentcenterapp.ui.common.AppTopBar
import com.example.studentcenterapp.ui.common.ErrorView
import com.example.studentcenterapp.ui.common.LoadingView
import com.example.studentcenterapp.ui.common.PrimaryButton
import com.example.studentcenterapp.ui.confirmation.AppointmentConfirmationViewModel
import com.example.studentcenterapp.ui.confirmation.AppointmentConfirmationViewModelFactory
import com.example.studentcenterapp.ui.confirmation.AppointmentSummaryCard

@Composable
fun AppointmentConfirmationScreen(
    studentId: String,
    departmentName: String,
    serviceId: String,
    serviceName: String,
    timeSlotId: String,
    scheduledStartMillis: Long,
    dateTimeText: String,
    factory: AppointmentConfirmationViewModelFactory,
    onBack: () -> Unit,
    onSuccessNavigate: () -> Unit
) {
    val vm: AppointmentConfirmationViewModel = viewModel(factory = factory)
    val state by vm.uiState.collectAsState()

    // selection bilgilerini 1 kere set et
    LaunchedEffect(studentId, serviceId, timeSlotId, scheduledStartMillis) {
        vm.setSelection(
            studentId = studentId,
            departmentName = departmentName,
            serviceId = serviceId,
            serviceName = serviceName,
            timeSlotId = timeSlotId,
            scheduledStartMillis = scheduledStartMillis,
            dateTimeText = dateTimeText
        )
    }

    // success -> navigate (1 kere)
    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onSuccessNavigate()
            vm.consumeSuccess()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // ✅ AppTopBar sadece logo gösteriyor, geri butonu yok
        AppTopBar(title = "Randevu Onayı")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            val serviceText = state.serviceName.ifBlank { state.serviceId }

            AppointmentSummaryCard(
                departmentName = state.departmentName,
                serviceText = serviceText,
                dateTimeText = state.dateTimeText
            )

            Spacer(Modifier.height(16.dp))

            if (state.isLoading) {
                LoadingView(modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(10.dp))
            }

            if (state.errorMessage != null) {
                ErrorView(
                    message = state.errorMessage ?: "",
                    onRetry = { vm.clearError() }
                )
                Spacer(Modifier.height(10.dp))
            }

            PrimaryButton(
                text = if (state.isLoading) "Oluşturuluyor..." else "Onayla",
                enabled = !state.isLoading,
                onClick = { vm.confirm() }
            )

            Spacer(Modifier.height(10.dp))

            // ✅ Geri/iptal işlemi zaten burada
            PrimaryButton(
                text = "İptal",
                enabled = !state.isLoading,
                onClick = onBack
            )
        }
    }
}
