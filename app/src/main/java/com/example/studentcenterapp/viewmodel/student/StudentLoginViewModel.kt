package com.example.studentcenterapp.viewmodel.student

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.studentcenterapp.data.AppDI
import com.example.studentcenterapp.data.student.StudentRepository
import kotlinx.coroutines.launch

class StudentLoginViewModel(private val repository: StudentRepository) : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var errorMessage by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(false)

    fun onLoginClick(onSuccess: () -> Unit) {
        errorMessage = null // Her tıklamada hatayı sıfırla

        viewModelScope.launch {
            isLoading = true
            val result = repository.login(email, password)
            isLoading = false

            result.onSuccess {
                onSuccess()
            }.onFailure {
                errorMessage = "E-posta/Şifre uyuşmuyor"
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