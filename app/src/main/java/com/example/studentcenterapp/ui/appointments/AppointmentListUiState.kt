package com.example.studentcenterapp.ui.appointments

import com.example.studentcenterapp.model.Appointment

enum class AppointmentListFilter {
    UPCOMING,
    PAST
}

data class AppointmentListUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val selectedFilter: AppointmentListFilter = AppointmentListFilter.UPCOMING,

    val upcomingAppointments: List<Appointment> = emptyList(),
    val pastAppointments: List<Appointment> = emptyList()
) {
    val visibleAppointments: List<Appointment>
        get() = if (selectedFilter == AppointmentListFilter.UPCOMING) {
            upcomingAppointments
        } else {
            pastAppointments
        }

    val isEmpty: Boolean
        get() = !isLoading && errorMessage == null && visibleAppointments.isEmpty()
}
