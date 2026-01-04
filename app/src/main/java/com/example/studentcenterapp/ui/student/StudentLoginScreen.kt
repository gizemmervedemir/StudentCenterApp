package com.example.studentcenterapp.ui.student

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.studentcenterapp.ui.theme.PrimaryBlue
import com.example.studentcenterapp.viewmodel.student.StudentLoginViewModel
import com.example.studentcenterapp.R
import com.example.studentcenterapp.ui.common.ContentCard
import com.example.studentcenterapp.ui.common.PrimaryButton
import com.example.studentcenterapp.ui.theme.ErrorRed
import com.example.studentcenterapp.ui.theme.PrimaryGreen
// Renkleri kullanabilmek için Color importu
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.PlatformTextStyle
import com.example.studentcenterapp.ui.theme.lightText

@Composable
fun StudentLoginScreen(
    viewModel: StudentLoginViewModel,
    onSignupClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    // Mavi arka plan
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBlue) // Temandaki mavi renk
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo Alanı
            Spacer(modifier = Modifier.height(83.dp))
            Image(
                painter = painterResource(id = R.drawable.logo_oldx), // Logo dosyan
                contentDescription = "Logo",
                modifier = Modifier
                    .width(173.dp)  // Figma genişlik (W)
                    .height(118.dp), // Figma yükseklik (H)
                contentScale = ContentScale.Fit // Logonun ezilmeden kutuya sığmasını sağlar
            )
            Spacer(modifier = Modifier.height(27.dp))

            // Beyaz Kart (ContentCard)
            ContentCard(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 34.dp),
//                shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp) // Sadece üst köşeler yuvarlak
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 30.dp, vertical = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Giriş Yapın",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // E-posta Input
                    OutlinedTextField(
                        value = viewModel.email,
                        onValueChange = { viewModel.email = it },
                        label = { Text("E-posta", style = MaterialTheme.typography.labelMedium.copy(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false // Gereksiz alt boşlukları siler
                            )
                        ),
                            modifier = Modifier.offset(y = (-5).dp) )}, // Negatif değer metni yukarı çeker)  },
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

                    // Şifre Input
                    OutlinedTextField(
                        value = viewModel.password,
                        onValueChange = { viewModel.password = it },
                        label = { Text("Şifre", style = MaterialTheme.typography.labelMedium.copy(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false // Gereksiz alt boşlukları siler
                            )
                        ),
                            modifier = Modifier.offset(y = (-5).dp) )},// Negatif değer metni yukarı çeker) },
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

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp), // Kutudan biraz mesafe bırakmak için
                        horizontalArrangement = Arrangement.SpaceBetween, // Birini sola, diğerini sağa yaslar
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Sol Tarafa Yaslı Hata Mesajı
                        Box(modifier = Modifier.weight(1f)) { // Mesaj uzun olsa bile alanı korumak için
                            if (viewModel.errorMessage != null) {
                                Text(
                                    text = viewModel.errorMessage!!,
                                    color = ErrorRed,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }

                        // Sağ Tarafa Yaslı Şifremi Unuttum
                        TextButton(
                            onClick = onForgotPasswordClick,
                            contentPadding = PaddingValues(0.dp) // Metni tam köşeye yanaştırmak için
                        ) {
                            Text(
                                text = "Şifremi Unuttum",
                                color = PrimaryBlue,
                                modifier = Modifier
                                    .clickable { onForgotPasswordClick() },
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(64.dp))

                    // Giriş Yap Butonu
                    PrimaryButton(
                        text = "Giriş Yap",
                        onClick = { viewModel.onLoginClick(onLoginSuccess) },
//                        isLoading = viewModel.isLoading,
                        modifier = Modifier
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Hesabınız yok mu?", color = lightText, style = MaterialTheme.typography.labelMedium)
                    Spacer(modifier = Modifier.height(6.dp))
                    // Üye Ol Butonu (Yeşil buton)
                    PrimaryButton( // Temandaki yeşil renkli buton
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