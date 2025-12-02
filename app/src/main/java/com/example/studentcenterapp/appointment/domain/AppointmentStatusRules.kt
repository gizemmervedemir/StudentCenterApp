package com.example.studentcenterapp.appointment.domain

object AppointmentStatusRules {
    fun canCancel(status: AppointmentStatus): Boolean =
        status == AppointmentStatus.PENDING || status == AppointmentStatus.CONFIRMED

    fun nextAfterCreate(): AppointmentStatus = AppointmentStatus.PENDING
}