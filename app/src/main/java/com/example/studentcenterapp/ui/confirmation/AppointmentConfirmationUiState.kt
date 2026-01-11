package com.example.studentcenterapp.ui.confirmation

data class AppointmentConfirmationUiState(
    val studentId: String = "",

    val departmentName: String = "",
    val serviceId: String = "",
    val serviceName: String = "",

    val timeSlotId: String = "",
    val scheduledStartMillis: Long = 0L,
    val dateTimeText: String = "",

    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
) {
    fun isValid(): Boolean {
        return studentId.isNotBlank() &&
                serviceId.isNotBlank() &&
                timeSlotId.isNotBlank() &&
                scheduledStartMillis > 0L
    }
}
