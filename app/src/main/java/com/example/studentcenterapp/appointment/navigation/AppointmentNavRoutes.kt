package com.example.studentcenterapp.appointment.navigation

sealed class AppointmentNavRoute(val route: String) {
    object List : AppointmentNavRoute("appointment_list")
    object Confirm : AppointmentNavRoute("appointment_confirm")
}
