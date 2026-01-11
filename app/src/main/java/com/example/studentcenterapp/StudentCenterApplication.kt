package com.example.studentcenterapp

import android.app.Application
import com.example.studentcenterapp.data.AppDI

class StudentCenterApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Dependency Injection
        AppDI.init(this)
    }
}

