package com.example.studentcenterapp.viewmodel.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.studentcenterapp.data.staff.StaffRepository
import com.example.studentcenterapp.data.student.StudentRepository

class ProfileViewModelFactory(
    private val studentRepo: StudentRepository,
    private val staffRepo: StaffRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(studentRepo, staffRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}