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

    // Firestore'dan gelen ham listeyi burada tutuyoruz
    private var allAppointments: List<Appointment> = emptyList()

    // UI'ın dinlediği filtrelenmiş liste
    private val _uiState = MutableStateFlow<UiState<List<Appointment>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Appointment>>> = _uiState.asStateFlow()

    // Personelin ismi (Firestore 'users' koleksiyonundan)
    private val _staffName = MutableStateFlow("Personel")
    val staffName: StateFlow<String> = _staffName.asStateFlow()

    // Şu an seçili olan filtre (Default: pending)
    private val _currentFilter = MutableStateFlow("pending")
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
            _uiState.value = UiState.Loading
            try {
                // Repository üzerinden personelin tüm randevularını canlı dinle
                repository.getPendingAppointmentsForStaff(staffId).collect { list ->
                    allAppointments = list
                    applyFilter(_currentFilter.value) // Veri geldiğinde mevcut filtreyi uygula
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Veriler yüklenemedi")
            }
        }
    }

    // Ekrandaki kutulara tıklandığında bu fonksiyon çağrılacak
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

    fun approve(appointmentId: String) = updateStatus(appointmentId, true)
    fun reject(appointmentId: String) = updateStatus(appointmentId, false)

    private fun updateStatus(appointmentId: String, isApprove: Boolean) {
        viewModelScope.launch {
            _actionLoading.value = true
            _actionError.value = null

            val result = if (isApprove) repository.approveAppointment(appointmentId)
            else repository.rejectAppointment(appointmentId)

            _actionLoading.value = false
            if (result.isFailure) {
                _actionError.value = result.exceptionOrNull()?.message
            }
        }
    }
}