package com.example.studentcenterapp.data

import com.example.studentcenterapp.data.auth.FirebaseAuthStaffAuthRepository // Yeni yaratacağımız
import com.example.studentcenterapp.data.auth.StaffAuthRepository
import com.example.studentcenterapp.data.department.*
import com.example.studentcenterapp.data.service.*
import com.example.studentcenterapp.data.staff.*
import com.example.studentcenterapp.data.student.*
import com.example.studentcenterapp.data.appointment.*

object AppDI {

    // 1. DataSources (Gerçek Veri Kaynakları)
    private val departmentDataSource: DepartmentDataSource = FirestoreDepartmentDataSource()
    private val serviceDataSource: ServiceDataSource = FirestoreServiceDataSource()
    private val appointmentFirestoreDataSource = FirestoreAppointmentDataSource()
    // Not: StudentDataSource için de Firestore versiyonu gerekirse ekleyeceğiz.

    // 2. Repositories
    val departmentRepository: DepartmentRepository by lazy {
        DepartmentRepositoryImpl(departmentDataSource)
    }

    val serviceRepository: ServiceRepository by lazy {
        ServiceRepositoryImpl(serviceDataSource)
    }

    // Burayı da Firestore tabanlı yapmalıyız
    val studentRepository: StudentRepository by lazy {
        StudentRepositoryImpl(InMemoryStudentDataSource()) // TODO: FirestoreStudentDataSource yaratılmalı
    }

    // PERSONEL DÜNYASI (GÜNCEL)
    val staffRepository: StaffRepository by lazy {
        StaffRepositoryImpl(appointmentFirestoreDataSource)
    }

    // AUTH (GÜNCEL)

    // 1. Repository Tanımları
    val staffAuthRepository: StaffAuthRepository by lazy {
        FirebaseAuthStaffAuthRepository()
    }

    val appointmentRepository: AppointmentRepository by lazy {
        // Hem personel hem öğrenci artık aynı Firestore motorunu kullanıyor
        AppointmentRepositoryImpl(appointmentFirestoreDataSource)
    }
}