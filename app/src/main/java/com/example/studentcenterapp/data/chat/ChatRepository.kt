package com.example.studentcenterapp.data.chat

import com.example.studentcenterapp.model.ChatMessage
import com.example.studentcenterapp.model.Conversation
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ChatRepository(private val db: FirebaseFirestore) {

    // Konuşmaları dinle (Kullanıcının içinde olduğu odalar)
    fun getConversations(userId: String): Flow<List<Conversation>> = callbackFlow {
        val subscription = db.collection("conversations")
            .whereArrayContains("participants", userId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, _ ->
                val conversations = snapshot?.toObjects(Conversation::class.java) ?: emptyList()
                trySend(conversations)
            }
        awaitClose { subscription.remove() }
    }

    // Mesajları anlık dinle
    fun getMessages(conversationId: String): Flow<List<ChatMessage>> = callbackFlow {
        val subscription = db.collection("conversations")
            .document(conversationId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, _ ->
                val messages = snapshot?.toObjects(ChatMessage::class.java) ?: emptyList()
                trySend(messages)
            }
        awaitClose { subscription.remove() }
    }

    // --- OTOMATİK OLUŞTURMA MANTIĞI BURADA ---
    suspend fun sendMessage(
        conversationId: String,
        message: ChatMessage,
        studentId: String,
        studentName: String,
        serviceName: String
    ) {
        val convRef = db.collection("conversations").document(conversationId)

        // set(..., SetOptions.merge()) kullanarak doküman yoksa oluşturur, varsa sadece günceller
        val conversationData = mapOf(
            "id" to conversationId,
            "participants" to listOf(studentId, message.senderId), // Öğrenci ve Personel (sender)
            "studentId" to studentId,
            "studentName" to studentName,
            "serviceName" to serviceName,
            "lastMessage" to message.text,
            "timestamp" to message.timestamp
        )

        // 1. Ana dokümanı oluştur/güncelle
        convRef.set(conversationData, SetOptions.merge()).await()

        // 2. Mesajı alt koleksiyona ekle
        convRef.collection("messages").add(message).await()
    }
}