package com.example.studentcenterapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.studentcenterapp.data.inmemory.InMemoryDataSource
import com.example.studentcenterapp.data.timeslot.TimeSlotRepositoryImpl
import com.example.studentcenterapp.ui.timeslot.timeSlotGraph

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    // Uygulama boyunca tek bir dataSource & repository kullanıyoruz
    val dataSource = remember { InMemoryDataSource() }
    val repository = remember { TimeSlotRepositoryImpl(dataSource) }

    NavHost(
        navController = navController,
        // timeSlotGraph içinde tanımladığımız pattern:
        // "timeSlotSelection/{serviceId}"
        startDestination = "timeSlotSelection/service-1"
    ) {
        timeSlotGraph(
            navController = navController,
            timeSlotRepository = repository
        )
    }
}