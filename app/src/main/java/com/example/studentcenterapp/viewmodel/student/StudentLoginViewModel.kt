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
import kotlinx.coroutines.launch

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

            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { authResult ->
                    isLoading = false
                    // Firebase'in verdiği benzersiz User ID'yi (UID) session'a kaydediyoruz
                    val userId = authResult.user?.uid ?: ""
                    com.example.studentcenterapp.data.student.StudentSession.currentStudentId = userId
                    onSuccess()
                }
                .addOnFailureListener { e ->
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