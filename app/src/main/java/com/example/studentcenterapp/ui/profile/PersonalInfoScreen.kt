package com.example.studentcenterapp.ui.profile

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studentcenterapp.ui.theme.Figtree
import com.example.studentcenterapp.ui.theme.PrimaryBlue

@Composable
fun PersonalInfoScreen(
    onBackClick: () -> Unit,
    onUpdateClick: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var day by remember { mutableStateOf("") }
    var month by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBackIos, contentDescription = "Geri")
            }
            Text(
                "Kişisel Bilgiler",
                style = androidx.compose.ui.text.TextStyle(
                    fontFamily = Figtree,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            )
        }

        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            ProfileFormTextField(value = name, label = "Ad Soyad", onValueChange = { name = it })

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ProfileFormTextField(modifier = Modifier.weight(1.5f), value = year, label = "Yıl", onValueChange = { year = it })
                ProfileFormTextField(modifier = Modifier.weight(1f), value = day, label = "Gün", onValueChange = { day = it })
                ProfileFormTextField(modifier = Modifier.weight(1f), value = month, label = "Ay", onValueChange = { month = it })
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onUpdateClick,
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Güncelle", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ProfileFormTextField(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    )
}