package com.example.studentcenterapp.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Departments : Screen("departments")
    object Services : Screen("services")
    object Slots : Screen("slots")
    object Confirm : Screen("confirm")
    object Appointments : Screen("appointments")
    object StaffDashboard : Screen("staffDashboard")
}