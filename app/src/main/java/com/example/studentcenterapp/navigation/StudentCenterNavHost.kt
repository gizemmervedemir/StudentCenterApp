package com.example.studentcenterapp.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
import com.example.studentcenterapp.ui.screens.AppointmentConfirmationScreen
import com.example.studentcenterapp.ui.screens.AppointmentListScreen
import com.example.studentcenterapp.ui.service.ServiceListScreen
import com.example.studentcenterapp.ui.service.ServiceListViewModel
import com.example.studentcenterapp.ui.service.ServiceListViewModelFactory
import com.example.studentcenterapp.ui.splash.SplashScreen
import com.example.studentcenterapp.ui.splash.WelcomeScreen
import com.example.studentcenterapp.ui.staff.StaffDashboardScreen
import com.example.studentcenterapp.ui.staff.StaffDashboardViewModel
import com.example.studentcenterapp.ui.staff.StaffDashboardViewModelFactory
import com.example.studentcenterapp.ui.staffauth.StaffLoginScreen
import com.example.studentcenterapp.ui.staffauth.StaffLoginViewModel
import com.example.studentcenterapp.ui.staffauth.StaffLoginViewModelFactory
import com.example.studentcenterapp.ui.staffauth.StaffSignupScreen
import com.example.studentcenterapp.ui.staffauth.StaffSignupViewModel
import com.example.studentcenterapp.ui.staffauth.StaffSignupViewModelFactory
import com.example.studentcenterapp.ui.student.ForgotPasswordEmailScreen
import com.example.studentcenterapp.ui.student.SignupSuccessScreen
import com.example.studentcenterapp.ui.student.StudentLoginScreen
import com.example.studentcenterapp.ui.student.StudentSignupScreen
import com.example.studentcenterapp.ui.theme.StudentCenterTheme
import com.example.studentcenterapp.viewmodel.appointment.AppointmentListViewModelFactory
import com.example.studentcenterapp.viewmodel.appointment.TimeSlotDestinations
import com.example.studentcenterapp.viewmodel.appointment.timeSlotGraph
import com.example.studentcenterapp.viewmodel.department.DepartmentListScreen
import com.example.studentcenterapp.viewmodel.department.DepartmentListViewModel
import com.example.studentcenterapp.viewmodel.department.DepartmentListViewModelFactory
import com.example.studentcenterapp.viewmodel.splash.SplashViewModel
import com.example.studentcenterapp.viewmodel.student.ForgotPasswordViewModel
import com.example.studentcenterapp.viewmodel.student.StudentLoginViewModel
import com.example.studentcenterapp.viewmodel.student.StudentSignupViewModel

// ✅ Appointment Detail imports (SENİN DOSYA PATH'İNE GÖRE DÜZELT)
// Eğer AppointmentDetailScreen farklı paketteyse bunu düzelt.
import com.example.studentcenterapp.ui.appointments.components.AppointmentDetailScreen
import com.example.studentcenterapp.viewmodel.appointment.AppointmentDetailViewModelFactory

import com.example.studentcenterapp.ui.appointment.AppointmentDetailRoute
import com.example.studentcenterapp.ui.student.PasswordResetSuccessScreen

// -------------------------
// Query param key for appointments list
// -------------------------
private const val ARG_STUDENT_ID = "studentId"

