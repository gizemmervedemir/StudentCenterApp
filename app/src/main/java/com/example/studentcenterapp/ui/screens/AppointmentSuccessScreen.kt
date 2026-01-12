package com.example.studentcenterapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studentcenterapp.R

/**
 * Görseldeki 12 köşeli yeşil rozet (badge) şeklini oluşturan özel Shape
 */
val BadgeStarShape = object : Shape {
    override fun createOutline(
        size: androidx.compose.ui.geometry.Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path()
        val numPoints = 12
        val centerX = size.width / 2f
        val centerY = size.height / 2f
        val outerRadius = size.width / 2f
        val innerRadius = outerRadius * 0.82f // Tırtık derinliği

        for (i in 0 until numPoints * 2) {
            val r = if (i % 2 == 0) outerRadius else innerRadius
            val angle = Math.PI.toFloat() / numPoints * i
            val x = centerX + r * Math.cos(angle.toDouble()).toFloat()
            val y = centerY + r * Math.sin(angle.toDouble()).toFloat()
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        path.close()
        return Outline.Generic(path)
    }
}

@Composable
fun AppointmentSuccessScreen(
    onNavigateToAppointments: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF4DB6E1)) // Görseldeki tam üst mavi tonu
    ) {
        // Üst Logo Alanı
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.25f),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.logo_oldx),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(90.dp)
            )
        }

        // Beyaz Gövde Alanı
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.75f),
            color = Color(0xFFF2F2F2), // Görseldeki hafif kırık beyaz/gri tonu
            shape = RoundedCornerShape(topStart = 35.dp, topEnd = 35.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 60.dp), // Butonu biraz yukarı çekmek için
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // YEŞİL ROZET VE TİK (Birebir görseldeki form)
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(180.dp)
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = Color(0xFF48D1AC), // Görseldeki tam yeşil tonu
                        shape = BadgeStarShape
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Success",
                                tint = Color.White,
                                modifier = Modifier.size(95.dp) // Tik büyüklüğü
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(45.dp))

                // Başarı Metni
                Text(
                    text = "Randevunuz oluşturulmuştur",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF333333)
                )

                Spacer(modifier = Modifier.height(80.dp))

                // "Randevularım" Butonu (Birebir görseldeki form)
                Button(
                    onClick = onNavigateToAppointments,
                    modifier = Modifier
                        .width(170.dp)
                        .height(52.dp),
                    shape = RoundedCornerShape(26.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2EABDA) // Görseldeki buton mavisi
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    Text(
                        text = "Randevularım",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}