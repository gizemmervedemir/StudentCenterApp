package com.example.studentcenterapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.studentcenterapp.ui.navigation.AppNavHost
import com.example.studentcenterapp.ui.theme.StudentCenterAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudentCenterAppTheme {
                AppNavHost()
            }
        }
    }
}