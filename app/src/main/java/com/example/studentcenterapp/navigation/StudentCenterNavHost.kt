package com.example.studentcenterapp.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.studentcenterapp.ui.theme.StudentCenterAppTheme


@Composable
fun StudentCenterApp() {
    StudentCenterAppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            val navController = rememberNavController()
            StudentCenterNavHost(navController)
        }
    }
}

@Composable
fun StudentCenterNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            PlaceholderScreen("Splash")
        }
        composable(Screen.Departments.route) {
            PlaceholderScreen("Departments")
        }
        composable(Screen.Services.route) {
            PlaceholderScreen("Services")
        }
        composable(Screen.Slots.route) {
            PlaceholderScreen("Slots")
        }
        composable(Screen.Confirm.route) {
            PlaceholderScreen("Confirm")
        }
        composable(Screen.Appointments.route) {
            PlaceholderScreen("Appointments")
        }
        composable(Screen.StaffDashboard.route) {
            PlaceholderScreen("Staff Dashboard")
        }
    }
}

@Composable
private fun PlaceholderScreen(name: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "TODO: $name screen")
    }
}
@Preview(showBackground = true)
@Composable
fun StudentCenterAppPreview() {
    StudentCenterApp()
}