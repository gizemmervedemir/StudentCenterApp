package com.example.studentcenterapp.ui.service

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentcenterapp.data.service.ServiceRepository
import com.example.studentcenterapp.model.Service
import com.example.studentcenterapp.ui.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.studentcenterapp.data.service.InMemoryServiceDataSource
import com.example.studentcenterapp.data.service.ServiceRepositoryImpl

class ServiceListViewModel(
    private val repository: ServiceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Service>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Service>>> = _uiState.asStateFlow()

    fun loadServices(departmentId: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                repository.getServicesByDepartment(departmentId).collect { list ->
                    _uiState.value = UiState.Success(list)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Bir hata oluştu")
            }
        }
    }
}