package com.example.studentcenterapp.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.studentcenterapp.R

@Composable
fun UpdateSuccessScreen(onProfileClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.yes_il_tik), // Yeşil tik ikonu
            contentDescription = null,
            tint = Color(0xFF7DCEB2),
            modifier = Modifier.size(150.dp)
        )
        Text("Bilgileriniz başarıyla güncellenmiştir")
        Button(onClick = onProfileClick) { Text("Profil") }
    }
}