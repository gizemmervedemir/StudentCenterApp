package com.example.studentcenterapp.ui.profile

import android.graphics.drawable.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studentcenterapp.data.student.StudentSession
import com.example.studentcenterapp.ui.common.ContentCard
import com.example.studentcenterapp.ui.common.PrimaryButton
import com.example.studentcenterapp.ui.student.SignupTextField
import com.example.studentcenterapp.ui.theme.Figtree
import com.example.studentcenterapp.ui.theme.PrimaryBlue
import java.util.Calendar
import com.example.studentcenterapp.R

@Composable
fun PersonalInfoScreen(
    profileVm: com.example.studentcenterapp.viewmodel.profile.ProfileViewModel,
    onBackClick: () -> Unit,
    onUpdateSuccessNavigate: () -> Unit,
) {
    var fullName by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var day by remember { mutableStateOf("") }
    var month by remember { mutableStateOf("") }

    var showDatePicker by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()

    // Takvim Dialogu
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val calendar = Calendar.getInstance().apply { timeInMillis = millis }
                        day = calendar.get(Calendar.DAY_OF_MONTH).toString()
                        month = (calendar.get(Calendar.MONTH) + 1).toString()
                        year = calendar.get(Calendar.YEAR).toString()
                    }
                    showDatePicker = false
                }) { Text("Tamam") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("İptal") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBlue)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo_oldx),
                contentDescription = "Logo",
                modifier = Modifier.size(width = 94.dp, height = 64.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            ContentCard(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 27.dp, vertical = 34.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header (Geri Butonu ve Başlık)
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Geri",
                            tint = Color(0xFF707070),
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .clickable { onBackClick() }
                                .size(24.dp)
                        )

                        Text(
                            text = "Kişisel Bilgiler",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    // Ad Soyad Field
                    SignupTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = "Ad Soyad"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Doğum Tarihi Seçici (Tıklanabilir Row)
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            SignupTextField(
                                value = year,
                                onValueChange = {},
                                label = "Doğum Yılı",
                                readOnly = true,
                                enabled = false,
                                modifier = Modifier.weight(2f)
                            )
                            SignupTextField(
                                value = day,
                                onValueChange = {},
                                label = "Gün",
                                readOnly = true,
                                enabled = false,
                                modifier = Modifier.weight(1f)
                            )
                            SignupTextField(
                                value = month,
                                onValueChange = {},
                                label = "Ay",
                                readOnly = true,
                                enabled = false,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        // Görünmez katman takvimi açar
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable { showDatePicker = true }
                        )
                    }

                    if (error != null) {
                        Text(
                            text = error!!,
                            color = Color.Red,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(top = 8.dp).fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(50.dp))

                    // Güncelle Butonu (PrimaryButton kullanarak)
                    PrimaryButton(
                        text = if (loading) "Güncelleniyor..." else "Güncelle",
                        onClick = {
                            error = null
                            val id = StudentSession.currentStudentId

                            if (id.isBlank()) {
                                error = "Oturum bulunamadı."
                                return@PrimaryButton
                            }
                            if (fullName.isBlank() || year.isBlank()) {
                                error = "Lütfen alanları doldurun."
                                return@PrimaryButton
                            }

                            loading = true
                            profileVm.updatePersonalInfo(
                                id = id,
                                fullName = fullName,
                                birthDay = day,
                                birthMonth = month,
                                birthYear = year
                            ) { ok ->
                                loading = false
                                if (ok) onUpdateSuccessNavigate()
                                else error = "Güncelleme başarısız."
                            }
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileFormTextField(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    )
}