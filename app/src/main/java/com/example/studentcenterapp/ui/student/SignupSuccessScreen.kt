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
fun SignupSuccessScreen(
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
                painter = painterResource(id = R.drawable.logo_oldx), // Kendi logonun path'ini kullan
                contentDescription = null, // Erişilebilirlik için doldurulabilir
                modifier = Modifier.size(width = 94.dp, height = 64.dp)
            )

            Spacer(modifier = Modifier.height(20.dp)) // Logo ile kart arasına boşluk

            ContentCard(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 80.dp), // Yanlardan boşluk
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center // İçeriği dikeyde ortala
                ) {
                    Spacer(modifier = Modifier.height(80.dp))
                    // Yeşil Başarı İkonu (drawable klasörüne eklediğin ikon)
                    Image(
                        painter = painterResource(id = R.drawable.yes_il_tik),
                        contentDescription = "Başarılı Kayıt",
                        modifier = Modifier.size(180.dp) // İkon boyutu
                    )

                    Spacer(modifier = Modifier.height(32.dp)) // İkon ile yazı arasına boşluk

                    Text(
                        text = "Profiliniz başarıyla oluşturulmuştur", // İki satırlık yazı
                        style = MaterialTheme.typography.bodyLarge, // Büyük başlık stili
                        textAlign = TextAlign.Center, // Ortaya hizala
                        color = DarkText // Yazı rengi siyah
                    )

                    Spacer(modifier = Modifier.height(90.dp)) // Yazı ile buton arasına boşluk

                    // Giriş Yap Butonu
                    PrimaryButton(
                        text = "Giriş Yap",
                        onClick = onLoginClick, // ViewModel'den gelen lambda
                        modifier = Modifier
                    )

                    Spacer(modifier = Modifier.height(50.dp)) // Alt boşluk
                }
            }
        }
    }
}