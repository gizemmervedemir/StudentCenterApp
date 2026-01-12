package com.example.studentcenterapp.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.studentcenterapp.data.AppDI
import com.example.studentcenterapp.data.inmemory.InMemoryDataSource
import com.example.studentcenterapp.data.student.StudentSession
import com.example.studentcenterapp.data.timeslot.TimeSlotRepositoryImpl
import com.example.studentcenterapp.ui.confirmation.AppointmentConfirmationViewModelFactory
import com.example.studentcenterapp.ui.screens.*
import com.example.studentcenterapp.ui.service.*
import com.example.studentcenterapp.ui.splash.*
import com.example.studentcenterapp.ui.staff.*
import com.example.studentcenterapp.ui.staffauth.*
import com.example.studentcenterapp.ui.student.*
import com.example.studentcenterapp.ui.theme.StudentCenterTheme
import com.example.studentcenterapp.viewmodel.appointment.*
import com.example.studentcenterapp.viewmodel.department.*
import com.example.studentcenterapp.viewmodel.splash.SplashViewModel
import com.example.studentcenterapp.viewmodel.student.*
import com.example.studentcenterapp.ui.appointment.AppointmentDetailRoute
import com.example.studentcenterapp.ui.student.PasswordResetSuccessScreen
private const val ARG_STUDENT_ID = "studentId"

