package com.example.studentcenterapp.ui.staffauth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentcenterapp.data.auth.StaffAuthRepository
import com.example.studentcenterapp.ui.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StaffSignupViewModel(
    private val repo: StaffAuthRepository
) : ViewModel() {

    // --- Temel Bilgiler ---
    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _staffNumber = MutableStateFlow("") // Yeni alan
    val staffNumber: StateFlow<String> = _staffNumber.asStateFlow()

    // --- Doğum Tarihi ---
    private val _birthYear = MutableStateFlow("")
    val birthYear: StateFlow<String> = _birthYear.asStateFlow()

    private val _birthDay = MutableStateFlow("")
    val birthDay: StateFlow<String> = _birthDay.asStateFlow()

    private val _birthMonth = MutableStateFlow("")
    val birthMonth: StateFlow<String> = _birthMonth.asStateFlow()

    // --- Şifre Alanları ---
    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _passwordConfirm = MutableStateFlow("") // Yeni alan
    val passwordConfirm: StateFlow<String> = _passwordConfirm.asStateFlow()

    // --- UI & Navigasyon Durumu ---
    private val _uiState = MutableStateFlow<UiState<Unit>>(UiState.Success(Unit))
    val uiState: StateFlow<UiState<Unit>> = _uiState.asStateFlow()

    // --- Değişiklik Fonksiyonları ---
    fun onNameChange(v: String) { _name.value = v }
    fun onEmailChange(v: String) { _email.value = v }
    fun onStaffNumberChange(v: String) { _staffNumber.value = v }
    fun onPasswordChange(v: String) { _password.value = v }
    fun onPasswordConfirmChange(v: String) { _passwordConfirm.value = v }

    /**
     * DatePicker'dan gelen Long değeri Yıl, Gün ve Ay olarak parçalar.
     */
    fun updateDate(millis: Long?) {
        millis?.let {
            val date = Date(it)
            _birthYear.value = SimpleDateFormat("yyyy", Locale.getDefault()).format(date)
            _birthDay.value = SimpleDateFormat("dd", Locale.getDefault()).format(date)
            _birthMonth.value = SimpleDateFormat("MMMM", Locale("tr")).format(date)
        }
    }

    /**
     * Kayıt işlemi ve temel validasyon kontrolü.
     */
    fun signup(onSuccess: () -> Unit) {
        // Temel şifre kontrolü
        if (_password.value != _passwordConfirm.value) {
            _uiState.value = UiState.Error("Şifreler uyuşmuyor!")
            return
        }

        if (_staffNumber.value.isBlank()) {
            _uiState.value = UiState.Error("Lütfen personel numaranızı girin.")
            return
        }

        viewModelScope.launch {
            _uiState.value = UiState.Loading

            // Not: Mevcut repo.signup metodu sadece name, email, password alıyorsa
            // diğer alanları (staffNumber, birthDate vb.) şimdilik Firestore'a
            // manuel ekleyebilir veya repo metodunu güncelleyebilirsin.
            val res = repo.signup(
                name = _name.value.trim(),
                email = _email.value.trim(),
                password = _password.value
            )

            if (res.isSuccess) {
                _uiState.value = UiState.Success(Unit)
                onSuccess() // Navigasyon için callback çağrısı
            } else {
                _uiState.value = UiState.Error(res.exceptionOrNull()?.message ?: "Kayıt başarısız")
            }
        }
    }
}