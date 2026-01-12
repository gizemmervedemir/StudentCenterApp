package com.example.studentcenterapp.data

import com.example.studentcenterapp.data.auth.FakeStaffAuthRepository
import com.example.studentcenterapp.data.auth.StaffAuthRepository
import com.example.studentcenterapp.data.department.DepartmentRepository
import com.example.studentcenterapp.data.department.DepartmentRepositoryImpl
import com.example.studentcenterapp.data.department.InMemoryDepartmentDataSource
import com.example.studentcenterapp.data.service.InMemoryServiceDataSource
import com.example.studentcenterapp.data.service.ServiceRepository
import com.example.studentcenterapp.data.service.ServiceRepositoryImpl
import com.example.studentcenterapp.data.staff.InMemoryAppointmentAdminDataSource
import com.example.studentcenterapp.data.staff.StaffRepository
import com.example.studentcenterapp.data.staff.StaffRepositoryImpl
import com.example.studentcenterapp.data.student.InMemoryStudentDataSource
import com.example.studentcenterapp.data.student.StudentRepository
import com.example.studentcenterapp.data.student.StudentRepositoryImpl

// ✅ Appointment
import com.example.studentcenterapp.data.appointment.AppointmentRepository
import com.example.studentcenterapp.data.appointment.AppointmentRepositoryImpl
import com.example.studentcenterapp.data.appointment.InMemoryAppointmentDataSource
import com.example.studentcenterapp.data.department.DepartmentDataSource
import com.example.studentcenterapp.data.department.FirestoreDepartmentDataSource
import com.example.studentcenterapp.data.service.FirestoreServiceDataSource
import com.example.studentcenterapp.data.service.ServiceDataSource

object AppDI {

    private val departmentDataSource: DepartmentDataSource = FirestoreDepartmentDataSource()
    private val serviceDataSource: ServiceDataSource = FirestoreServiceDataSource()

    // 2. Orta katman: Repository (DataSource'u içine alıyor)
    val departmentRepository: DepartmentRepository by lazy {
        DepartmentRepositoryImpl(departmentDataSource)
    }

    val serviceRepository: ServiceRepository by lazy {
        ServiceRepositoryImpl(serviceDataSource)
    }

    val studentRepository: StudentRepository by lazy {
        StudentRepositoryImpl(InMemoryStudentDataSource())
    }

    val staffRepository: StaffRepository by lazy {
        StaffRepositoryImpl(InMemoryAppointmentAdminDataSource())
    }

    val staffAuthRepository: StaffAuthRepository by lazy {
        FakeStaffAuthRepository()
    }

    // ✅ Appointment repository (Confirm + Appointments list için kullanılacak)
    val appointmentRepository: AppointmentRepository by lazy {
        AppointmentRepositoryImpl(InMemoryAppointmentDataSource())
    }
}
