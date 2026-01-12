package com.example.studentcenterapp.ui.student

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.studentcenterapp.R
import com.example.studentcenterapp.ui.common.ContentCard
import com.example.studentcenterapp.ui.common.PrimaryButton
import com.example.studentcenterapp.ui.theme.DarkText
import com.example.studentcenterapp.ui.theme.PrimaryBlue

@Composable
fun PasswordResetSuccessScreen(
    onLoginClick: () -> Unit // Giriş Yap butonuna basıldığında çağrılacak lambda
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBlue) // Ana arka plan mavi
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp)) // Üstten boşluk

            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo_oldx),
                contentDescription = null,
                modifier = Modifier.size(width = 94.dp, height = 64.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            ContentCard(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 40.dp), // Yazının sığması için padding'i biraz azalttım (80 çok dardı)
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.height(40.dp))

                    // Yeşil Başarı İkonu
                    Image(
                        painter = painterResource(id = R.drawable.yes_il_tik),
                        contentDescription = "Başarılı İşlem",
                        modifier = Modifier.size(180.dp)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "Şifre sıfırlama bağlantısı e-posta adresinize gönderilmiştir.",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = DarkText
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Lütfen gelen kutunuzu kontrol ediniz.",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = DarkText.copy(alpha = 0.7f) // Biraz daha silik bir alt metin
                    )

                    Spacer(modifier = Modifier.height(60.dp))

                    // Giriş Yap Butonu
                    PrimaryButton(
                        text = "Giriş Ekranına Dön",
                        onClick = onLoginClick,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(50.dp))
                }
            }
        }
    }
}