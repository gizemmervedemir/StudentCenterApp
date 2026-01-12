package com.example.studentcenterapp.ui.staffauth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.studentcenterapp.data.auth.StaffAuthRepository

class StaffSignupViewModelFactory(
    private val repo: StaffAuthRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // ViewModel sınıfının doğru olup olmadığını kontrol etmek iyi bir pratiktir
        if (modelClass.isAssignableFrom(StaffSignupViewModel::class.java)) {
            return StaffSignupViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}