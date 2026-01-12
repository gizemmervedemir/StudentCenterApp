package com.example.studentcenterapp.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studentcenterapp.ui.common.AppBottomBar
import com.example.studentcenterapp.ui.common.AppTab
import com.example.studentcenterapp.ui.common.AppTopBar
import com.example.studentcenterapp.ui.common.bottomTabs

@Composable
fun ChatListScreen(
    currentRoute: String?,
    onTabSelected: (AppTab) -> Unit,
    onChatClick: (String, String) -> Unit,
    onNewChatClick: () -> Unit
) {
    Scaffold(
        bottomBar = {
            AppBottomBar(
                tabs = bottomTabs,
                currentRoute = currentRoute,
                onTabSelected = onTabSelected
            )
        },
        containerColor = Color(0xFF4FC3F7) // Üstteki mavi alan
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFF4FC3F7))
        ) {
            // Logolu Üst Bar
            AppTopBar(title = "")

            // Beyaz Oval Gövde
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color(0xFFF0F0F0), // Tasarımdaki hafif gri/beyaz tonu
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp)
                ) {
                    // Başlık ve Pembe + Butonu Satırı
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp, bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Mesajlar",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        )

                        // Tasarımdaki Pembe Kare + Butonu
                        IconButton(
                            onClick = onNewChatClick,
                            modifier = Modifier
                                .size(32.dp)
                                .background(Color(0xFFE91E63), RoundedCornerShape(8.dp))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Yeni Mesaj",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // Arama Çubuğu (Search Bar)
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        placeholder = { Text("Ara...", color = Color.Gray, fontSize = 14.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(25.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = Color.LightGray,
                            unfocusedBorderColor = Color.LightGray
                        ),
                        trailingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray)
                        },
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Chat Listesi buraya gelecek (Deniz Dinsel, Zeynep Kurubacak örnekleri)
                    // Tasarımdaki gibi Divider'lı bir liste yapısı kullanılmalı
                }
            }
        }
    }
}