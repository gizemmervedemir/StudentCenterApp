package com.example.studentcenterapp.ui.student

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studentcenterapp.R
import com.example.studentcenterapp.ui.common.ContentCard
import com.example.studentcenterapp.ui.common.PrimaryButton
import com.example.studentcenterapp.ui.theme.DarkText
import com.example.studentcenterapp.ui.theme.ErrorRed
import com.example.studentcenterapp.ui.theme.PrimaryBlue
import com.example.studentcenterapp.ui.theme.lightText
import com.example.studentcenterapp.viewmodel.student.ForgotPasswordViewModel

@Composable
fun ForgotPasswordEmailScreen(
    viewModel: ForgotPasswordViewModel,
    onCodeSent: () -> Unit,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBlue)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Üst Kısım: Geri Butonu ve Logo Alanı
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(horizontal = 20.dp)
            ) {

                Spacer(modifier = Modifier.height(20.dp))
                // Buraya VA logosunu ekleyebilirsin
                Image(
                    painter = painterResource(id = R.drawable.logo_oldx),
                    contentDescription = "Logo",
                    modifier = Modifier.size(width = 94.dp, height = 64.dp).align(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            ContentCard(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp), // İçeriden genel boşluk
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Üst Kısım: Geri Oku ve Başlığı Hizalayan Box
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Geri",
                            tint = Color(0xFF707070),
                            modifier = Modifier
                                .align(Alignment.TopStart) // Figma'daki gibi sol üste yasla
                                .clickable { onBackClick() }
                                .size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(150.dp)) // Ok ile başlık arasındaki dikey boşluk

                    Text(
                        text = "Şifremi Sıfırla",
                        style = MaterialTheme.typography.titleMedium,
                        color = DarkText
                    )

                    Spacer(modifier = Modifier.height(48.dp))

                    // Figma'daki altı çizgili TextField tasarımı
                    ForgotPasswordTextField(
                        value = viewModel.email,
                        onValueChange = {
                            viewModel.email = it
                            viewModel.emailError = false
                        },
                        label = "E-posta",
                        isError = viewModel.emailError
                    )

                    // Hata Mesajı
                    if (viewModel.errorMessage != null) {
                        Text(
                            text = viewModel.errorMessage!!,
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(100.dp))

                    PrimaryButton(
                        text = "Gönder",
                        onClick = { viewModel.sendResetCode(onCodeSent) },
                        modifier = Modifier
                    )
                }
            }
        }
    }
}
@Composable
fun ForgotPasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean = false
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(
                    label,
                    style = MaterialTheme.typography.labelMedium, color = lightText
                )
            },
            leadingIcon = {
                // Figma'daki kişi ikonu
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.Gray
                )
            },
            // Arka planı şeffaf yapıp sadece alt çizgiyi bırakıyoruz
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Red,
                cursorColor = PrimaryBlue,
                focusedLabelColor = PrimaryBlue,     // Tıklayınca yukarı çıkan yazının rengi
                unfocusedLabelColor = lightText,    // Kutunun içindeyken duran yazının rengi
                errorLabelColor = ErrorRed,
            ),
            singleLine = true,
            isError = isError
        )
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth(0.9f) // Çizginin genişliğini buradan ayarla (%90 gibi)
                .padding(
                    start = 50.dp,
                    end = 10.dp
                ), // Çizgiyi sağa yasla (Böylece sol taraftaki boşluk korunur)
            thickness = 3.dp,
            color = if (isError) Color.Red else Color.LightGray
        )
    }
}