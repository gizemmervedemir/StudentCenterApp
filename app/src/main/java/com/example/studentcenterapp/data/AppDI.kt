package com.example.studentcenterapp.data

import com.example.studentcenterapp.data.staff.InMemoryAppointmentAdminDataSource
import com.example.studentcenterapp.data.staff.StaffRepository
import com.example.studentcenterapp.data.staff.StaffRepositoryImpl
import com.example.studentcenterapp.data.department.DepartmentRepository
import com.example.studentcenterapp.data.department.DepartmentRepositoryImpl
import com.example.studentcenterapp.data.department.InMemoryDepartmentDataSource
import com.example.studentcenterapp.data.service.InMemoryServiceDataSource
import com.example.studentcenterapp.data.service.ServiceRepository
import com.example.studentcenterapp.data.service.ServiceRepositoryImpl
import com.example.studentcenterapp.data.student.InMemoryStudentDataSource
import com.example.studentcenterapp.data.student.StudentRepository
import com.example.studentcenterapp.data.student.StudentRepositoryImpl
import com.example.studentcenterapp.data.auth.FakeStaffAuthRepository
import com.example.studentcenterapp.data.auth.StaffAuthRepository


object AppDI {
    val departmentRepository: DepartmentRepository by lazy {
        DepartmentRepositoryImpl(InMemoryDepartmentDataSource())
    }
    val studentRepository: StudentRepository by lazy {
        StudentRepositoryImpl(InMemoryStudentDataSource())
    }
    val serviceRepository: ServiceRepository by lazy {
        ServiceRepositoryImpl(InMemoryServiceDataSource())
    }
    val staffRepository: StaffRepository by lazy {
        StaffRepositoryImpl(InMemoryAppointmentAdminDataSource())
    }

    val staffAuthRepository: StaffAuthRepository by lazy {
        FakeStaffAuthRepository()
    }

}