package com.example.studentcenterapp.ui.appointments

import com.example.studentcenterapp.data.appointment.AppointmentRecord

enum class AppointmentListFilter {
    UPCOMING,
    PAST
}

data class AppointmentListUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val selectedFilter: AppointmentListFilter = AppointmentListFilter.UPCOMING,

    val upcomingAppointments: List<AppointmentRecord> = emptyList(),
    val pastAppointments: List<AppointmentRecord> = emptyList()
) {
    val visibleAppointments: List<AppointmentRecord>
        get() = if (selectedFilter == AppointmentListFilter.UPCOMING) {
            upcomingAppointments
        } else {
            pastAppointments
        }

    val isEmpty: Boolean
        get() = !isLoading && errorMessage == null && visibleAppointments.isEmpty()
}
