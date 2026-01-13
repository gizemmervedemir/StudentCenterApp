package com.example.studentcenterapp.viewmodel.student

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.studentcenterapp.data.AppDI
import com.example.studentcenterapp.data.student.StudentRepository
import com.example.studentcenterapp.data.student.StudentSession
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class StudentLoginViewModel(private val repository: StudentRepository) : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var errorMessage by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(false)

    fun onLoginClick(onSuccess: () -> Unit) {
        errorMessage = null
        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Lütfen tüm alanları doldurun."
            return
        }

        viewModelScope.launch {
            isLoading = true
            val auth = com.google.firebase.auth.FirebaseAuth.getInstance()

            try {
                // 1. Firebase Auth ile giriş yap
                val authResult = auth.signInWithEmailAndPassword(email, password).await()
                val userId = authResult.user?.uid ?: ""

                // 2. Firestore'dan kullanıcının gerçek bilgilerini çek
                val userDoc = FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(userId)
                    .get()
                    .await()

                if (userDoc.exists()) {
                    StudentSession.currentStudentId = userId
                    StudentSession.studentNumber = userDoc.getString("schoolNumber") ?: "Bilinmiyor"
                    StudentSession.currentStudentName = userDoc.getString("fullName") ?: "İsimsiz Öğrenci"

                    isLoading = false
                    onSuccess()
                } else {
                    isLoading = false
                    errorMessage = "Kullanıcı profili bulunamadı."
                }

            } catch (e: Exception) {
                isLoading = false
                errorMessage = "Giriş başarısız: E-posta veya şifre hatalı."
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return StudentLoginViewModel(AppDI.studentRepository) as T
            }
        }
    }
}