package com.example.studentcenterapp.ui.student

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
fun CancelSuccessScreen(
    onNavigateBack: () -> Unit // "Randevularım" butonuna basınca çalışacak
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBlue) // Signup ile aynı mavi arka plan
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Logo (Signup ile aynı hizada)
            Image(
                painter = painterResource(id = R.drawable.logo_oldx),
                contentDescription = null,
                modifier = Modifier.size(width = 94.dp, height = 64.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Beyaz Kart Yapısı
            ContentCard(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 40.dp), // padding'i biraz azalttım metin sığsın diye
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Yeşil Tik İkonu (Signup ile aynı)
                    Image(
                        painter = painterResource(id = R.drawable.yes_il_tik),
                        contentDescription = "Başarılı İptal",
                        modifier = Modifier.size(180.dp)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "Randevunuz başarıyla iptal edilmiştir",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = DarkText
                    )

                    Spacer(modifier = Modifier.height(90.dp))

                    // Senin ortak butonun: PrimaryButton
                    PrimaryButton(
                        text = "Randevularım",
                        onClick = onNavigateBack,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(50.dp))
                }
            }
        }
    }
}