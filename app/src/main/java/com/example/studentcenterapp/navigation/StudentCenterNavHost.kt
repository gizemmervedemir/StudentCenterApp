package com.example.studentcenterapp.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
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
import com.example.studentcenterapp.ui.chat.ChatListScreen
import com.example.studentcenterapp.ui.chat.ChatDetailScreen
import com.example.studentcenterapp.ui.common.AppTab

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
    val timeSlotDataSource = remember { InMemoryDataSource() }
    val timeSlotRepository = remember { TimeSlotRepositoryImpl(timeSlotDataSource) }

    // ROTA TAKİBİ: Bottom Bar ikonlarının seçili gelmesini sağlar
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Merkezi Tab Geçiş Mantığı
    val navigateToTab: (AppTab) -> Unit = { tab ->
        if (currentRoute != tab.route) { // Zaten o sayfadaysak tekrar yükleme yapma
            navController.navigate(tab.route) {
                // Stack'i temizleyerek döngüsel crashleri önler
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

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
                onForgotPasswordClick = { navController.navigate("forgotPasswordEmail/staff") },
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
                    navController.navigate("signupSuccess/staff") {
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
                onForgotPasswordClick = { navController.navigate("forgotPasswordEmail/student") },
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
                    navController.navigate("signupSuccess/student") {
                        popUpTo(Screen.StudentSignup.route) { inclusive = true }
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        // --- Common Success & Password Flow ---
        composable("signupSuccess/{userType}") { backStackEntry ->
            val userType = backStackEntry.arguments?.getString("userType") ?: "student"
            SignupSuccessScreen(onLoginClick = {
                val target = if (userType == "staff") Screen.StaffLogin.route else Screen.StudentLogin.route
                navController.navigate(target) {
                    popUpTo(Screen.Welcome.route) { inclusive = true }
                }
            })
        }

        composable("forgotPasswordEmail/{userType}") { backStackEntry ->
            val userType = backStackEntry.arguments?.getString("userType") ?: "student"
            val vm: ForgotPasswordViewModel = viewModel()
            ForgotPasswordEmailScreen(
                viewModel = vm,
                onCodeSent = { navController.navigate("passwordResetSuccess/$userType") },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("passwordResetSuccess/{userType}") { backStackEntry ->
            val userType = backStackEntry.arguments?.getString("userType") ?: "student"
            PasswordResetSuccessScreen(onLoginClick = {
                val target = if (userType == "staff") Screen.StaffLogin.route else Screen.StudentLogin.route
                navController.navigate(target) {
                    popUpTo(Screen.Welcome.route) { inclusive = true }
                }
            })
        }

        // --- Student Flow (Departments, Services, Appointments) ---
        composable(Screen.Departments.route) {
            val vm: DepartmentListViewModel = viewModel(factory = DepartmentListViewModelFactory(AppDI.departmentRepository))
            val state by vm.uiState.collectAsState()
            val userName by vm.userName.collectAsState()

            DepartmentListScreen(
                state = state,
                userName = userName,
                currentRoute = currentRoute,
                onTabSelected = navigateToTab,
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
                currentRoute = currentRoute,
                onTabSelected = navigateToTab,
                onServiceClick = { serviceId, serviceName, type ->
                    val handle = navController.currentBackStackEntry?.savedStateHandle
                    handle?.set(ConfirmationNavKeys.KEY_SERVICE_ID, serviceId)
                    handle?.set(ConfirmationNavKeys.KEY_SERVICE_NAME, serviceName)
                    handle?.set("appointment_type", type)

                    navController.navigate(TimeSlotDestinations.timeSlotSelectionRoute(serviceId))
                },
                onBackClick = { navController.popBackStack() },
                onRetry = { vm.loadServices(departmentId) }
            )
        }

        timeSlotGraph(navController = navController, timeSlotRepository = timeSlotRepository)

        composable(Screen.Confirm.route) {
            val prev = navController.previousBackStackEntry?.savedStateHandle
            val studentId = prev?.get<String>(ConfirmationNavKeys.KEY_STUDENT_ID).orEmpty()
            val departmentName = prev?.get<String>(ConfirmationNavKeys.KEY_DEPARTMENT_NAME).orEmpty()
            val serviceId = prev?.get<String>(ConfirmationNavKeys.KEY_SERVICE_ID).orEmpty()
            val serviceName = prev?.get<String>(ConfirmationNavKeys.KEY_SERVICE_NAME).orEmpty()
            val timeSlotId = prev?.get<String>(ConfirmationNavKeys.KEY_TIMESLOT_ID).orEmpty()
            val scheduledStartMillis = prev?.get<Long>(ConfirmationNavKeys.KEY_START_MILLIS) ?: 0L
            val dateTimeText = prev?.get<String>(ConfirmationNavKeys.KEY_DATE_TEXT).orEmpty()
            val appointmentType = prev?.get<String>("appointment_type") ?: "office"

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

        // --- Chat Routes ---
        composable(route = Screen.Chat.route) {
            // Alt barın bu rotayı tanıması için Screen.Chat.route'un
            // AppTab.Chat içindeki rota ile aynı string olduğundan emin olun.
            ChatListScreen(
                currentRoute = currentRoute,
                onTabSelected = navigateToTab,
                onChatClick = { id, name -> navController.navigate("chatDetail/$id/$name") },
                onNewChatClick = { /* New chat logic */ }
            )
        }
        composable(
            route = "chatDetail/{chatId}/{chatName}",
            arguments = listOf(
                navArgument("chatId") { type = NavType.StringType },
                navArgument("chatName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId").orEmpty()
            val chatName = backStackEntry.arguments?.getString("chatName").orEmpty()

            ChatDetailScreen(
                chatId = chatId,
                chatTitle = chatName,
                currentRoute = currentRoute,
                onTabSelected = navigateToTab,
                onBackClick = { navController.popBackStack() }
            )
        }

        // --- Appointment List & Detail ---
        composable(
            route = "${Screen.Appointments.route}?$ARG_STUDENT_ID={$ARG_STUDENT_ID}",
            arguments = listOf(navArgument(ARG_STUDENT_ID) { type = NavType.StringType; defaultValue = "" })
        ) { backStackEntry ->
            val studentId = backStackEntry.arguments?.getString(ARG_STUDENT_ID).orEmpty()
            val factory = AppointmentListViewModelFactory(studentId, AppDI.appointmentRepository)

            AppointmentListScreen(
                factory = factory,
                currentRoute = currentRoute,
                onTabSelected = navigateToTab,
                onAppointmentClick = { id ->
                    navController.navigate(Screen.AppointmentDetail.createRoute(id))
                }
            )
        }

        composable(
            route = Screen.AppointmentDetail.route,
            arguments = listOf(navArgument("appointmentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val appointmentId = backStackEntry.arguments?.getString("appointmentId").orEmpty()
            AppointmentDetailRoute(
                appointmentId = appointmentId,
                repository = AppDI.appointmentRepository,
                onBack = { navController.popBackStack() }
            )
        }

        // --- Staff Dashboard ---
        composable(
            route = "staffDashboard/{staffId}?entry={entry}",
            arguments = listOf(
                navArgument("staffId") { type = NavType.StringType },
                navArgument("entry") { type = NavType.StringType; defaultValue = "staff" }
            )
        ) { backStackEntry ->
            val staffId = backStackEntry.arguments?.getString("staffId").orEmpty()
            val entry = backStackEntry.arguments?.getString("entry") ?: "staff"

            if (staffId.isBlank() || entry != "staff") {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
                return@composable
            }

            val vm: StaffDashboardViewModel = viewModel(
                factory = StaffDashboardViewModelFactory(staffId, AppDI.staffRepository)
            )

            val state by vm.uiState.collectAsState()
            val staffName by vm.staffName.collectAsState()
            val currentFilter by vm.currentFilter.collectAsState()
            val actionLoading by vm.actionLoading.collectAsState()
            val actionError by vm.actionError.collectAsState()

            StaffDashboardScreen(
                state = state,
                staffName = staffName,
                selectedFilter = currentFilter,
                onFilterChanged = { vm.onFilterChanged(it) },
                actionLoading = actionLoading,
                actionError = actionError,
                currentRoute = currentRoute,
                onTabSelected = navigateToTab,
                onApprove = { vm.approve(it) },
                onReject = { vm.reject(it) }
            )
        }
    }
}