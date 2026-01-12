package com.example.studentcenterapp.data.service

import com.example.studentcenterapp.model.Service
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirestoreServiceDataSource : ServiceDataSource {
    private val db = FirebaseFirestore.getInstance()

    override fun getServicesByDepartment(departmentId: String): Flow<List<Service>> = callbackFlow {
        val listener = db.collection("services")
            .whereEqualTo("departmentId", departmentId) // İlişkiyi burada kuruyoruz!
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val items = snapshot?.toObjects(Service::class.java) ?: emptyList()
                trySend(items)
            }
        awaitClose { listener.remove() }
    }
}