// Route builder for appointments list
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
    // ✅ TimeSlot için tek instance dataSource + repository (NavHost ömrü boyunca sabit)
    val timeSlotDataSource = remember { InMemoryDataSource() }
    val timeSlotRepository = remember { TimeSlotRepositoryImpl(timeSlotDataSource) }

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {

        // -------------------------
        // Splash -> Welcome
        // -------------------------
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

        // -------------------------
        // Staff Auth
        // -------------------------
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
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.StaffSignup.route) {
            val vm: StaffSignupViewModel = viewModel(
                factory = StaffSignupViewModelFactory(AppDI.staffAuthRepository)
            )

            StaffSignupScreen(
                vm = vm,
                onSuccess = { staffId ->
                    navController.navigate("staffDashboard/$staffId?entry=staff") {
                        popUpTo(Screen.StaffLogin.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        // -------------------------
        // Student Auth
        // -------------------------
        composable(Screen.StudentLogin.route) {
            val loginViewModel: StudentLoginViewModel = viewModel(
                factory = StudentLoginViewModel.Factory
            )

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
            val signupViewModel: StudentSignupViewModel = viewModel(
                factory = StudentSignupViewModel.Factory
            )

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
            SignupSuccessScreen(
                onLoginClick = {
                    navController.navigate(Screen.StudentLogin.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

// 1. Yeni rotayı ekle
        composable("passwordResetSuccess") {
            PasswordResetSuccessScreen(
                onLoginClick = {
                    navController.navigate(Screen.StudentLogin.route) {
                        popUpTo("passwordResetSuccess") { inclusive = true }
                    }
                }
            )
        }

// 2. ForgotPasswordEmail bloğunu güncelle
        composable(Screen.ForgotPasswordEmail.route) {
            val forgotPasswordViewModel: ForgotPasswordViewModel = viewModel()

            ForgotPasswordEmailScreen(
                viewModel = forgotPasswordViewModel,
                onCodeSent = {
                    // Başarılı olunca yeni ekrana git
                    navController.navigate("passwordResetSuccess")
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        // -------------------------
        // Student flow: Departments -> Services -> Slots -> Confirm -> Appointments
        // -------------------------
        composable(Screen.Departments.route) {
            val vm: DepartmentListViewModel = viewModel(
                factory = DepartmentListViewModelFactory(AppDI.departmentRepository)
            )

            val state by vm.uiState.collectAsState()
            val userName by vm.userName.collectAsState() // ViewModel'deki ismi dinle

            DepartmentListScreen(
                state = state,
                userName = userName, // İsmi ekrana gönderdik!
                currentRoute = Screen.Departments.route,
                onTabSelected = { tab ->
                    navController.navigate(tab.route) { launchSingleTop = true }
                },
                onDepartmentClick = { departmentId, departmentName ->
                    val handle = navController.currentBackStackEntry?.savedStateHandle
                    handle?.set(ConfirmationNavKeys.KEY_DEPARTMENT_NAME, departmentName)

                    val sid = StudentSession.currentStudentId
                    handle?.set(ConfirmationNavKeys.KEY_STUDENT_ID, sid)

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
                val prevHandle = navController.previousBackStackEntry?.savedStateHandle
                val currentHandle = navController.currentBackStackEntry?.savedStateHandle

                val deptName =
                    prevHandle?.get<String>(ConfirmationNavKeys.KEY_DEPARTMENT_NAME).orEmpty()

                val sid =
                    prevHandle?.get<String>(ConfirmationNavKeys.KEY_STUDENT_ID)
                        .orEmpty()
                        .ifBlank { StudentSession.currentStudentId }

                currentHandle?.set(ConfirmationNavKeys.KEY_DEPARTMENT_NAME, deptName)
                currentHandle?.set(ConfirmationNavKeys.KEY_STUDENT_ID, sid)

                if (departmentId.isNotBlank()) vm.loadServices(departmentId)
            }

            ServiceListScreen(
                state = state,
                currentRoute = Screen.Services.route,
                onTabSelected = { tab ->
                    navController.navigate(tab.route) { launchSingleTop = true }
                },
                onServiceClick = { serviceId, serviceName ->
                    val handle = navController.currentBackStackEntry?.savedStateHandle
                    handle?.set(ConfirmationNavKeys.KEY_SERVICE_ID, serviceId)
                    handle?.set(ConfirmationNavKeys.KEY_SERVICE_NAME, serviceName)

                    navController.navigate(TimeSlotDestinations.timeSlotSelectionRoute(serviceId))
                }
            )
        }

        // ✅ TimeSlot flow graph
        timeSlotGraph(
            navController = navController,
            timeSlotRepository = timeSlotRepository
        )

        // -------------------------
        // Confirm
        // -------------------------
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
                    if (studentId.isNotBlank()) {
                        navController.navigate(appointmentsRoute(studentId)) {
                            popUpTo(Screen.Confirm.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    } else {
                        navController.popBackStack()
                    }
                }
            )
        }

        // -------------------------
        // Appointments List
        // -------------------------
        composable(
            route = "${Screen.Appointments.route}?$ARG_STUDENT_ID={$ARG_STUDENT_ID}",
            arguments = listOf(
                navArgument(ARG_STUDENT_ID) {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val studentId = backStackEntry.arguments?.getString(ARG_STUDENT_ID).orEmpty()

            val factory = AppointmentListViewModelFactory(
                studentId = studentId,
                appointmentRepository = AppDI.appointmentRepository
            )

            AppointmentListScreen(
                factory = factory,
                onAppointmentClick = { appointmentId ->
                    navController.navigate(Screen.AppointmentDetail.createRoute(appointmentId))
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
        // -------------------------
        // ✅ Appointment Detail
        // -------------------------
        composable(
            route = Screen.AppointmentDetail.route,
            arguments = listOf(navArgument("appointmentId") { type = NavType.StringType })
        ) { backStackEntry ->

            val appointmentId = backStackEntry.arguments?.getString("appointmentId").orEmpty()

            val vm: com.example.studentcenterapp.viewmodel.appointment.AppointmentDetailViewModel =
                viewModel(
                    factory = com.example.studentcenterapp.viewmodel.appointment.AppointmentDetailViewModelFactory(
                        appointmentId = appointmentId,
                        repository = AppDI.appointmentRepository
                    )
                )

            val state by vm.uiState.collectAsState()

            com.example.studentcenterapp.ui.appointments.components.AppointmentDetailScreen(
                state = state,
                onBack = { navController.popBackStack() },
                onCancelClick = { vm.cancelAppointment() }
            )
        }

        // -------------------------
        // Staff Dashboard (guarded)
        // -------------------------
        composable("staffDashboard/{staffId}?entry={entry}") { backStackEntry ->
            val staffId = backStackEntry.arguments?.getString("staffId").orEmpty()
            val entry = backStackEntry.arguments?.getString("entry").orEmpty()

            if (staffId.isBlank() || entry != "staff") {
                navController.navigate(Screen.Welcome.route) {
                    popUpTo(Screen.Welcome.route) { inclusive = false }
                    launchSingleTop = true
                }
                return@composable
            }

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

        // Placeholders
        composable(Screen.Services.route) { PlaceholderScreen("Services") }
        composable(Screen.Slots.route) { PlaceholderScreen("Slots") }
        composable(Screen.StaffDashboard.route) { PlaceholderScreen("Staff Dashboard") }
    }
}

@Composable
private fun PlaceholderScreen(name: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "TODO: $name screen")
    }
}

@Preview(showBackground = true)
@Composable
fun StudentCenterAppPreview() {
    StudentCenterApp()
}