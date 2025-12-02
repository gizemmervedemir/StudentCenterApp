package com.example.studentcenterapp.appointment.di

import com.example.studentcenterapp.appointment.data.AppointmentDataSource
import com.example.studentcenterapp.appointment.data.AppointmentRepositoryImpl
import com.example.studentcenterapp.appointment.domain.AppointmentRepository

object ServiceLocator {

    private val appointmentDataSource: AppointmentDataSource by lazy {
        AppointmentDataSource()
    }

    val appointmentRepository: AppointmentRepository by lazy {
        AppointmentRepositoryImpl(appointmentDataSource)
    }
}
