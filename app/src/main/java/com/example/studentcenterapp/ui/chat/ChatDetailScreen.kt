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
import com.example.studentcenterapp.ui.common.AppBottomBar
import com.example.studentcenterapp.ui.common.AppTab
import com.example.studentcenterapp.ui.common.AppTopBar
import com.example.studentcenterapp.viewmodel.chat.ChatViewModel

@Composable
fun ChatDetailScreen(
    tabs: List<AppTab>,
    chatId: String,
    chatTitle: String, // Bu zaten öğrenci ismi (studentName) olarak geliyor
    currentRoute: String?,
    currentUserId: String,
    // EKLENEN PARAMETRELER:
    studentId: String,
    serviceName: String,
    onTabSelected: (AppTab) -> Unit,
    onBackClick: () -> Unit,
    viewModel: ChatViewModel
){
    val messages by viewModel.messages.collectAsState()
    var messageText by remember { mutableStateOf("") }

    LaunchedEffect(chatId) { viewModel.loadMessages(chatId) }

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
        ) {
            AppTopBar(title = "")

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color(0xFFF5F5F5),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
            ) {
                Column {
                    // Chat Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.Black)
                        }
                        Surface(
                            modifier = Modifier.size(40.dp),
                            shape = CircleShape,
                            color = Color.LightGray
                        ) {
                            Icon(Icons.Default.Person, null, tint = Color.White, modifier = Modifier.padding(8.dp))
                        }
                        Column(modifier = Modifier.padding(start = 12.dp)) {
                            Text(chatTitle, fontWeight = FontWeight.Bold, color = Color.Black)
                            Text("Aktif", fontSize = 12.sp, color = Color.Gray)
                        }
                    }

                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f), thickness = 1.dp)

                    // Mesaj Listesi
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(vertical = 16.dp)
                    ) {
                        items(messages) { message ->
                            MessageBubble(
                                text = message.text,
                                // currentUserId NavHost'tan doğru geldiği sürece "Benim" mesajlarım sağda görünür
                                isMe = message.senderId == currentUserId
                            )
                        }
                    }

                    // Input Alanı
                    ChatInputArea(
                        text = messageText,
                        onValueChange = { messageText = it },
                        onSendClick = {
                            if (messageText.isNotBlank()) {
                                // ViewModel'deki yeni sendMessage yapısına uygun çağrı:
                                viewModel.sendMessage(
                                    conversationId = chatId,
                                    text = messageText,
                                    studentId = studentId,
                                    studentName = chatTitle, // chatTitle zaten öğrenci adı
                                    serviceName = serviceName
                                )
                                messageText = ""
                            }
                        }
                    )
                }
            }
        }
    }
}

// ... MessageBubble ve ChatInputArea fonksiyonları aynı kalabilir ...

@Composable
fun MessageBubble(text: String, isMe: Boolean) {
    val bubbleColor = if (isMe) Color(0xFFB2EBF2) else Color.White
    val alignment = if (isMe) Alignment.CenterEnd else Alignment.CenterStart
    val shape = if (isMe)
        RoundedCornerShape(16.dp, 16.dp, 0.dp, 16.dp)
    else
        RoundedCornerShape(16.dp, 16.dp, 16.dp, 0.dp)

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = alignment
    ) {
        Surface(
            color = bubbleColor,
            shape = shape,
            shadowElevation = 1.dp
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(12.dp),
                fontSize = 14.sp,
                color = Color.Black
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatInputArea(text: String, onValueChange: (String) -> Unit, onSendClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .imePadding(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = text,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Mesaj yazın...", color = Color.Gray) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF0F0F0),
                    unfocusedContainerColor = Color(0xFFF0F0F0),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            IconButton(
                onClick = onSendClick,
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFFE91E63), CircleShape)
            ) {
                Icon(Icons.Default.Send, contentDescription = "Gönder", tint = Color.White)
            }
        }
    }
}