package com.example.studentcenterapp.ui.staff

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentcenterapp.data.staff.StaffRepository
import com.example.studentcenterapp.model.Appointment
import com.example.studentcenterapp.ui.state.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StaffDashboardViewModel(
    private val staffId: String, // Firebase UID
    private val repository: StaffRepository
) : ViewModel() {

    private var allAppointments: List<Appointment> = emptyList()

    private val _uiState = MutableStateFlow<UiState<List<Appointment>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Appointment>>> = _uiState.asStateFlow()

    private val _staffName = MutableStateFlow("Personel")
    val staffName: StateFlow<String> = _staffName.asStateFlow()

    private val _departmentId = MutableStateFlow<String?>(null)

    // Başlangıçta bekleyen randevuları (pending) göstermek daha mantıklıdır
    private val _currentFilter = MutableStateFlow("pending")
    val currentFilter: StateFlow<String> = _currentFilter.asStateFlow()

    private val _actionLoading = MutableStateFlow(false)
    val actionLoading: StateFlow<Boolean> = _actionLoading.asStateFlow()

    private val _actionError = MutableStateFlow<String?>(null)
    val actionError: StateFlow<String?> = _actionError.asStateFlow()

    init {
        loadStaffDataAndResolveDepartment()
    }

    /**
     * Firestore'dan personelin staffNo bilgisini çeker ve departmanını belirler.
     */
    private fun loadStaffDataAndResolveDepartment() {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        FirebaseFirestore.getInstance()
            .collection("users")
            .document(currentUserId)
            .get()
            .addOnSuccessListener { doc ->

                val fullName = doc.getString("fullName") ?: "Personel"
                _staffName.value = fullName.split(" ").first()


                val resolvedDeptId = doc.getString("departmentId")

                if (resolvedDeptId != null) {
                    _departmentId.value = resolvedDeptId

                    observeDepartmentAppointments(resolvedDeptId)
                } else {
                    _uiState.value = UiState.Error("Departman bilgisi bulunamadı. Lütfen tekrar kayıt olun.")
                }
            }
            .addOnFailureListener { e ->
                _uiState.value = UiState.Error("Bağlantı hatası: ${e.message}")
            }
    }

    private fun observeDepartmentAppointments(deptId: String) {
        viewModelScope.launch {
            repository.getAppointmentsByDepartment(deptId).collect { list ->
                allAppointments = list
                applyFilter(_currentFilter.value)
            }
        }
    }

    fun onFilterChanged(newStatus: String) {
        _currentFilter.value = newStatus
        applyFilter(newStatus)
    }

    private fun applyFilter(status: String) {
        val filteredList = if (status == "all") {
            allAppointments
        } else {
            allAppointments.filter { it.status == status }
        }
        _uiState.value = UiState.Success(filteredList)
    }
    fun approve(id: String) {
        viewModelScope.launch {
            _actionLoading.value = true
            // Onaylarken personelin UID'sini (staffId) gönderiyoruz
            val result = repository.approveAppointment(id, staffId)
            _actionLoading.value = false
            if (result.isFailure) {
                _actionError.value = result.exceptionOrNull()?.message
            }
        }
    }

    fun reject(id: String) {
        viewModelScope.launch {
            _actionLoading.value = true
            val result = repository.rejectAppointment(id)
            _actionLoading.value = false
            if (result.isFailure) {
                _actionError.value = result.exceptionOrNull()?.message
            }
        }
    }

    fun clearError() {
        _actionError.value = null
    }
}