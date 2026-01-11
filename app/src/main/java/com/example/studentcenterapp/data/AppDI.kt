package com.example.studentcenterapp.data

import android.content.Context
import androidx.room.Room
import com.example.studentcenterapp.StudentCenterApplication
import com.example.studentcenterapp.data.auth.FakeStaffAuthRepository
import com.example.studentcenterapp.data.auth.StaffAuthRepository
import com.example.studentcenterapp.data.department.DepartmentRepository
import com.example.studentcenterapp.data.department.RoomDepartmentRepository
import com.example.studentcenterapp.data.local.database.AppDatabase
import com.example.studentcenterapp.data.local.preferences.UserSessionManager
import com.example.studentcenterapp.data.remote.api.StudentCenterApi
import com.example.studentcenterapp.data.service.RoomServiceRepository
import com.example.studentcenterapp.data.service.ServiceRepository
import com.example.studentcenterapp.data.staff.InMemoryAppointmentAdminDataSource
import com.example.studentcenterapp.data.staff.StaffRepository
import com.example.studentcenterapp.data.staff.StaffRepositoryImpl
import com.example.studentcenterapp.data.student.InMemoryStudentDataSource
import com.example.studentcenterapp.data.student.StudentRepository
import com.example.studentcenterapp.data.student.StudentRepositoryImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

// ✅ Appointment
import com.example.studentcenterapp.data.appointment.AppointmentRepository
import com.example.studentcenterapp.data.appointment.AppointmentRepositoryImpl
import com.example.studentcenterapp.data.appointment.InMemoryAppointmentDataSource

object AppDI {

    // ⚠️ Context lazy initialization - Application class'dan alınacak
    private var appContext: Context? = null

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    private val context: Context
        get() = appContext ?: throw IllegalStateException("AppDI.init() must be called first!")

    // ========== Room Database ==========
    val database: AppDatabase by lazy {
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration() // Development için - production'da migration yazılmalı
            .build()
        
        // Initialize database on first access (if empty)
        CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
            val initializer = com.example.studentcenterapp.data.local.database.DatabaseInitializer(db)
            initializer.populateDatabaseIfEmpty()
        }
        
        db
    }

    // ========== DataStore (Session Management) ==========
    val sessionManager: UserSessionManager by lazy {
        UserSessionManager(context)
    }

    // ========== Retrofit + Mockoon API ==========
    private val moshi: Moshi by lazy {
        Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }

    private val okHttpClient: OkHttpClient by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    val api: StudentCenterApi by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/") // Android Emulator için localhost
            // Production/Real Device için: "http://YOUR_IP:3000/"
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(StudentCenterApi::class.java)
    }

    // ========== Repositories (Hibrit: Room + Retrofit) ==========
    
    // ✅ Room-based repositories (sabit veriler)
    val departmentRepository: DepartmentRepository by lazy {
        RoomDepartmentRepository(database)
    }

    val serviceRepository: ServiceRepository by lazy {
        RoomServiceRepository(database)
    }

    // TODO: API-based repositories (dinamik veriler)
    val studentRepository: StudentRepository by lazy {
        StudentRepositoryImpl(InMemoryStudentDataSource()) // TODO: Retrofit API'ye geçilecek
    }

    val staffRepository: StaffRepository by lazy {
        StaffRepositoryImpl(InMemoryAppointmentAdminDataSource()) // TODO: Room + API'ye geçilecek
    }

    val staffAuthRepository: StaffAuthRepository by lazy {
        FakeStaffAuthRepository() // TODO: Retrofit API'ye geçilecek
    }

    val appointmentRepository: AppointmentRepository by lazy {
        AppointmentRepositoryImpl(InMemoryAppointmentDataSource()) // TODO: Retrofit API'ye geçilecek
    }
}
