package com.example.studentcenterapp.ui.staffauth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.studentcenterapp.data.auth.StaffAuthRepository

class StaffSignupViewModelFactory(
    private val repo: StaffAuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return StaffSignupViewModel(repo) as T
    }
}
