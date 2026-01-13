package com.example.studentcenterapp.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.studentcenterapp.R
import com.example.studentcenterapp.ui.common.ContentCard
import com.example.studentcenterapp.ui.common.PrimaryButton
import com.example.studentcenterapp.ui.theme.DarkText
import com.example.studentcenterapp.ui.theme.PrimaryBlue

@Composable
fun UpdateSuccessScreen(
    onProfileClick: () -> Unit // Profil butonuna basıldığında çağrılacak lambda
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

            // Logo (Tüm sayfalarda aynı boyutta)
            Image(
                painter = painterResource(id = R.drawable.logo_oldx),
                contentDescription = "Logo",
                modifier = Modifier.size(width = 94.dp, height = 64.dp)
            )

            Spacer(modifier = Modifier.height(20.dp)) // Logo ile kart arası

            ContentCard(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 40.dp), // Yanlardan boşluk
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center // İçeriği ortala
                ) {
                    // Yeşil Başarı İkonu
                    Image(
                        painter = painterResource(id = R.drawable.yes_il_tik),
                        contentDescription = "Başarılı Güncelleme",
                        modifier = Modifier.size(180.dp)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "Bilgileriniz başarıyla güncellenmiştir",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = DarkText
                    )

                    Spacer(modifier = Modifier.height(60.dp))

                    // Profilime Dön Butonu
                    PrimaryButton(
                        text = "Profil",
                        onClick = onProfileClick,
                    )

                    Spacer(modifier = Modifier.height(50.dp))
                }
            }
        }
    }
}