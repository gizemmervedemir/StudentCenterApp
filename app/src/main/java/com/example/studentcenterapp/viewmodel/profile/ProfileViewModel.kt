package com.example.studentcenterapp.viewmodel.profile

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentcenterapp.data.staff.StaffRepository
import com.example.studentcenterapp.data.student.StudentRepository
import com.example.studentcenterapp.data.student.StudentSession
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val studentRepo: StudentRepository,
    private val staffRepo: StaffRepository
) : ViewModel() {

    var uiState by mutableStateOf<ProfileUiState>(ProfileUiState.Loading)
        private set

    fun loadProfile(id: String, isStaff: Boolean) {
        println("DEBUG: loadProfile çağrıldı. ID: $id, isStaff: $isStaff")
        viewModelScope.launch {
            if (isStaff) {
                val staff = staffRepo.getStaffById(id)
                uiState = if (staff != null) ProfileUiState.Success(name = staff.name, email = "Staff Account", id = staff.id)
                else ProfileUiState.Error("Personel bulunamadı")
            } else {
                val student = studentRepo.getStudentById(id)
                uiState = if (student != null) ProfileUiState.Success(name = student.name, email = student.email, id = student.id)
                else ProfileUiState.Error("Öğrenci bulunamadı")
            }
        }
    }

    fun logout(onSuccess: () -> Unit) {
        StudentSession.currentStudentId = ""
        // Varsa StaffSession da temizlenmeli
        onSuccess()
    }

    fun updateProfile(id: String, newName: String, newEmail: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = studentRepo.updateStudentProfile(id, newName, newEmail)
            if (result.isSuccess) {
                // Başarılıysa state'i güncelle ki geri döndüğümüzde yeni isim görünsün
                loadProfile(id, false)
                onComplete(true)
            } else {
                onComplete(false)
            }
        }
    }
    fun uploadProfileImage(id: String, imageUri: Uri) {
        val storageRef = FirebaseStorage.getInstance().reference.child("profile_images/$id.jpg")

        storageRef.putFile(imageUri).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { url ->
                // Resim URL'sini Firestore'da kullanıcının dökümanına kaydet
                FirebaseFirestore.getInstance().collection("users").document(id)
                    .update("profilePictureUrl", url.toString())
            }
        }
    }
    fun updatePersonalInfo(
        id: String,
        fullName: String,
        birthDay: String,
        birthMonth: String,
        birthYear: String,
        onComplete: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // ✅ SENDE "users" var demiştin
                FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(id)
                    .update(
                        mapOf(
                            "fullName" to fullName,
                            "birthDay" to birthDay,
                            "birthMonth" to birthMonth,
                            "birthYear" to birthYear
                        )
                    )
                    .addOnSuccessListener { onComplete(true) }
                    .addOnFailureListener { onComplete(false) }

            } catch (_: Exception) {
                onComplete(false)
            }
        }
    }
    fun updatePassword(
        oldPassword: String,
        newPassword: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        val user = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
        val email = user?.email

        if (user != null && email != null) {
            // 1. Adım: Kullanıcının kimliğini eski şifreyle doğrula (Re-authentication)
            val credential = com.google.firebase.auth.EmailAuthProvider.getCredential(email, oldPassword)

            user.reauthenticate(credential)
                .addOnSuccessListener {
                    // 2. Adım: Doğrulama başarılıysa yeni şifreyi güncelle
                    user.updatePassword(newPassword)
                        .addOnSuccessListener {
                            onResult(true, null)
                        }
                        .addOnFailureListener { e ->
                            onResult(false, "Şifre güncelleme hatası: ${e.localizedMessage}")
                        }
                }
                .addOnFailureListener {
                    onResult(false, "Eski şifre yanlış.")
                }
        } else {
            onResult(false, "Kullanıcı oturumu bulunamadı.")
        }
    }
}

sealed class ProfileUiState {
    object Loading : ProfileUiState()
    data class Success(val name: String, val email: String, val id: String) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}
