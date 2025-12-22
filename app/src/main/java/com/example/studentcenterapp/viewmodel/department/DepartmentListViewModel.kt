package com.example.studentcenterapp.viewmodel.department

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentcenterapp.data.department.DepartmentRepository
import com.example.studentcenterapp.model.Department
import com.example.studentcenterapp.ui.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DepartmentListViewModel(
    private val repository: DepartmentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Department>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Department>>> = _uiState.asStateFlow()

    init {
        loadDepartments()
    }

    fun loadDepartments() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                repository.getDepartments().collect { list ->
                    _uiState.value = UiState.Success(list)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Bir hata oluştu")
            }
        }
    }
}