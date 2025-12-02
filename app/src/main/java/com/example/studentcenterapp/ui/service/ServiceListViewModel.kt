package com.example.studentcenterapp.ui.service

import androidx.lifecycle.ViewModel
import com.example.studentcenterapp.data.service.InMemoryServiceDataSource
import com.example.studentcenterapp.data.service.ServiceRepositoryImpl

class ServiceListViewModel : ViewModel() {

    private val serviceRepository = ServiceRepositoryImpl(
        dataSource = InMemoryServiceDataSource()
    )

    // ...
}