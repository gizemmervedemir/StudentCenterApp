package com.example.studentcenterapp.ui.staffauth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.studentcenterapp.R
import com.example.studentcenterapp.ui.common.ContentCard
import com.example.studentcenterapp.ui.common.PrimaryButton
import com.example.studentcenterapp.ui.student.SignupTextField
import com.example.studentcenterapp.ui.theme.PrimaryBlue
import com.example.studentcenterapp.ui.state.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaffSignupScreen(
    vm: StaffSignupViewModel,
    onSuccess: () -> Unit,
    onBackClick: () -> Unit
) {
    val name by vm.name.collectAsState()
    val email by vm.email.collectAsState()
    val staffNumber by vm.staffNumber.collectAsState()
    val birthYear by vm.birthYear.collectAsState()
    val birthDay by vm.birthDay.collectAsState()
    val birthMonth by vm.birthMonth.collectAsState()
    val password by vm.password.collectAsState()
    val passwordConfirm by vm.passwordConfirm.collectAsState()
    val state by vm.uiState.collectAsState()

    // --- DatePicker Logic (Öğrenci ekranından uyarlandı) ---
    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    vm.updateDate(datePickerState.selectedDateMillis)
                    showDatePicker = false
                }) { Text("Tamam") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("İptal") }
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = Color.White,
                    titleContentColor = PrimaryBlue,
                    headlineContentColor = PrimaryBlue,
                    navigationContentColor = PrimaryBlue,
                    weekdayContentColor = PrimaryBlue,
                    subheadContentColor = PrimaryBlue,
                    selectedDayContainerColor = PrimaryBlue,
                    selectedDayContentColor = Color.White,
                    todayContentColor = PrimaryBlue,
                    todayDateBorderColor = PrimaryBlue
                )
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(PrimaryBlue)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

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
                            text = "Hesap Oluşturun",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    SignupTextField(value = email, onValueChange = vm::onEmailChange, label = "E-posta")
                    Spacer(modifier = Modifier.height(12.dp))

                    SignupTextField(value = name, onValueChange = vm::onNameChange, label = "Ad Soyad")
                    Spacer(modifier = Modifier.height(12.dp))

                    SignupTextField(
                        value = staffNumber,
                        onValueChange = vm::onStaffNumberChange,
                        label = "Personel Numarası",
                        keyboardType = KeyboardType.Number
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // --- Doğum Tarihi Row (DatePicker Tetikleyici) ---
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            SignupTextField(value = birthYear, onValueChange = {}, label = "Doğum Yılı", modifier = Modifier.weight(2f), readOnly = true, enabled = false)
                            SignupTextField(value = birthDay, onValueChange = {}, label = "Gün", modifier = Modifier.weight(1f), readOnly = true, enabled = false)
                            SignupTextField(value = birthMonth, onValueChange = {}, label = "Ay", modifier = Modifier.weight(1f), readOnly = true, enabled = false)
                        }
                        // Tıklamayı Yakalayan Görünmez Katman
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable { showDatePicker = true }
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    SignupTextField(value = password, onValueChange = vm::onPasswordChange, label = "Şifre", isPassword = true)
                    Spacer(modifier = Modifier.height(12.dp))
                    SignupTextField(value = passwordConfirm, onValueChange = vm::onPasswordConfirmChange, label = "Şifre (Tekrar)", isPassword = true)

                    if (state is UiState.Error) {
                        Text(text = (state as UiState.Error).message, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
                    }

                    Spacer(modifier = Modifier.height(50.dp))

                    PrimaryButton(
                        text = if (state is UiState.Loading) "Kayıt Yapılıyor..." else "Üye Ol",
                        onClick = { vm.signup { onSuccess() } },
                        modifier = Modifier
                    )
                }
            }
        }
    }
}