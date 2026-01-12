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
import com.example.studentcenterapp.ui.common.studentBottomTabs
import com.example.studentcenterapp.ui.common.staffBottomTabs

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

    // ROTA TAKİBİ
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // KULLANICI ROLÜNÜ TESPİT ET (Önemli: Personel mi Öğrenci mi?)
    // Bu mantık, personel dashboard'dan başka sekmeye geçse bile rolü hatırlamasını sağlar.
    val isUserStaff = remember(navBackStackEntry) {
        currentRoute?.contains("staffDashboard") == true ||
                navBackStackEntry?.arguments?.getString("entry") == "staff"
    }

    // Personel ID'sini sakla (Home'a dönerken lazım olacak)
    val staffIdFromArgs = navBackStackEntry?.arguments?.getString("staffId")

    // Merkezi Tab Geçiş Mantığı
    val navigateToTab: (AppTab) -> Unit = { tab ->
        val targetRoute = when (tab.route) {
            "staff_home" -> "staffDashboard/$staffIdFromArgs?entry=staff"
            else -> tab.route
        }

        if (currentRoute != targetRoute && targetRoute.isNotEmpty()) {
            navController.navigate(targetRoute) {
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
            val vm: StaffLoginViewModel = viewModel(factory = StaffLoginViewModelFactory(AppDI.staffAuthRepository))
            StaffLoginScreen(
                vm = vm,
                onSignupClick = { navController.navigate(Screen.StaffSignup.route) },
                onForgotPasswordClick = { navController.navigate("forgotPasswordEmail/staff") },
                onSuccess = { staffId ->
                    navController.navigate("staffDashboard/$staffId?entry=staff") {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
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

        // --- Chat Routes (Ortak Ekran Yönetimi) ---
        composable(route = Screen.Chat.route) {
            ChatListScreen(
                tabs = if (isUserStaff) staffBottomTabs else studentBottomTabs,
                currentRoute = currentRoute,
                isStaff = isUserStaff, // EKSİK PARAMETRE EKLENDİ
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
                tabs = if (isUserStaff) staffBottomTabs else studentBottomTabs,
                chatId = chatId,
                chatTitle = chatName,
                currentRoute = currentRoute,
                onTabSelected = navigateToTab,
                onBackClick = { navController.popBackStack() }
            )
        }

        // --- Appointment List ---
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

        // --- Staff Dashboard ---
        composable(
            route = "staffDashboard/{staffId}?entry={entry}",
            arguments = listOf(
                navArgument("staffId") { type = NavType.StringType },
                navArgument("entry") { type = NavType.StringType; defaultValue = "staff" }
            )
        ) { backStackEntry ->
            val staffId = backStackEntry.arguments?.getString("staffId").orEmpty()

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
                // Dashboard'dayken ikonun yeşil yanması için rotayı "staff_home" olarak simüle ediyoruz
                currentRoute = if (currentRoute?.contains("staffDashboard") == true) "staff_home" else currentRoute,
                onTabSelected = navigateToTab,
                onApprove = { vm.approve(it) },
                onReject = { vm.reject(it) }
            )
        }

        // Diğer yardımcı rotalar (Success, Password Reset vb.) buraya eklenebilir...
    }
}