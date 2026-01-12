package com.example.studentcenterapp.data.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseAuthStaffAuthRepository : StaffAuthRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override suspend fun login(email: String, password: String): Result<String> {
        return try {
            val res = auth.signInWithEmailAndPassword(email, password).await()
            val uid = res.user?.uid ?: throw Exception("Kullanıcı bulunamadı.")
            Result.success(uid)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signup(name: String, email: String, password: String): Result<String> {
        return try {
            val res = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = res.user?.uid ?: throw Exception("Kayıt başarısız.")

            // Personeli 'users' koleksiyonuna 'staff' rolüyle kaydet (Eylül'ün mantığı)
            val staffData = hashMapOf(
                "uid" to uid,
                "fullName" to name,
                "email" to email,
                "role" to "staff"
            )
            db.collection("users").document(uid).set(staffData).await()

            Result.success(uid)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}