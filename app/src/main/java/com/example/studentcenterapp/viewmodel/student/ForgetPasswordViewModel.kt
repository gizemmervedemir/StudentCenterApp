package com.example.studentcenterapp.viewmodel.student

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ForgotPasswordViewModel : ViewModel() {

    // 1. Ekran: E-posta girişi
    var email by mutableStateOf("")
    var emailError by mutableStateOf(false)

    // 2. Ekran: 4 Haneli Kod (Her hane için ayrı state)
    var code1 by mutableStateOf("")
    var code2 by mutableStateOf("")
    var code3 by mutableStateOf("")
    var code4 by mutableStateOf("")
    var codeError by mutableStateOf(false)

    // 3. Ekran: Yeni Şifre
    var newPassword by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    var passwordError by mutableStateOf(false)

    // Genel Durumlar
    var errorMessage by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(false)

    // --- FONKSİYONLAR ---

    // E-posta Gönder (1. Ekran)
    fun sendResetCode(onSuccess: () -> Unit) {
        emailError = false
        errorMessage = null

        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        if (email.isBlank() || !email.matches(emailPattern.toRegex())) {
            emailError = true
            errorMessage = "Lütfen geçerli bir e-posta adresi giriniz."
            return
        }

        viewModelScope.launch {
            isLoading = true
            val auth = com.google.firebase.auth.FirebaseAuth.getInstance()

            // Firebase şifre sıfırlama mailini gönderir
            auth.sendPasswordResetEmail(email)
                .addOnSuccessListener {
                    isLoading = false
                    // Mail başarıyla gitti!
                    // Burada kullanıcıya "Mailinizi kontrol edin" mesajı gösterip
                    // Success ekranına veya Login'e yönlendirebiliriz.
                    onSuccess()
                }
                .addOnFailureListener { e ->
                    isLoading = false
                    errorMessage = "Hata: ${e.localizedMessage}"
                }
        }
    }

//    // Kodu Doğrula (2. Ekran)
//    fun verifyCode(onSuccess: () -> Unit) {
//        codeError = false
//        val fullCode = code1 + code2 + code3 + code4
//
//        if (fullCode.length < 4) {
//            codeError = true
//            errorMessage = "Lütfen 4 haneli kodu eksiksiz giriniz."
//            return
//        }
//
//        viewModelScope.launch {
//            isLoading = true
//            // Kod doğrulama isteği...
//            isLoading = false
//            onSuccess()
//        }
//    }
//
//    // Şifreyi Güncelle (3. Ekran)
//    fun updatePassword(onSuccess: () -> Unit) {
//        passwordError = false
//        errorMessage = null
//
//        if (newPassword.length < 6 || newPassword != confirmPassword) {
//            passwordError = true
//            errorMessage = if (newPassword.length < 6) "Şifre en az 6 karakter olmalıdır." else "Şifreler uyuşmuyor."
//            return
//        }
//
//        viewModelScope.launch {
//            isLoading = true
//            // Şifre güncelleme isteği...
//            isLoading = false
//            onSuccess()
//        }
//    }
}