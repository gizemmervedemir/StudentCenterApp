package com.example.studentcenterapp.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.studentcenterapp.R
import com.example.studentcenterapp.ui.common.ContentCard
import com.example.studentcenterapp.ui.common.PrimaryButton
import com.example.studentcenterapp.ui.student.SignupTextField
import com.example.studentcenterapp.ui.theme.ErrorRed
import com.example.studentcenterapp.ui.theme.PrimaryBlue
import com.example.studentcenterapp.viewmodel.profile.ProfileViewModel

@Composable
fun PasswordUpdateScreen(
    profileVm: ProfileViewModel,
    onBackClick: () -> Unit,
    onSuccessNavigate: () -> Unit
) {
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().background(PrimaryBlue)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                painter = painterResource(id = R.drawable.logo_oldx),
                contentDescription = null,
                modifier = Modifier.size(94.dp, 64.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            ContentCard(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 27.dp, vertical = 34.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        IconButton(onClick = onBackClick, modifier = Modifier.align(Alignment.TopStart)) {
                            Icon(
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = "Geri",
                                tint = Color(0xFF707070)
                            )
                        }
                    }

                    Text(
                        "Parola Güncelle",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Yeni Şifre Oluştur",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    SignupTextField(
                        value = oldPassword,
                        onValueChange = { oldPassword = it; error = null },
                        label = "Eski Şifre",
                        isPassword = true
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    SignupTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it; error = null },
                        label = "Şifre",
                        isPassword = true
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    SignupTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it; error = null },
                        label = "Şifre (tekrar)",
                        isPassword = true
                    )

                    if (newPassword != confirmPassword && confirmPassword.isNotEmpty()) {
                        Text(
                            "Şifreler uyuşmuyor",
                            color = ErrorRed,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                        )
                    }

                    error?.let {
                        Text(
                            it,
                            color = ErrorRed,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    PrimaryButton(
                        text = if (loading) "Güncelleniyor..." else "Onayla",
                        onClick = {
                            if (oldPassword.isBlank() || newPassword.isBlank()) {
                                error = "Lütfen tüm alanları doldurun."
                                return@PrimaryButton
                            }
                            if (newPassword != confirmPassword) {
                                error = "Şifreler birbiriyle eşleşmiyor."
                                return@PrimaryButton
                            }
                            if (newPassword.length < 6) {
                                error = "Yeni şifre en az 6 karakter olmalıdır."
                                return@PrimaryButton
                            }

                            loading = true
                            profileVm.updatePassword(oldPassword, newPassword) { success, msg ->
                                loading = false
                                if (success) {
                                    onSuccessNavigate()
                                } else {
                                    error = msg
                                }
                            }
                        },
                        modifier = Modifier.padding(bottom = 20.dp),
                        enabled = !loading
                    )
                }
            }
        }
    }
}