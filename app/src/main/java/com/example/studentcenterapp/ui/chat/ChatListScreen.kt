package com.example.studentcenterapp.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studentcenterapp.model.Conversation
import com.example.studentcenterapp.ui.common.AppBottomBar
import com.example.studentcenterapp.ui.common.AppTab
import com.example.studentcenterapp.ui.common.AppTopBar
import com.example.studentcenterapp.viewmodel.chat.ChatViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ChatListScreen(
    tabs: List<AppTab>,
    currentRoute: String?,
    isStaff: Boolean, // Personel mi öğrenci mi bilgisi
    onTabSelected: (AppTab) -> Unit,
    onChatClick: (String, String) -> Unit,
    onNewChatClick: () -> Unit,
    viewModel: ChatViewModel = viewModel(factory = ChatViewModel.Factory)
) {
    // ViewModel içindeki StateFlow'u dinliyoruz
    val conversations by viewModel.conversations.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        bottomBar = {
            AppBottomBar(
                tabs = tabs,
                currentRoute = currentRoute,
                onTabSelected = onTabSelected
            )
        },
        containerColor = Color(0xFF4FC3F7)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFF4FC3F7))
        ) {
            AppTopBar(title = "")

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color(0xFFF0F0F0),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp)
                ) {
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

                        IconButton(
                            onClick = onNewChatClick,
                            modifier = Modifier
                                .size(32.dp)
                                .background(Color(0xFFE91E63), RoundedCornerShape(8.dp))
                        ) {
                            Icon(Icons.Default.Add, "Yeni", tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                    }

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Ara...", color = Color.Gray, fontSize = 14.sp) },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(25.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        trailingIcon = { Icon(Icons.Default.Search, null, tint = Color.Gray) },
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    if (conversations.isEmpty()) {
                        Box(Modifier.fillMaxSize(), Alignment.Center) {
                            Text("Henüz mesajınız bulunmuyor.", color = Color.Gray)
                        }
                    } else {
                        val filteredList = conversations.filter {
                            it.studentName.contains(searchQuery, ignoreCase = true) ||
                                    it.serviceName.contains(searchQuery, ignoreCase = true)
                        }

                        LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            items(filteredList, key = { it.id }) { conversation ->
                                // Personel için öğrenci ismi, Öğrenci için hizmet ismi görünür
                                val title = if (isStaff) conversation.studentName else conversation.serviceName

                                ChatListItem(
                                    title = title,
                                    lastMessage = conversation.lastMessage,
                                    time = conversation.timestamp?.let {
                                        SimpleDateFormat("HH:mm", Locale.getDefault()).format(it.toDate())
                                    } ?: "",
                                    onClick = { onChatClick(conversation.id, title) }
                                )
                                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatListItem(
    title: String,
    lastMessage: String,
    time: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(modifier = Modifier.size(48.dp), shape = CircleShape, color = Color.LightGray) {
            Icon(Icons.Default.Person, null, tint = Color.White, modifier = Modifier.padding(10.dp))
        }

        Column(modifier = Modifier.padding(start = 16.dp).weight(1f)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Text(lastMessage, fontSize = 13.sp, color = Color.Gray, maxLines = 1)
        }

        Text(time, fontSize = 12.sp, color = Color.Gray)
    }
}