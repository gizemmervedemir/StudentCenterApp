package com.example.studentcenterapp.data.chat

import com.example.studentcenterapp.model.ChatMessage
import com.example.studentcenterapp.model.Conversation
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ChatRepository(private val db: FirebaseFirestore) {

    // Konuşma listesini (Chat_Main) dinle
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

    // Mesaj detaylarını (Chat_Ali) anlık dinle
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

    // Mesaj Gönder
    fun sendMessage(conversationId: String, message: ChatMessage) {
        val batch = db.batch()

        // 1. Mesajı alt koleksiyona ekle
        val msgRef = db.collection("conversations").document(conversationId)
            .collection("messages").document()
        batch.set(msgRef, message)

        // 2. Ana konuşma dokümanındaki "son mesaj" bilgisini güncelle
        val convRef = db.collection("conversations").document(conversationId)
        batch.update(convRef, "lastMessage", message.text, "timestamp", message.timestamp)

        batch.commit()
    }
}