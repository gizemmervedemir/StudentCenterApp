package com.example.studentcenterapp.ui.staffauth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentcenterapp.data.auth.StaffAuthRepository
import com.example.studentcenterapp.ui.state.UiState
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StaffSignupViewModel(
    private val repo: StaffAuthRepository
) : ViewModel() {

    // --- Kullanıcıdan Alınan Bilgiler ---
    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _staffNumber = MutableStateFlow("")
    val staffNumber: StateFlow<String> = _staffNumber.asStateFlow()

    // --- Doğum Tarihi Parçaları ---
    private val _birthYear = MutableStateFlow("")
    val birthYear: StateFlow<String> = _birthYear.asStateFlow()
    private val _birthDay = MutableStateFlow("")
    val birthDay: StateFlow<String> = _birthDay.asStateFlow()
    private val _birthMonth = MutableStateFlow("")
    val birthMonth: StateFlow<String> = _birthMonth.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _passwordConfirm = MutableStateFlow("")
    val passwordConfirm: StateFlow<String> = _passwordConfirm.asStateFlow()

    private val _uiState = MutableStateFlow<UiState<Unit>>(UiState.Success(Unit))
    val uiState: StateFlow<UiState<Unit>> = _uiState.asStateFlow()

    // --- Input Değişim Fonksiyonları ---
    fun onNameChange(v: String) {
        _name.value = v
    }

    fun onEmailChange(v: String) {
        _email.value = v
    }

    fun onStaffNumberChange(v: String) {
        _staffNumber.value = v
    }

    fun onPasswordChange(v: String) {
        _password.value = v
    }

    fun onPasswordConfirmChange(v: String) {
        _passwordConfirm.value = v
    }

    fun updateDate(millis: Long?) {
        millis?.let {
            val date = Date(it)
            _birthYear.value = SimpleDateFormat("yyyy", Locale.getDefault()).format(date)
            _birthDay.value = SimpleDateFormat("dd", Locale.getDefault()).format(date)
            _birthMonth.value = SimpleDateFormat("MMMM", Locale("tr")).format(date)
        }
    }

    /**
     * Tüm verileri toplayıp Firebase'e yazar.
     */
    fun signup(onSuccess: () -> Unit) {
        // 1. Temel Validasyonlar
        if (_password.value != _passwordConfirm.value) {
            _uiState.value = UiState.Error("Şifreler uyuşmuyor!")
            return
        }

        val sNo = _staffNumber.value.trim()
        if (sNo.isEmpty()) {
            _uiState.value = UiState.Error("Personel numarası girilmedi!")
            return
        }

        viewModelScope.launch {
            _uiState.value = UiState.Loading

            // 2. Önce Repo üzerinden Auth kaydını yap (Hesap açılır)
            val res = repo.signup(
                name = _name.value.trim(),
                email = _email.value.trim(),
                password = _password.value
            )

            if (res.isSuccess) {
                // 3. Kayıt olan kullanıcının Firebase tarafından atanan UID'sini hemen yakala
                val currentUid =
                    com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid

                if (currentUid != null) {
                    try {
                        // 4. Departman ID'yi personelin girdiği numaradan hesapla (Son hane)
                        val lastDigit = sNo.last().toString()
                        val resolvedDeptId = if (lastDigit.toIntOrNull() in 1..8) lastDigit else "1"

                        // 5. Firebase dökümanına eklenecek "eksik" verileri hazırla
                        val dataToUpdate = hashMapOf(
                            "staffNo" to sNo,
                            "departmentId" to resolvedDeptId,
                            "birthDate" to "${_birthDay.value} ${_birthMonth.value} ${_birthYear.value}",
                            // Mevcut verileri de üzerine yazarak garantiye alalım
                            "fullName" to _name.value.trim(),
                            "email" to _email.value.trim(),
                            "role" to "staff"
                        )

                        // 6. Firestore'daki o dökümanı bul ve verileri birleştir (Merge)
                        FirebaseFirestore.getInstance()
                            .collection("users")
                            .document(currentUid)
                            .set(dataToUpdate, com.google.firebase.firestore.SetOptions.merge())
                            .await()

                        _uiState.value = UiState.Success(Unit)
                        onSuccess()
                    } catch (e: Exception) {
                        _uiState.value =
                            UiState.Error("Firestore verisi güncellenemedi: ${e.message}")
                    }
                }
            } else {
                _uiState.value = UiState.Error(res.exceptionOrNull()?.message ?: "Kayıt başarısız")
            }
        }
    }
}