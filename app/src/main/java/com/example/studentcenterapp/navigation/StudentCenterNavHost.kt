package com.example.studentcenterapp.navigation

import com.example.studentcenterapp.ui.staff.StaffDashboardScreen
import com.example.studentcenterapp.ui.staff.StaffDashboardViewModel
import com.example.studentcenterapp.ui.staff.StaffDashboardViewModelFactory
import androidx.compose.runtime.LaunchedEffect
import com.example.studentcenterapp.ui.service.ServiceListScreen
import com.example.studentcenterapp.ui.service.ServiceListViewModel
import com.example.studentcenterapp.ui.service.ServiceListViewModelFactory
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.studentcenterapp.ui.theme.StudentCenterTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studentcenterapp.data.AppDI
import com.example.studentcenterapp.ui.splash.SplashScreen
import com.example.studentcenterapp.ui.splash.WelcomeScreen
import com.example.studentcenterapp.ui.student.ForgotPasswordEmailScreen
import com.example.studentcenterapp.ui.student.SignupSuccessScreen
import com.example.studentcenterapp.ui.student.StudentLoginScreen
import com.example.studentcenterapp.ui.student.StudentSignupScreen
import com.example.studentcenterapp.viewmodel.department.DepartmentListScreen
import com.example.studentcenterapp.viewmodel.department.DepartmentListViewModel
import com.example.studentcenterapp.viewmodel.department.DepartmentListViewModelFactory
import com.example.studentcenterapp.viewmodel.splash.SplashViewModel
import com.example.studentcenterapp.viewmodel.student.ForgotPasswordViewModel
import com.example.studentcenterapp.viewmodel.student.StudentLoginViewModel
import com.example.studentcenterapp.viewmodel.student.StudentSignupViewModel
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
                onStudentClick = { navController.navigate(Screen.StudentLogin.route)
                },
                onStaffClick = {
                    //navController.navigate("staffDashboard/staff1") //gizem
                    // deneme navController.navigate(Screen.StudentHome.route)
                }
            )
        }

        composable(Screen.StudentLogin.route) {
            val loginViewModel: StudentLoginViewModel = viewModel(
                factory = StudentLoginViewModel.Factory
            )

            StudentLoginScreen(
                viewModel = loginViewModel,
                onSignupClick = {
                    navController.navigate(Screen.StudentSignup.route)
                },
                onForgotPasswordClick = {
                    // İŞTE BURADA: İlk tasarladığımız ResetPassword ekranına gönderiyoruz
                    navController.navigate(Screen.ForgotPasswordEmail.route)
                },
                onLoginSuccess = {
                    // Giriş başarılı! Şimdi ana ekrana gönderiyoruz.
                    navController.navigate(Screen.Departments.route) {
                        // Welcome ve Login ekranlarını geri tuşu yığınından siliyoruz
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.SignupSuccess.route) {
            SignupSuccessScreen(
                onLoginClick = {
                    // Kullanıcıyı Login ekranına geri gönderir
                    navController.navigate(Screen.StudentLogin.route) {
                        // Bu kısım çok önemli: Geri tuşuna basınca tekrar başarı ekranı gelmesin diye
                        // Signup ve Success ekranlarını yığından temizleriz.
                        popUpTo(Screen.Welcome.route) { inclusive = true } // Login ekranını da yığından temizleyip yeniden oluştur
                        launchSingleTop = true // Aynı ekranı üst üste yığma
                    }
                }
            )
        }

        composable(Screen.ForgotPasswordEmail.route) {
            val forgotPasswordViewModel: ForgotPasswordViewModel = viewModel()
            ForgotPasswordEmailScreen(
                viewModel = forgotPasswordViewModel,
                onCodeSent = { navController.navigate(Screen.ForgotPasswordCode.route) },
                onBackClick = { navController.popBackStack() }
            )
        }
        // NavHost dosyanın içine, StudentLogin composable'ından hemen sonra ekle:
        composable(Screen.StudentSignup.route) {
            // ViewModel'i factory ile bağlıyoruz (Issue'da istendiği gibi)
            val signupViewModel: StudentSignupViewModel = viewModel(
                factory = StudentSignupViewModel.Factory
            )

            StudentSignupScreen(
                viewModel = signupViewModel,
                onSignupSuccess = {
                    // Başarılı olduğunda SignupSuccess ekranına yönlendir
                    navController.navigate(Screen.SignupSuccess.route) {
                        popUpTo(Screen.StudentSignup.route) { inclusive = true } // Signup ekranını yığından temizle
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }
        //same
        composable(Screen.Departments.route) {

            val vm: DepartmentListViewModel = viewModel(
                factory = DepartmentListViewModelFactory(AppDI.departmentRepository)
            )

            val state by vm.uiState.collectAsState()


            DepartmentListScreen(
                state = state,
                currentRoute = Screen.Departments.route,
                onTabSelected = { tab ->
                    navController.navigate(tab.route) {
                        launchSingleTop = true
                    }
                },
                onDepartmentClick = { departmentId ->
                    // Şimdilik services yoksa placeholder'a gidebilirsin
                    navController.navigate("services/$departmentId")
                }
            )
        }

        composable("services/{departmentId}") { backStackEntry ->
            val departmentId = backStackEntry.arguments?.getString("departmentId").orEmpty()

            val vm: ServiceListViewModel = viewModel(
                factory = ServiceListViewModelFactory(AppDI.serviceRepository)
            )
            val state by vm.uiState.collectAsState()

            LaunchedEffect(departmentId) {
                if (departmentId.isNotBlank()) vm.loadServices(departmentId)
            }

            ServiceListScreen(
                state = state,
                currentRoute = Screen.Services.route,
                onTabSelected = { tab ->
                    navController.navigate(tab.route) { launchSingleTop = true }
                },
                onServiceClick = { serviceId ->
                    navController.navigate("slots/$serviceId")
                }
            )
        }

        composable("staffDashboard/{staffId}") { backStackEntry ->
            val staffId = backStackEntry.arguments?.getString("staffId").orEmpty()

            val vm: StaffDashboardViewModel = viewModel(
                factory = StaffDashboardViewModelFactory(staffId, AppDI.staffRepository)
            )

            val state by vm.uiState.collectAsState()
            val actionLoading by vm.actionLoading.collectAsState()
            val actionError by vm.actionError.collectAsState()

            StaffDashboardScreen(
                state = state,
                actionLoading = actionLoading,
                actionError = actionError,
                currentRoute = Screen.StaffDashboard.route,
                onTabSelected = { tab ->
                    navController.navigate(tab.route) { launchSingleTop = true }
                },
                onApprove = { vm.approve(it) },
                onReject = { vm.reject(it) }
            )

        }



        composable(Screen.Services.route) {
            PlaceholderScreen("Services")
        }

        composable("slots/{serviceId}") { backStackEntry ->
            val serviceId = backStackEntry.arguments?.getString("serviceId").orEmpty()
            PlaceholderScreen("Slots for serviceId = $serviceId")
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