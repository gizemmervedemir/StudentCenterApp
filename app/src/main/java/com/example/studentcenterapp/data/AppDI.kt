package com.example.studentcenterapp.data

import com.example.studentcenterapp.data.auth.FirebaseAuthStaffAuthRepository
import com.example.studentcenterapp.data.auth.StaffAuthRepository
import com.example.studentcenterapp.data.department.*
import com.example.studentcenterapp.data.service.*
import com.example.studentcenterapp.data.staff.*
import com.example.studentcenterapp.data.student.*
import com.example.studentcenterapp.data.appointment.*
import com.example.studentcenterapp.data.chat.ChatRepository
import com.example.studentcenterapp.data.inmemory.InMemoryDataSource
import com.example.studentcenterapp.data.timeslot.TimeSlotRepository
import com.example.studentcenterapp.data.timeslot.TimeSlotRepositoryImpl
import com.google.firebase.firestore.FirebaseFirestore

object AppDI {

    // --- 1. Veri Kaynakları (DataSources) ---
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private val departmentDataSource: DepartmentDataSource by lazy { FirestoreDepartmentDataSource() }
    private val serviceDataSource: ServiceDataSource by lazy { FirestoreServiceDataSource() }

    // Randevular için tek bir Firestore veri kaynağı
    private val appointmentFirestoreDataSource: FirestoreAppointmentDataSource by lazy {
        FirestoreAppointmentDataSource()
    }

    // --- 2. Depolar (Repositories) ---

    val departmentRepository: DepartmentRepository by lazy {
        DepartmentRepositoryImpl(departmentDataSource)
    }

    val serviceRepository: ServiceRepository by lazy {
        ServiceRepositoryImpl(serviceDataSource)
    }

    val studentRepository: StudentRepository by lazy {
        // Student verileri şu an için InMemory, Firestore'a geçirmek istersen değiştirilebilir
        StudentRepositoryImpl(InMemoryStudentDataSource())
    }

    // ✅ Firebase Randevu İşlemleri
    val appointmentRepository: AppointmentRepository by lazy {
        AppointmentRepositoryImpl(appointmentFirestoreDataSource)
    }

    val staffRepository: StaffRepository by lazy {
        StaffRepositoryImpl(appointmentFirestoreDataSource)
    }

    val chatRepository: ChatRepository by lazy {
        ChatRepository(db = firestore)
    }

    val staffAuthRepository: StaffAuthRepository by lazy {
        FirebaseAuthStaffAuthRepository()
    }

    // --- 3. Zaman Dilimi (TimeSlot) İşlemleri ---

    val timeSlotDataSource: InMemoryDataSource by lazy {
        InMemoryDataSource().apply {
            seedTimeSlots(generateMockTimeSlots())
        }
    }

    val timeSlotRepository: TimeSlotRepository by lazy {
        TimeSlotRepositoryImpl(timeSlotDataSource)
    }

    private fun generateMockTimeSlots(): List<com.example.studentcenterapp.model.TimeSlot> {
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
                            serviceId = sId,
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