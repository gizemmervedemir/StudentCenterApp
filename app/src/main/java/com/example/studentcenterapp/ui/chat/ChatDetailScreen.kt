package com.example.studentcenterapp.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studentcenterapp.data.student.StudentSession
import com.example.studentcenterapp.ui.common.AppBottomBar
import com.example.studentcenterapp.ui.common.AppTab
import com.example.studentcenterapp.ui.common.AppTopBar
import com.example.studentcenterapp.ui.common.bottomTabs
import com.example.studentcenterapp.viewmodel.chat.ChatViewModel

@Composable
fun ChatDetailScreen(
    chatId: String,
    chatTitle: String,
    currentRoute: String?,
    onTabSelected: (AppTab) -> Unit,
    onBackClick: () -> Unit,
    viewModel: ChatViewModel = viewModel(factory = ChatViewModel.Factory)
) {
    val messages by viewModel.messages.collectAsState()
    var messageText by remember { mutableStateOf("") }

    LaunchedEffect(chatId) { viewModel.loadMessages(chatId) }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF4FC3F7))) {
        AppTopBar(title = "")

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFF5F5F5), // Mesaj alanı hafif gri (Figma'daki gibi)
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            Column {
                // Kişi Bilgisi Header
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBackClick) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) }
                    Surface(modifier = Modifier.size(40.dp), shape = CircleShape, color = Color.LightGray) {
                        Icon(Icons.Default.Person, null, tint = Color.White, modifier = Modifier.padding(8.dp))
                    }
                    Column(modifier = Modifier.padding(start = 12.dp)) {
                        Text(chatTitle, fontWeight = FontWeight.Bold)
                        Text("Görsel İletişim Tasarımı, 4. Sınıf", fontSize = 12.sp, color = Color.Gray)
                    }
                }

                HorizontalDivider(color = Color.LightGray, modifier = Modifier.padding(horizontal = 16.dp))

                LazyColumn(modifier = Modifier.weight(1f).padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(messages) { message ->
                        MessageBubble(text = message.text, isMe = message.senderId == StudentSession.currentStudentId)
                    }
                }

                ChatInputArea(
                    text = messageText,
                    onValueChange = { messageText = it },
                    onSendClick = {
                        if (messageText.isNotBlank()) {
                            viewModel.sendMessage(chatId, messageText)
                            messageText = ""
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun MessageBubble(text: String, isMe: Boolean) {
    val bubbleColor = if (isMe) Color(0xFFB2EBF2) else Color(0xFFE0E0E0) // Figma'daki açık mavi ve gri
    val alignment = if (isMe) Alignment.CenterEnd else Alignment.CenterStart
    val shape = if (isMe)
        RoundedCornerShape(16.dp, 16.dp, 0.dp, 16.dp)
    else
        RoundedCornerShape(16.dp, 16.dp, 16.dp, 0.dp)

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = alignment) {
        Surface(color = bubbleColor, shape = shape) {
            Text(
                text = text,
                modifier = Modifier.padding(12.dp),
                fontSize = 14.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
fun ChatInputArea(text: String, onValueChange: (String) -> Unit, onSendClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .imePadding(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Oval beyaz kutu
        TextField(
            value = text,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Mesaj yazın...") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF0F0F0),
                unfocusedContainerColor = Color(0xFFF0F0F0),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        // Pembe yuvarlak gönder butonu
        IconButton(
            onClick = onSendClick,
            modifier = Modifier.size(48.dp).background(Color(0xFFE91E63), CircleShape)
        ) {
            Icon(Icons.Default.Send, contentDescription = null, tint = Color.White)
        }
    }
}
