package com.example.studentcenterapp.ui.profile

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studentcenterapp.R
import com.example.studentcenterapp.ui.theme.Figtree
import com.example.studentcenterapp.ui.theme.PrimaryBlue

@Composable
fun ProfileHeader(
    userName: String,
    imageUri: Uri?,
    onImageClick: () -> Unit // String değil, fonksiyon olmalı
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp) // Figma'ya göre ayarlanabilir
            .background(PrimaryBlue),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 40.dp)
        ) {
            // Logo (VA simgesi)
            Image(
                painter = painterResource(id = R.drawable.logo_oldx), // Logo dosyanın ismi
                contentDescription = null,
                modifier = Modifier.size(60.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = userName,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = Figtree
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Profil Fotoğrafı ve Kamera İkonu
            Box(contentAlignment = Alignment.BottomEnd) {
                Surface(
                    modifier = Modifier.size(100.dp),
                    shape = CircleShape,
                    color = Color.LightGray
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.padding(20.dp),
                        tint = Color.Gray
                    )
                }

                // Mavi Kamera İkonu
                Surface(
                    modifier = Modifier
                        .size(30.dp)
                        .clickable { onImageClick() }
                        .offset(x = (-4).dp, y = (-4).dp),
                    shape = CircleShape,
                    color = PrimaryBlue,
                    border = BorderStroke(2.dp, Color.White)
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoCamera,
                        contentDescription = null,
                        modifier = Modifier.padding(6.dp),
                        tint = Color.White
                    )
                }
            }
        }
    }
}
