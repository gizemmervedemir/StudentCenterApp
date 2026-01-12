package com.example.studentcenterapp.viewmodel.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.studentcenterapp.data.AppDI
import com.example.studentcenterapp.data.chat.ChatRepository
import com.example.studentcenterapp.data.student.StudentSession
import com.example.studentcenterapp.model.ChatMessage
import com.example.studentcenterapp.model.Conversation
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel(
    private val repository: ChatRepository,
    private val currentUserId: String
) : ViewModel() {

    private val _conversations = MutableStateFlow<List<Conversation>>(emptyList())
    val conversations: StateFlow<List<Conversation>> = _conversations

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    init {
        // ViewModel ilk oluştuğunda kullanıcının konuşmalarını dinlemeye başla
        loadConversations()
    }

    private fun loadConversations() {
        viewModelScope.launch {
            repository.getConversations(currentUserId).collect {
                _conversations.value = it
            }
        }
    }

    fun loadMessages(conversationId: String) {
        viewModelScope.launch {
            repository.getMessages(conversationId).collect {
                _messages.value = it
            }
        }
    }

    fun sendMessage(conversationId: String, text: String) {
        if (text.isBlank()) return
        val newMessage = ChatMessage(
            senderId = currentUserId,
            text = text,
            timestamp = Timestamp.now()
        )
        repository.sendMessage(conversationId, newMessage)
    }

    // --- FACTORY EKLEMESİ (Crash Çözümü Burası) ---
    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                // Repository'yi AppDI'dan, UserID'yi StudentSession'dan alıyoruz
                return ChatViewModel(
                    repository = AppDI.chatRepository,
                    currentUserId = StudentSession.currentStudentId ?: ""
                ) as T
            }
        }
    }
}