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
    private val staffId: String,
    private val repository: StaffRepository
) : ViewModel() {

    private var allAppointments: List<Appointment> = emptyList()

    private val _uiState = MutableStateFlow<UiState<List<Appointment>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Appointment>>> = _uiState.asStateFlow()

    private val _staffName = MutableStateFlow("Personel")
    val staffName: StateFlow<String> = _staffName.asStateFlow()

    // Görseldeki default filtre: Aktif Randevular (approved)
    private val _currentFilter = MutableStateFlow("approved")
    val currentFilter: StateFlow<String> = _currentFilter.asStateFlow()

    private val _actionLoading = MutableStateFlow(false)
    val actionLoading: StateFlow<Boolean> = _actionLoading.asStateFlow()

    private val _actionError = MutableStateFlow<String?>(null)
    val actionError: StateFlow<String?> = _actionError.asStateFlow()

    init {
        fetchStaffName()
        observeAppointments()
    }

    private fun fetchStaffName() {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        FirebaseFirestore.getInstance()
            .collection("users")
            .document(currentUserId)
            .get()
            .addOnSuccessListener { doc ->
                val fullName = doc.getString("fullName") ?: doc.getString("name")
                if (!fullName.isNullOrBlank()) {
                    _staffName.value = fullName.split(" ").first()
                }
            }
    }

    private fun observeAppointments() {
        viewModelScope.launch {
            repository.getPendingAppointmentsForStaff(staffId).collect { list ->
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
        val filteredList = if (status == "all") allAppointments else allAppointments.filter { it.status == status }
        _uiState.value = UiState.Success(filteredList)
    }

    fun approve(id: String) = updateStatus(id, true)
    fun reject(id: String) = updateStatus(id, false)

    private fun updateStatus(id: String, isApprove: Boolean) {
        viewModelScope.launch {
            _actionLoading.value = true
            val result = if (isApprove) repository.approveAppointment(id) else repository.rejectAppointment(id)
            _actionLoading.value = false
            if (result.isFailure) _actionError.value = result.exceptionOrNull()?.message
        }
    }
}