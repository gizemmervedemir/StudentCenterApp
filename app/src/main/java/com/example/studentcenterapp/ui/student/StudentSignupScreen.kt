package com.example.studentcenterapp.ui.student
import androidx.compose.material3.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.studentcenterapp.ui.theme.lightText
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studentcenterapp.R
import com.example.studentcenterapp.ui.common.ContentCard
import com.example.studentcenterapp.ui.common.PrimaryButton
import com.example.studentcenterapp.ui.theme.PrimaryBlue
import com.example.studentcenterapp.viewmodel.student.StudentSignupViewModel
import com.example.studentcenterapp.ui.theme.ErrorRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentSignupScreen(

    viewModel: StudentSignupViewModel,
    onSignupSuccess: () -> Unit,
    onBackClick: () -> Unit
) {
    // Takvim durumu kontrolü
    val datePickerState = rememberDatePickerState()

    // Takvim Penceresi (Dialog)
    if (viewModel.showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { viewModel.showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.updateDate(datePickerState.selectedDateMillis)
                    viewModel.showDatePicker = false
                }) { Text("Tamam") }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.showDatePicker = false }) { Text("İptal") }
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = Color.White,

                    // 2. Başlık Renkleri (Mavi)
                    titleContentColor = PrimaryBlue,     // "Select date" yazısı
                    headlineContentColor = PrimaryBlue,  // Seçilen tarih büyük yazı

                    // 3. Takvim Kontrolleri (Oklar ve Gün İsimleri)
                    navigationContentColor = PrimaryBlue, // Sağ/Sol okları
                    weekdayContentColor = PrimaryBlue,    // S, M, T... (Gün isimleri)
                    subheadContentColor = PrimaryBlue,    // Ay ve Yıl başlığı (December 2025)

                    // 4. Gün Seçimleri
                    dayContentColor = Color.Black,             // Normal günler
                    selectedDayContainerColor = PrimaryBlue,   // Seçili gün yuvarlağı
                    selectedDayContentColor = Color.White,     // Seçili gün rakamı

                    // 5. Bugünün İşareti
                    todayContentColor = PrimaryBlue,
                    todayDateBorderColor = PrimaryBlue,

                    // 6. Yıl Seçici Ekranı (Üstteki yıla tıklayınca açılan yer)
                    yearContentColor = Color.Black,
                    currentYearContentColor = PrimaryBlue,
                    selectedYearContainerColor = PrimaryBlue,
                    selectedYearContentColor = Color.White          )
            )
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

            // Logo (Login ile aynı boyutta)
            Image(
                painter = painterResource(id = R.drawable.logo_oldx),
                contentDescription = "Logo",
                modifier = Modifier.size(width = 94.dp, height = 64.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            ContentCard(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 27.dp, vertical = 34.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Geri",
                            tint = Color(0xFF707070),
                            modifier = Modifier
                                .align(Alignment.TopStart) // Sol üste yaslar
                                .clickable { onBackClick() } // Geri gitme fonksiyonu
                                .size(24.dp)
                        )


                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Hesap Oluşturun",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    Spacer(modifier = Modifier.height(30.dp))

                    // E-posta
                    SignupTextField(
                        value = viewModel.email,
                        onValueChange = { viewModel.email = it
                            viewModel.emailError = false},
                        label = "E-posta",
                        isError = viewModel.emailError
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Ad Soyad
                    SignupTextField(
                        value = viewModel.fullName,
                        onValueChange = { viewModel.fullName = it
                            viewModel.fullNameError = false},
                        label = "Ad Soyad",
                        isError = viewModel.fullNameError
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Okul Numarası
                    SignupTextField(
                        value = viewModel.schoolNumber,
                        onValueChange = { viewModel.schoolNumber = it
                            viewModel.schoolNumberError = false},
                        label = "Okul Numarası",
                        isError = viewModel.schoolNumberError,
                        keyboardType = KeyboardType.Number
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(modifier = Modifier.fillMaxWidth()) {
                        // 1. Görsel Kutular (Row)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            SignupTextField(
                                value = viewModel.birthYear,
                                onValueChange = { viewModel.birthDateError = false
                                    viewModel.birthDateError = false},
                                label = " Doğum Yılı",
                                readOnly = true,
                                enabled = false, // Klavyeyi engellemek için false kalsın
                                modifier = Modifier.weight(2f),
                                isError = viewModel.birthDateError,
                            )
                            SignupTextField(
                                value = viewModel.birthDay,
                                onValueChange = {viewModel.birthDateError = false
                                    viewModel.birthDateError = false},
                                label = "Gün",
                                readOnly = true,
                                enabled = false,
                                modifier = Modifier.weight(1f),
                                isError = viewModel.birthDateError,
                            )
                            SignupTextField(
                                value = viewModel.birthMonth,
                                onValueChange = {viewModel.birthDateError = false
                                    viewModel.birthDateError = false},
                                label = "Ay",
                                readOnly = true,
                                enabled = false,
                                modifier = Modifier.weight(1f),
                                isError = viewModel.birthDateError,
                            )
                        }

                        // 2. Tıklamayı Yakalayan Şeffaf Katman (Görünmez ama orada!)
                        Box(
                            modifier = Modifier
                                .matchParentSize() // Kutuların tam üzerini kaplar
                                .background(Color.Transparent)
                                .clickable { viewModel.showDatePicker = true } // İşte sihir burada!
                        )
                    }
                        Spacer(modifier = Modifier.height(12.dp))

                        // Şifre
                        SignupTextField(
                            value = viewModel.password,
                            onValueChange = { viewModel.password = it
                                viewModel.passwordError = false },
                            label = "Şifre",
                            isPassword = true,
                            isError = viewModel.passwordError
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Şifre Tekrar
                        SignupTextField(
                            value = viewModel.passwordConfirm,
                            onValueChange = { viewModel.passwordConfirm = it
                                viewModel.passwordError = false},
                            label = "Şifre (Tekrar)",
                            isPassword = true,
                            isError = viewModel.passwordError
                        )

                    if (viewModel.errorMessage != null) {
                        Text(
                            text = viewModel.errorMessage!!,
                            color = Color.Red, // Tasarımındaki ErrorRed rengi
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .fillMaxWidth()
                        )
                    }
                        Spacer(modifier = Modifier.height(50.dp))

                        // Üye Ol Butonu (Mavi)
                        PrimaryButton(
                            text = "Üye Ol",
                            onClick = { viewModel.onSignupClick(onSignupSuccess) },
                            modifier = Modifier
                        )
                    }
                }
            }
        }
    }


// Signup ekranı için özelleştirilmiş TextField bileşeni (Kod tekrarını önlemek için)
@Composable
fun SignupTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        readOnly = readOnly,
        isError = isError,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        textStyle = MaterialTheme.typography.labelMedium.copy(
            fontSize = 18.sp, // Yazı boyutunu 14sp yaparak kutuya sığdırıyoruz
            platformStyle = PlatformTextStyle(includeFontPadding = false) // Gereksiz boşlukları siler
        ),
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium.copy(
                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                ),
                modifier = Modifier.offset(y = (-4).dp)
            )
        },
        modifier = modifier.fillMaxWidth().height(60.dp),
        shape = RoundedCornerShape(12.dp),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = PrimaryBlue,
            unfocusedIndicatorColor = lightText,
            focusedLabelColor = PrimaryBlue,
            unfocusedLabelColor = lightText,
            errorIndicatorColor = ErrorRed, // Hata durumunda kenarlık kırmızı olur
            errorLabelColor = ErrorRed
        )
    )
}

