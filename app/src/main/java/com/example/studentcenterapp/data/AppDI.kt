package com.example.studentcenterapp.data

import com.example.studentcenterapp.data.auth.FirebaseAuthStaffAuthRepository
import com.example.studentcenterapp.data.auth.StaffAuthRepository
import com.example.studentcenterapp.data.department.*
import com.example.studentcenterapp.data.service.*
import com.example.studentcenterapp.data.staff.*
import com.example.studentcenterapp.data.student.*
import com.example.studentcenterapp.data.appointment.*
import com.example.studentcenterapp.data.chat.ChatRepository // Chat için gerekli import
import com.example.studentcenterapp.data.inmemory.InMemoryDataSource
import com.example.studentcenterapp.data.timeslot.TimeSlotRepository
import com.example.studentcenterapp.data.timeslot.TimeSlotRepositoryImpl
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
        StudentRepositoryImpl(studentDataSource)
    }

    // Randevu İşlemleri (Hem Öğrenci hem Personel için Ortak)
    val appointmentRepository: AppointmentRepository by lazy {
        AppointmentRepositoryImpl(appointmentFirestoreDataSource)
    }

    // Personel Dashboard ve Onay İşlemleri
    val staffRepository: StaffRepository by lazy {
        StaffRepositoryImpl(appointmentFirestoreDataSource)
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

    // 1. Paylaşılan Veri Kaynağı (Singleton)

    val timeSlotDataSource: InMemoryDataSource by lazy {
        InMemoryDataSource().apply {
            seedTimeSlots(generateMockTimeSlots())
        }
    }

    // 2. Repository Tanımı
    val timeSlotRepository: TimeSlotRepository by lazy {
        TimeSlotRepositoryImpl(timeSlotDataSource)
    }

    private fun generateMockTimeSlots(): List<com.example.studentcenterapp.model.TimeSlot> {
        // Firestore'dan gelen gerçek ID'ler (Paylaştığın listedeki 'id' alanları)
        val realServiceIds = listOf("101", "201", "301", "401", "501", "601", "701", "801")

        val dates = listOf("2026-01-20", "2026-01-21", "2026-01-22")
        val times = listOf("09:00", "10:00", "11:00", "14:00", "15:00", "16:00")

        val slots = mutableListOf<com.example.studentcenterapp.model.TimeSlot>()
        var idCounter = 1

        realServiceIds.forEach { sId ->
            dates.forEach { dStr ->
                times.forEach { tStr ->
                    slots.add(
                        com.example.studentcenterapp.model.TimeSlot(
                            id = "slot_${idCounter++}",
                            serviceId = sId, // Burası artık "601", "401" vb. olacak
                            date = dStr,
                            startTime = tStr,
                            endTime = "${tStr.split(":")[0].toInt() + 1}:00",
                            isReserved = false
                        )
                    )
                }
            }
        }
        return slots
    }

}