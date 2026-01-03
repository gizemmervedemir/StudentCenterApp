package com.example.studentcenterapp.ui.staffauth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentcenterapp.data.auth.StaffAuthRepository
import com.example.studentcenterapp.ui.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StaffSignupViewModel(
    private val repo: StaffAuthRepository
) : ViewModel() {

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _uiState = MutableStateFlow<UiState<Unit>>(UiState.Success(Unit))
    val uiState: StateFlow<UiState<Unit>> = _uiState.asStateFlow()

    private val _navStaffId = MutableStateFlow<String?>(null)
    val navStaffId: StateFlow<String?> = _navStaffId.asStateFlow()

    fun onNameChange(v: String) { _name.value = v }
    fun onEmailChange(v: String) { _email.value = v }
    fun onPasswordChange(v: String) { _password.value = v }

    fun signup() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val res = repo.signup(_name.value.trim(), _email.value.trim(), _password.value)
            if (res.isSuccess) {
                _uiState.value = UiState.Success(Unit)
                _navStaffId.value = res.getOrNull()
            } else {
                _uiState.value = UiState.Error(res.exceptionOrNull()?.message ?: "Signup failed")
            }
        }
    }

    fun consumeNav() { _navStaffId.value = null }
}
