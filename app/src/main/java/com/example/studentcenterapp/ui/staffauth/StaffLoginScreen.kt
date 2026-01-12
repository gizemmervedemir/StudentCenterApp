package com.example.studentcenterapp.ui.staffauth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.studentcenterapp.R
import com.example.studentcenterapp.ui.common.ContentCard
import com.example.studentcenterapp.ui.common.PrimaryButton
import com.example.studentcenterapp.ui.state.UiState
import com.example.studentcenterapp.ui.theme.*

@Composable
fun StaffLoginScreen(
    vm: StaffLoginViewModel,
    onSignupClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onSuccess: (staffId: String) -> Unit
) {
    val email by vm.email.collectAsState()
    val password by vm.password.collectAsState()
    val state by vm.uiState.collectAsState()
    val navStaffId by vm.navStaffId.collectAsState()

    // Başarılı giriş durumunda yönlendirme tetikleyici
    navStaffId?.let { id ->
        vm.consumeNav()
        onSuccess(id)
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
            // Logo Alanı
            Spacer(modifier = Modifier.height(83.dp))
            Image(
                painter = painterResource(id = R.drawable.logo_oldx),
                contentDescription = "Logo",
                modifier = Modifier
                    .width(173.dp)
                    .height(118.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(27.dp))

            // Beyaz Giriş Kartı
            ContentCard(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 34.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 30.dp, vertical = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Personel Girişi",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // E-posta Alanı
                    OutlinedTextField(
                        value = email,
                        onValueChange = vm::onEmailChange,
                        label = {
                            Text(
                                "E-posta",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                                ),
                                modifier = Modifier.offset(y = (-5).dp)
                            )
                        },
                        modifier = Modifier.fillMaxWidth().height(64.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = PrimaryBlue,
                            unfocusedIndicatorColor = lightText,
                            focusedLabelColor = PrimaryBlue,
                            unfocusedLabelColor = lightText
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Şifre Alanı
                    OutlinedTextField(
                        value = password,
                        onValueChange = vm::onPasswordChange,
                        label = {
                            Text(
                                "Şifre",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                                ),
                                modifier = Modifier.offset(y = (-5).dp)
                            )
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth().height(64.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = PrimaryBlue,
                            unfocusedIndicatorColor = lightText,
                            focusedLabelColor = PrimaryBlue,
                            unfocusedLabelColor = lightText
                        )
                    )

                    // Hata ve Şifremi Unuttum Satırı
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            if (state is UiState.Error) {
                                Text(
                                    text = (state as UiState.Error).message,
                                    color = ErrorRed,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }

                        TextButton(
                            onClick = onForgotPasswordClick,
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = "Şifremi Unuttum",
                                color = PrimaryBlue,
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(64.dp))

                    // Giriş Yap Butonu (Mavi)
                    PrimaryButton(
                        text = if (state is UiState.Loading) "Giriş Yapılıyor..." else "Giriş Yap",
                        enabled = state !is UiState.Loading,
                        onClick = vm::login,
                        modifier = Modifier
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Hesabınız yok mu?",
                        color = lightText,
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    // Üye Ol Butonu (Yeşil)
                    PrimaryButton(
                        text = "Üye Ol",
                        onClick = onSignupClick,
                        modifier = Modifier,
                        containerColor = PrimaryGreen
                    )
                }
            }
        }
    }
}