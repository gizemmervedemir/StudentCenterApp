package com.example.studentcenterapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studentcenterapp.R
import com.example.studentcenterapp.ui.theme.PrimaryBlue

@Composable
fun AppointmentSuccessScreen(
    onNavigateToAppointments: () -> Unit
) {
    // Görseldeki gibi arka plan rengi ve genel yapı
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF4FC3F7)) // Üstteki mavi alan
    ) {
        // Üst Logo Alanı (Görseldeki VA logosu)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.3f),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.logo_oldx), // Projendeki logo
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(80.dp)
            )
        }

        // Beyaz Gövde Alanı
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.7f),
            color = Color.White,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Yeşil Onay İkonu (Görseldeki yıldız benzeri çerçeve içindeki check)
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(150.dp)
                ) {
                    // Arka plandaki yeşil şekil (Yıldız/Mücevher formu için özel çizim veya büyük ikon)
                    Surface(
                        modifier = Modifier.size(120.dp),
                        color = Color(0xFF48C9B0), // Görseldeki yeşil tonu
                        shape = RoundedCornerShape(30.dp) // Hafif döndürülmüş karemsi yapı
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Success",
                                tint = Color.White,
                                modifier = Modifier.size(80.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Başarı Metni
                Text(
                    text = "Randevunuz oluşturulmuştur",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Normal,
                        color = Color.Black,
                        fontSize = 18.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(60.dp))

                // "Randevularım" Butonu
                Button(
                    onClick = onNavigateToAppointments,
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4FC3F7) // Görseldeki buton mavisi
                    )
                ) {
                    Text(
                        text = "Randevularım",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                // BottomBar için boşluk bırakmaya gerek yok çünkü Scaffold dışında
                // ya da Scaffold padding ile yönetilecek
            }
        }
    }
}