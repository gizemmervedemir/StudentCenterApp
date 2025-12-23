package com.example.studentcenterapp.ui.service

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.studentcenterapp.data.service.ServiceRepository

class ServiceListViewModelFactory(
    private val repository: ServiceRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ServiceListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ServiceListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
