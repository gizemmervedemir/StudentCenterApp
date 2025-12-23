package com.example.studentcenterapp.viewmodel.department

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.studentcenterapp.data.department.DepartmentRepository

class DepartmentListViewModelFactory(
    private val repository: DepartmentRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DepartmentListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DepartmentListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
