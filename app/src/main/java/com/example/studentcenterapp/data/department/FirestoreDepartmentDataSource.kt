package com.example.studentcenterapp.data.department

import com.example.studentcenterapp.model.Department
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirestoreDepartmentDataSource : DepartmentDataSource {
    private val db = FirebaseFirestore.getInstance()

    override fun getDepartments(): Flow<List<Department>> = callbackFlow {
        val listener = db.collection("departments")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val items = snapshot?.toObjects(Department::class.java) ?: emptyList()
                trySend(items)
            }
        awaitClose { listener.remove() }
    }
}