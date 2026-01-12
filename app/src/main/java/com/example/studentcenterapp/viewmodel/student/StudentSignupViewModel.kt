package com.example.studentcenterapp.viewmodel.student

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.studentcenterapp.data.AppDI
import com.example.studentcenterapp.data.student.StudentRepository
import kotlinx.coroutines.launch
import java.util.Calendar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp

class StudentSignupViewModel(private val repository: StudentRepository) : ViewModel() {
    var email by mutableStateOf("")
    var fullName by mutableStateOf("")
    var schoolNumber by mutableStateOf("")

    // Figma'daki 3'lü kutu: Doğum Yılı, Gün, Ay
    var birthYear by mutableStateOf("")
    var birthDay by mutableStateOf("")
    var birthMonth by mutableStateOf("")

    var password by mutableStateOf("")
    var passwordConfirm by mutableStateOf("")

    var errorMessage by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(false)

    var showDatePicker by mutableStateOf(false)

    var emailError by mutableStateOf(false)
    var fullNameError by mutableStateOf(false)
    var schoolNumberError by mutableStateOf(false)
    var birthDateError by mutableStateOf(false)
    var passwordError by mutableStateOf(false)
    fun onSignupClick(onSuccess: () -> Unit) {
        // 1. Önce her şeyi temizle
        emailError = false
        fullNameError = false
        schoolNumberError = false
        birthDateError = false
        passwordError = false
        errorMessage = null

        var hasError = false // Herhangi bir hata var mı kontrolü için değişken

        // 2. Tüm kontrolleri arka arkaya yap (Return kullanma!)

        // Boş alan kontrolü
        if (email.isBlank()) { emailError = true; hasError = true }
        if (fullName.isBlank()) { fullNameError = true; hasError = true }
        if (schoolNumber.isBlank()) { schoolNumberError = true; hasError = true }
        if (birthDay.isBlank() || birthMonth.isBlank() || birthYear.isBlank()) { birthDateError = true; hasError = true }

        // E-posta format kontrolü
        val emailPattern = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
        if (!email.matches(emailPattern.toRegex())) {
            emailError = true
            hasError = true
        }
        if (schoolNumber.isBlank() || !schoolNumber.all { it.isDigit() }) {
            schoolNumberError = true
            hasError = true
        }
        // Şifre kontrolleri
        if (password.length < 6 || password != passwordConfirm) {
            passwordError = true
            hasError = true
        }

        // 3. EĞER HERHANGİ BİR HATA VARSA (Tek bir genel mesaj ver)
        if (hasError) {
            errorMessage = "Lütfen hatalı veya eksik alanları düzeltiniz."
            return // Burada dur, kayıt işlemini yapma
        }
        // Tüm kontroller geçerse kayıt işlemini başlat
        errorMessage = null
        viewModelScope.launch {
            isLoading = true
            val auth = com.google.firebase.auth.FirebaseAuth.getInstance()
            val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()

            // 1. Firebase Auth ile hesap aç
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { authResult ->
                    val userId = authResult.user?.uid ?: ""

                    // 2. Kullanıcı bilgilerini bir paket yap (İsimleri dilediğin gibi koyabilirsin!)
                    val studentData = hashMapOf(
                        "fullName" to fullName,
                        "email" to email,
                        "schoolNumber" to schoolNumber,
                        "birthDate" to "$birthDay/$birthMonth/$birthYear",
                        "role" to "student", // Bu çok önemli, personeli buradan ayıracağız
                        "createdAt" to com.google.firebase.Timestamp.now()
                    )

                    // 3. Firestore'da "users" diye bir yere bu ID ile kaydet
                    db.collection("users").document(userId).set(studentData)
                        .addOnSuccessListener {
                            isLoading = false
                            onSuccess() // Ekranda bir sonraki yere git (Home Screen)
                        }
                        .addOnFailureListener { e ->
                            isLoading = false
                            errorMessage = "Veritabanı hatası: ${e.message}"
                        }
                }
                .addOnFailureListener { e ->
                    isLoading = false
                    errorMessage = "Kayıt hatası: ${e.message}"
                }
        }
    }
    fun updateDate(millis: Long?) {
        millis?.let {
            val calendar = Calendar.getInstance().apply { timeInMillis = it }
            birthDay = calendar.get(Calendar.DAY_OF_MONTH).toString()
            birthMonth = (calendar.get(Calendar.MONTH) + 1).toString() // Ay 0'dan başlar
            birthYear = calendar.get(Calendar.YEAR).toString()

            birthDateError = false
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                // AppDI içindeki merkezi repository'i kullanarak ViewModel oluşturuyoruz
                return StudentSignupViewModel(AppDI.studentRepository) as T
            }
        }
    }
}