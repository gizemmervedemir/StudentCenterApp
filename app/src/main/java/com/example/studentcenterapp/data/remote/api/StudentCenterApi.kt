package com.example.studentcenterapp.data.remote.api

import com.example.studentcenterapp.model.Student
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit API interface for Mockoon backend
 * Base URL: http://localhost:3000 (or your Mockoon server URL)
 */
interface StudentCenterApi {

    // ========== Authentication ==========
    
    @POST("students/login")
    suspend fun loginStudent(
        @Body request: LoginRequest
    ): StudentResponse

    @POST("students/signup")
    suspend fun signupStudent(
        @Body request: SignupRequest
    ): StudentResponse

    @POST("staff/login")
    suspend fun loginStaff(
        @Body request: LoginRequest
    ): StaffResponse

    @POST("staff/signup")
    suspend fun signupStaff(
        @Body request: SignupRequest
    ): StaffResponse

    // ========== Students ==========
    
    @GET("students")
    suspend fun getStudents(): List<StudentResponse>

    @GET("students/{id}")
    suspend fun getStudentById(@Path("id") id: String): StudentResponse

    // ========== Appointments ==========
    
    @GET("students/{studentId}/appointments")
    suspend fun getStudentAppointments(@Path("studentId") studentId: String): List<AppointmentResponse>

    @GET("appointments/{id}")
    suspend fun getAppointmentById(@Path("id") id: String): AppointmentResponse

    @POST("appointments")
    suspend fun createAppointment(@Body request: CreateAppointmentRequest): AppointmentResponse

    @POST("appointments/{id}/cancel")
    suspend fun cancelAppointment(@Path("id") id: String): ResultResponse

    // ========== TimeSlots ==========
    
    @GET("services/{serviceId}/timeslots")
    suspend fun getTimeSlotsByService(
        @Path("serviceId") serviceId: String,
        @Query("date") date: String? = null
    ): List<TimeSlotResponse>
}

// ========== Request/Response DTOs ==========

data class LoginRequest(
    val email: String,
    val password: String
)

data class SignupRequest(
    val name: String,
    val surname: String,
    val email: String,
    val password: String,
    val studentNumber: String? = null,
    val birthDay: String? = null,
    val birthMonth: String? = null,
    val birthYear: String? = null
)

data class StudentResponse(
    val id: String,
    val name: String,
    val email: String,
    val studentNumber: String? = null
)

data class StaffResponse(
    val id: String,
    val name: String,
    val email: String,
    val role: String? = null
)

data class AppointmentResponse(
    val id: String,
    val studentId: String,
    val serviceId: String,
    val timeSlotId: String,
    val date: String? = null,
    val scheduledStartMillis: Long? = null,
    val status: String // "pending", "approved", "cancelled"
)

data class CreateAppointmentRequest(
    val studentId: String,
    val serviceId: String,
    val timeSlotId: String,
    val scheduledStartMillis: Long? = null,
    val date: String? = null
)

data class TimeSlotResponse(
    val id: String,
    val serviceId: String,
    val date: String,
    val startTime: String,
    val endTime: String,
    val isAvailable: Boolean
)

data class ResultResponse(
    val success: Boolean,
    val message: String? = null
)

