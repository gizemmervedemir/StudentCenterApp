package com.example.studentcenterapp.data

import com.example.studentcenterapp.data.auth.FirebaseAuthStaffAuthRepository
import com.example.studentcenterapp.data.auth.StaffAuthRepository
import com.example.studentcenterapp.data.department.*
import com.example.studentcenterapp.data.service.*
import com.example.studentcenterapp.data.staff.*
import com.example.studentcenterapp.data.student.*
import com.example.studentcenterapp.data.appointment.*
import com.example.studentcenterapp.data.chat.ChatRepository // Chat için gerekli import
import com.google.firebase.firestore.FirebaseFirestore

object AppDI {

    // --- 1. DataSources (Veri Kaynakları) ---

    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val departmentDataSource: DepartmentDataSource = FirestoreDepartmentDataSource()
    private val serviceDataSource: ServiceDataSource = FirestoreServiceDataSource()
    private val appointmentFirestoreDataSource = FirestoreAppointmentDataSource()

    // Student için Firestore tabanlı bir veri kaynağı (Gelecekte değiştirilebilir)
    private val studentDataSource: StudentDataSource = InMemoryStudentDataSource()



    // --- 2. Repositories (Depolar) ---

    // Departman (Bölüm) İşlemleri
    val departmentRepository: DepartmentRepository by lazy {
        DepartmentRepositoryImpl(departmentDataSource)
    }

    // Servis (Hizmet) İşlemleri
    val serviceRepository: ServiceRepository by lazy {
        ServiceRepositoryImpl(serviceDataSource)
    }

    // Öğrenci Profil ve Veri İşlemleri
    val studentRepository: StudentRepository by lazy {
        StudentRepositoryImpl(FirebaseFirestore.getInstance())
    }

    // Randevu İşlemleri (Hem Öğrenci hem Personel için Ortak)
    val appointmentRepository: AppointmentRepository by lazy {
        AppointmentRepositoryImpl(appointmentFirestoreDataSource)
    }

    // Personel Dashboard ve Onay İşlemleri
    val staffRepository: StaffRepository by lazy {
        StaffRepositoryImpl(FirebaseFirestore.getInstance())
    }

    // --- 3. Mesajlaşma (Chat) ---

    // Mesajlaşma işlemlerini yürüten repository
    val chatRepository: ChatRepository by lazy {
        ChatRepository(db = firestore)
    }

    // --- 4. Kimlik Doğrulama (Auth) ---

    // Personel Giriş ve Kayıt İşlemleri
    val staffAuthRepository: StaffAuthRepository by lazy {
        FirebaseAuthStaffAuthRepository()
    }
}