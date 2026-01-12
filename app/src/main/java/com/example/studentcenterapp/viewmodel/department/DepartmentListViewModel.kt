package com.example.studentcenterapp.viewmodel.department

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentcenterapp.data.department.DepartmentRepository
import com.example.studentcenterapp.model.Department
import com.example.studentcenterapp.ui.state.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import kotlinx.coroutines.Job

class DepartmentListViewModel(
    private val repository: DepartmentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Department>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Department>>> = _uiState.asStateFlow()

    private val _userName = MutableStateFlow("Öğrenci")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadDepartmentsOnce()
        fetchUserName()
    }

    private fun loadDepartmentsOnce() {
        if (loadJob != null) return // zaten dinliyor
        loadJob = viewModelScope.launch {
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

    fun refreshDepartments() {
        loadJob?.cancel()
        loadJob = null
        loadDepartmentsOnce()
    }

    private fun fetchUserName() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        FirebaseFirestore.getInstance()
            .collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                val fullName = document.getString("fullName")
                if (!fullName.isNullOrBlank()) {
                    _userName.value = fullName.split(" ").first()
                }
            }
    }
}