private fun appointmentsRoute(studentId: String): String {
    return "${Screen.Appointments.route}?$ARG_STUDENT_ID=$studentId"
}

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
    // ✅ Repository ve DataSource'ları AppDI üzerinden merkezi yönetiyoruz
    val timeSlotDataSource = remember { InMemoryDataSource() }
    val timeSlotRepository = remember { TimeSlotRepositoryImpl(timeSlotDataSource) }

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {

        // --- Splash & Welcome ---
        composable(Screen.Splash.route) {
            val splashViewModel: SplashViewModel = viewModel()
            SplashScreen(
                onFinished = {
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                viewModel = splashViewModel
            )
        }

        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onStudentClick = { navController.navigate(Screen.StudentLogin.route) },
                onStaffClick = { navController.navigate(Screen.StaffLogin.route) }
            )
        }

        // --- Staff Auth ---
        composable(Screen.StaffLogin.route) {
            val vm: StaffLoginViewModel = viewModel(
                factory = StaffLoginViewModelFactory(AppDI.staffAuthRepository)
            )
            StaffLoginScreen(
                vm = vm,
                onSignupClick = { navController.navigate(Screen.StaffSignup.route) },
                onSuccess = { staffId ->
                    navController.navigate("staffDashboard/$staffId?entry=staff") {
                        popUpTo(Screen.StaffLogin.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.StaffSignup.route) {
            val vm: StaffSignupViewModel = viewModel(factory = StaffSignupViewModelFactory(AppDI.staffAuthRepository))
            StaffSignupScreen(
                vm = vm,
                onBackClick = { navController.popBackStack() },
                onSuccess = {
                    navController.navigate(Screen.SignupSuccess.route) {
                        popUpTo(Screen.StaffSignup.route) { inclusive = true }
                    }
                }
            )
        }

        // --- Student Auth ---
        composable(Screen.StudentLogin.route) {
            val loginViewModel: StudentLoginViewModel = viewModel(factory = StudentLoginViewModel.Factory)
            StudentLoginScreen(
                viewModel = loginViewModel,
                onSignupClick = { navController.navigate(Screen.StudentSignup.route) },
                onForgotPasswordClick = { navController.navigate(Screen.ForgotPasswordEmail.route) },
                onLoginSuccess = {
                    navController.navigate(Screen.Departments.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.StudentSignup.route) {
            val signupViewModel: StudentSignupViewModel = viewModel(factory = StudentSignupViewModel.Factory)
            StudentSignupScreen(
                viewModel = signupViewModel,
                onSignupSuccess = {
                    navController.navigate(Screen.SignupSuccess.route) {
                        popUpTo(Screen.StudentSignup.route) { inclusive = true }
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.SignupSuccess.route) {
            SignupSuccessScreen(onLoginClick = {
                navController.navigate(Screen.StudentLogin.route) {
                    popUpTo(Screen.Welcome.route) { inclusive = true }
                }
            })
        }

        composable(Screen.ForgotPasswordEmail.route) {
            val vm: ForgotPasswordViewModel = viewModel()
            ForgotPasswordEmailScreen(
                viewModel = vm,
                onCodeSent = { navController.navigate("passwordResetSuccess") },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("passwordResetSuccess") {
            PasswordResetSuccessScreen(onLoginClick = {
                navController.navigate(Screen.StudentLogin.route) {
                    popUpTo("passwordResetSuccess") { inclusive = true }
                }
            })
        }
        // --- Student Bottom Bar Screens ---

// --- Student Bottom Bar Screens ---
        composable(Screen.StudentCalendar.route) {
            // StudentSession'dan güncel giriş yapmış öğrencinin ID'sini alıyoruz
            val currentStudentId = StudentSession.currentStudentId ?: ""

            // ViewModel'ı AppDI üzerinden gelen repository ile kuruyoruz
            val vm: AppointmentListViewModel = viewModel(
                factory = AppointmentListViewModelFactory(
                    studentId = currentStudentId,
                    appointmentRepository = AppDI.appointmentRepository // Hata düzeldi ✅
                )
            )

            StudentCalendarScreen(
                navController = navController,
                viewModel = vm
            )
        }

        composable("cancelSuccess") {
            CancelSuccessScreen(
                onNavigateBack = {
                    navController.navigate(Screen.StudentCalendar.route) {
                        // Bu ekranı stack'ten atıyoruz ki geri tuşuyla tekrar başarı ekranına gelmesin
                        popUpTo("cancelSuccess") { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.StudentChat.route) {
            PlaceholderScreen(
                name = "Öğrenci Destek",
//                currentRoute = Screen.StudentChat.route,
//                navController = navController
            )
        }

        composable(Screen.StudentProfile.route) {
            PlaceholderScreen(
                name = "Öğrenci Profili",
//                currentRoute = Screen.StudentProfile.route,
//                navController = navController
            )
        }

        // --- Student Flow ---
        composable(Screen.Departments.route) {
            val vm: DepartmentListViewModel = viewModel(factory = DepartmentListViewModelFactory(AppDI.departmentRepository))
            val state by vm.uiState.collectAsState()
            val userName by vm.userName.collectAsState()

            DepartmentListScreen(
                state = state,
                userName = userName,
                currentRoute = Screen.Departments.route,
                onTabSelected = { tab -> navController.navigate(tab.route) {
                    // Ana sayfayı (Departments) root yaparak geri tuşu karmaşasını önleriz
                    popUpTo(Screen.Departments.route) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                } },
                onDepartmentClick = { deptId, deptName ->
                    val handle = navController.currentBackStackEntry?.savedStateHandle
                    handle?.set(ConfirmationNavKeys.KEY_DEPARTMENT_NAME, deptName)
                    handle?.set(ConfirmationNavKeys.KEY_STUDENT_ID, StudentSession.currentStudentId)
                    navController.navigate("services/$deptId")
                }
            )
        }

        composable("services/{departmentId}") { backStackEntry ->
            val departmentId = backStackEntry.arguments?.getString("departmentId").orEmpty()
            val vm: ServiceListViewModel = viewModel(factory = ServiceListViewModelFactory(AppDI.serviceRepository))
            val state by vm.uiState.collectAsState()

            LaunchedEffect(departmentId) {
                if (departmentId.isNotBlank()) vm.loadServices(departmentId)
            }

            ServiceListScreen(
                state = state,
                currentRoute = Screen.Services.route,
                onTabSelected = { tab -> navController.navigate(tab.route) { launchSingleTop = true } },
                onServiceClick = { serviceId, serviceName ->
                    val handle = navController.currentBackStackEntry?.savedStateHandle
                    handle?.set(ConfirmationNavKeys.KEY_SERVICE_ID, serviceId)
                    handle?.set(ConfirmationNavKeys.KEY_SERVICE_NAME, serviceName)
                    navController.navigate(TimeSlotDestinations.timeSlotSelectionRoute(serviceId))
                },
                onBackClick = { navController.popBackStack() },
                onRetry = { vm.loadServices(departmentId) }
            )
        }

        timeSlotGraph(navController = navController, timeSlotRepository = timeSlotRepository)

        // --- Confirm ---
        composable(Screen.Confirm.route) {
            val prev = navController.previousBackStackEntry?.savedStateHandle
            val studentId = prev?.get<String>(ConfirmationNavKeys.KEY_STUDENT_ID).orEmpty()
            val departmentName = prev?.get<String>(ConfirmationNavKeys.KEY_DEPARTMENT_NAME).orEmpty()
            val serviceId = prev?.get<String>(ConfirmationNavKeys.KEY_SERVICE_ID).orEmpty()
            val serviceName = prev?.get<String>(ConfirmationNavKeys.KEY_SERVICE_NAME).orEmpty()
            val timeSlotId = prev?.get<String>(ConfirmationNavKeys.KEY_TIMESLOT_ID).orEmpty()
            val scheduledStartMillis = prev?.get<Long>(ConfirmationNavKeys.KEY_START_MILLIS) ?: 0L
            val dateTimeText = prev?.get<String>(ConfirmationNavKeys.KEY_DATE_TEXT).orEmpty()

            AppointmentConfirmationScreen(
                studentId = studentId,
                departmentName = departmentName,
                serviceId = serviceId,
                serviceName = serviceName,
                timeSlotId = timeSlotId,
                scheduledStartMillis = scheduledStartMillis,
                dateTimeText = dateTimeText,
                factory = AppointmentConfirmationViewModelFactory(AppDI.appointmentRepository),
                onBack = { navController.popBackStack() },
                onSuccessNavigate = {
                    navController.navigate(appointmentsRoute(studentId)) {
                        popUpTo(Screen.Confirm.route) { inclusive = true }
                    }
                }
            )
        }

        // --- Appointments ---
        composable(
            route = "${Screen.Appointments.route}?$ARG_STUDENT_ID={$ARG_STUDENT_ID}",
            arguments = listOf(navArgument(ARG_STUDENT_ID) { type = NavType.StringType; defaultValue = "" })
        ) { backStackEntry ->
            val studentId = backStackEntry.arguments?.getString(ARG_STUDENT_ID).orEmpty()
            val factory = AppointmentListViewModelFactory(studentId, AppDI.appointmentRepository)

            AppointmentListScreen(
                factory = factory,
                onAppointmentClick = { id -> navController.navigate(Screen.AppointmentDetail.createRoute(id)) }
            )
        }

        composable(
            route = Screen.AppointmentDetail.route,
            arguments = listOf(navArgument("appointmentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val appointmentId = backStackEntry.arguments?.getString("appointmentId").orEmpty()

            // Buradaki repository parametresini AppDI üzerinden veriyoruz
            AppointmentDetailRoute(
                appointmentId = appointmentId,
                repository = AppDI.appointmentRepository, // Burası AppDI olmalı
                onBack = { navController.popBackStack() }
            )
        }
        // --- Staff Dashboard (GÜNCELLENMİŞ) ---
        composable("staffDashboard/{staffId}?entry={entry}") { backStackEntry ->
            val staffId = backStackEntry.arguments?.getString("staffId").orEmpty()
            val entry = backStackEntry.arguments?.getString("entry").orEmpty()

            if (staffId.isBlank() || entry != "staff") {
                navController.navigate(Screen.Welcome.route) {
                    popUpTo(Screen.Welcome.route) { inclusive = true }
                }
                return@composable
            }

            val vm: StaffDashboardViewModel = viewModel(
                factory = StaffDashboardViewModelFactory(staffId, AppDI.staffRepository)
            )

            // ViewModel'deki tüm Flow'ları dinle (collectAsState)
            val state by vm.uiState.collectAsState()
            val staffName by vm.staffName.collectAsState()
            val currentFilter by vm.currentFilter.collectAsState()
            val actionLoading by vm.actionLoading.collectAsState()
            val actionError by vm.actionError.collectAsState()

            StaffDashboardScreen(
                state = state,
                staffName = staffName, // Artık dinamik!
                selectedFilter = currentFilter, // Filtre durumu!
                onFilterChanged = { vm.onFilterChanged(it) }, // Filtreleme aksiyonu!
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
    }
}

@Composable
private fun PlaceholderScreen(name: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "TODO: $name screen")
    }
}