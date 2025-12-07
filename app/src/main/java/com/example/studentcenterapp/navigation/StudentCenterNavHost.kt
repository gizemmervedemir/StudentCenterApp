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
import com.example.studentcenterapp.ui.theme.StudentCenterTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studentcenterapp.ui.splash.SplashScreen
import com.example.studentcenterapp.ui.splash.WelcomeScreen
import com.example.studentcenterapp.viewmodel.splash.SplashViewModel
@Composable
fun StudentCenterApp() {
    StudentCenterTheme {
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
            val splashViewModel: SplashViewModel = viewModel()

            SplashScreen(
                onFinished = {
                    navController.navigate(Screen.Welcome.route) {
                        // Geri tuşuna basınca Splash’e dönmeyelim
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                viewModel = splashViewModel
            )
        }

        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onStudentClick = {
                    // TODO: ileride Students/Departments vs.
                },
                onStaffClick = {
                    // TODO: ileride Staff login
                }
            )
        }


        //same
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