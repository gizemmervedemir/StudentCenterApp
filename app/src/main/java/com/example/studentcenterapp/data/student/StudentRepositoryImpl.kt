package com.example.studentcenterapp.data.student

import com.example.studentcenterapp.model.Student
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class StudentRepositoryImpl(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()) : StudentRepository {

    override suspend fun getCurrentStudent(): Student = TODO()

    override suspend fun getStudentById(id: String): Student? {
        return try {
            val doc = firestore.collection("users").document(id).get().await()
            if (!doc.exists()) return null

            Student(
                id = id,
                // Firebase alan isimleri: fullName, schoolNumber
                name = doc.getString("fullName") ?: "",
                email = doc.getString("email") ?: "",
                studentNumber = doc.getString("schoolNumber") ?: "",
                // Eğer modelinde profilePictureUrl varsa ekleyebilirsin:
                // profilePictureUrl = doc.getString("profilePictureUrl")
            )
        } catch (e: Exception) {
            null
        }
    }


    override suspend fun login(email: String, password: String): Result<Student> {
        kotlinx.coroutines.delay(1000) // Gerçekçi bir bekleme

        // Şimdilik basit bir kontrol: E-posta eylul@example.com ve şifre 123456 ise girsin
        return if (email == "eylul@example.com" && password == "123456") {
            Result.success(getStudentById("stu2")!!) // Senin veri kaynağındaki Eylül
        } else {
            Result.failure(Exception("E-posta veya şifre hatalı!"))
        }
    }

    override suspend fun signup(
        name: String,
        surname: String,
        schoolNumber: String,
        email: String,
        password: String,
        birthDay: String,
        birthMonth: String,
        birthYear: String
    ): Result<Student> {
        kotlinx.coroutines.delay(1000)
        // Kayıt başarılı simülasyonu
        return Result.success(Student("new_id", "$name $surname", email, schoolNumber))
    }

    override suspend fun updateStudentProfile(id: String, name: String, email: String): Result<Unit> {
        return try {
            val updates = mapOf(
                "fullName" to name,   // ✅ users alan adı
                "email" to email
            )
            firestore.collection("users").document(id).update(updates).await() // ✅ users
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}