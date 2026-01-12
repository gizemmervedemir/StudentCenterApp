package com.example.studentcenterapp.navigation

sealed class Screen(val route: String) {

    object Splash : Screen("splash")
    object Welcome : Screen("welcome")

    object StudentLogin : Screen("student_login")
    object StudentSignup : Screen("student_signup")

    // (Sende zaten departments'e gidiyor)
    object StudentHome : Screen("departments")

    object StudentCalendar : Screen("student_calendar")
    object StudentChat : Screen("student_chat")
    object StudentProfile : Screen("student_profile")
    object SignupSuccess : Screen("signup_success")
    object ForgotPasswordEmail : Screen("forgot_password_email")
    object ForgotPasswordCode : Screen("forgot_password_code")
    object UpdatePassword : Screen("update_password")
    object UpdatePasswordSuccess : Screen("update_password_success")

    object Departments : Screen("departments")
    object Services : Screen("services")
    object Slots : Screen("slots")
    object Confirm : Screen("confirm")

    object Appointments : Screen("appointments")

    // ✅ NEW: Appointment detail route
    object AppointmentDetail : Screen("appointmentDetail/{appointmentId}") {
        fun createRoute(appointmentId: String): String = "appointmentDetail/$appointmentId"
    }

    object StaffDashboard : Screen("staffDashboard")
    object StaffLogin : Screen("staffLogin")
    object StaffSignup : Screen("staffSignup")
}