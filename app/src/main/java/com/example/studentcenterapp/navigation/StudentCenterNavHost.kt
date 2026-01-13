package com.example.studentcenterapp.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import com.example.studentcenterapp.ui.profile.PersonalInfoScreen
import com.example.studentcenterapp.ui.profile.ProfileScreen
import com.example.studentcenterapp.ui.profile.UpdateSuccessScreen
import com.example.studentcenterapp.viewmodel.profile.ProfileUiState
import com.example.studentcenterapp.viewmodel.profile.ProfileViewModel
import com.example.studentcenterapp.viewmodel.profile.ProfileViewModelFactory
import com.example.studentcenterapp.viewmodel.staff.StaffCalendarViewModel
import com.example.studentcenterapp.viewmodel.staff.StaffCalendarViewModelFactory

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
    val timeSlotRepository = AppDI.timeSlotRepository
    // ROTA TAKİBİ
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Personel ID'sini sakla (Home'a dönerken lazım olacak)
    var savedStaffId by remember { mutableStateOf<String?>(null) }

    // KULLANICI ROLÜNÜ TESPİT ET (Önemli: Personel mi Öğrenci mi?)
    // Bu mantık, personel dashboard'dan başka sekmeye geçse bile rolü hatırlamasını sağlar.
    val isUserStaff = remember(navBackStackEntry, savedStaffId) {
        currentRoute?.contains("staffDashboard") == true ||
                navBackStackEntry?.arguments?.getString("entry") == "staff" ||
                savedStaffId != null // Eğer elimizde bir staffId varsa bu kişi personledir
    }


// staffId'yi her yakaladığımızda güncelleyelim
    val staffIdFromArgs = navBackStackEntry?.arguments?.getString("staffId")
    if (staffIdFromArgs != null) {
        savedStaffId = staffIdFromArgs
    }
    val navigateToTab: (AppTab) -> Unit = { tab ->
        val targetRoute = when {
            // DURUM 1: Kullanıcı Personel ve "Home" ikonuna (ister staff_home ister departments) basıyor
            isUserStaff && (tab.route == "staff_home" || tab.route == Screen.Departments.route) -> {
                "staffDashboard/$savedStaffId?entry=staff"
            }

            // DURUM 2: Kullanıcı Öğrenci ve "Home" ikonuna basıyor (zaten tab.route departments olacaktır)
            !isUserStaff && tab.route == Screen.Departments.route -> {
                Screen.Departments.route
            }

            isUserStaff && tab.route == Screen.StudentCalendar.route -> {
                "staffCalendar/$savedStaffId"
            }

            // DİĞER DURUMLAR: Chat, Profile, Calendar vb.
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

        composable(
            route = "signupSuccess/{userType}", // "signupSuccess/staff" veya "signupSuccess/student" buraya düşer
            arguments = listOf(navArgument("userType") { type = NavType.StringType })
        ) { backStackEntry ->
            val userType = backStackEntry.arguments?.getString("userType") ?: "student"

            SignupSuccessScreen(onLoginClick = {
                if (userType == "staff") {
                    navController.navigate(Screen.StaffLogin.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                } else {
                    navController.navigate(Screen.StudentLogin.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
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
            val currentStudentId = StudentSession.currentStudentId ?: ""
            val vm: AppointmentListViewModel = viewModel(
                factory = AppointmentListViewModelFactory(
                    studentId = currentStudentId,
                    appointmentRepository = AppDI.appointmentRepository
                )
            )

            StudentCalendarScreen(
                navController = navController,
                viewModel = vm,
                currentRoute = currentRoute,      // EKLENDİ
                onTabSelected = navigateToTab     // EKLENDİ (Merkezi Fonksiyon)
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

//        composable(Screen.StudentChat.route) {
//            PlaceholderScreen(
//                name = "Öğrenci Destek",
////                currentRoute = Screen.StudentChat.route,
////                navController = navController
//            )
//        }

//        composable(Screen.StudentProfile.route) {
////            PlaceholderScreen(
////                name = "Öğrenci Profili",
//////                currentRoute = Screen.StudentProfile.route,
//////                navController = navController
////            )
//        }

        // --- PROFILE FLOW ---

// 1. Ana Profil Ekranı
        composable(Screen.StudentProfile.route) {
            val profileVm: ProfileViewModel = viewModel(
                factory = ProfileViewModelFactory(AppDI.studentRepository, AppDI.staffRepository)
            )

            // userId'yi null-safe hale getirdik (?: "")
            val userId = (if (isUserStaff) staffIdFromArgs else StudentSession.currentStudentId) ?: ""

            // Artık userId String? değil, kesinlikle String olduğu için hata vermeyecektir
            LaunchedEffect(userId) {
                if (userId.isNotEmpty()) {
                    profileVm.loadProfile(userId, isUserStaff)
                }
            }

            when (val state = profileVm.uiState) {
                is ProfileUiState.Success -> {
                    ProfileScreen(
                        userName = state.name,
                        userEmail = state.email,
                        isUserStaff = isUserStaff, // Eklendi
                        currentRoute = currentRoute,
                        onTabSelected = navigateToTab,
                        onNavigateToPersonalInfos = { navController.navigate("personalInfo") },
                        onLogout = {
                            profileVm.logout {
                                navController.navigate(Screen.Welcome.route) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        }
                    )
                }
                is ProfileUiState.Loading -> { Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                    Text("Veriler Yükleniyor...")
                } }
                is ProfileUiState.Error -> { Text("Hata: ${state.message}") }
            }
        }
// 2. Kişisel Bilgiler Formu
        composable("personalInfo") {
            PersonalInfoScreen(
                onBackClick = { navController.popBackStack() },
                onUpdateClick = {
                    // Güncelleme mantığı ViewModel'e eklenecek
                    navController.navigate("updateSuccess")
                }
            )
        }

// 3. Güncelleme Başarılı Ekranı
        composable("updateSuccess") {
            UpdateSuccessScreen(
                onProfileClick = {
                    navController.navigate(Screen.StudentProfile.route) {
                        popUpTo("personalInfo") { inclusive = true }
                    }
                }
            )
        }
        // --- Student Flow ---
        // --- Student Flow (Departments, Services, Appointments) ---
        composable(Screen.Departments.route) {
            val vm: DepartmentListViewModel = viewModel(factory = DepartmentListViewModelFactory(AppDI.departmentRepository))
            val state by vm.uiState.collectAsState()
            val userName by vm.userName.collectAsState()

            DepartmentListScreen(
                state = state,
                userName = userName,
                currentRoute = Screen.Departments.route,
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
                    // ✅ HATA GİDERİLDİ: serviceName ve type parametreleri eklendi
                    navController.navigate(
                        TimeSlotDestinations.timeSlotSelectionRoute(serviceId, serviceName, type)
                    )
                },
                onBackClick = { navController.popBackStack() },
                onRetry = { vm.loadServices(departmentId) }
            )
        }

        composable("staffCalendar/{staffId}") { backStackEntry ->
            val staffId = backStackEntry.arguments?.getString("staffId").orEmpty()

            val calendarVm: StaffCalendarViewModel = viewModel(
                factory = StaffCalendarViewModelFactory(
                    staffId = staffId,
                    repository = AppDI.appointmentRepository
                )
            )

            StaffCalendarScreen(
                viewModel = calendarVm,
                currentRoute = navController.currentBackStackEntry?.destination?.route,
                onTabSelected = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                onApprove = { app -> calendarVm.approveAppointment(app) },
                onReject = { app -> calendarVm.rejectAppointment(app) }
            )
        }

        // --- YENİ: Time Slot Seçimi ve Randevu Onay Akışı ---
        composable(
            route = "${TimeSlotDestinations.ROUTE_TIME_SLOT_SELECTION}/{serviceId}/{serviceName}/{type}",
            arguments = listOf(
                navArgument("serviceId") { type = NavType.StringType },
                navArgument("serviceName") { type = NavType.StringType },
                navArgument("type") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val serviceId = backStackEntry.arguments?.getString("serviceId").orEmpty()
            val serviceName = backStackEntry.arguments?.getString("serviceName").orEmpty()
            val type = backStackEntry.arguments?.getString("type").orEmpty()

            // ✅ HATA GİDERİLDİ: ViewModel burada oluşturuluyor
            val vm: TimeSlotCalendarViewModel = viewModel(
                factory = TimeSlotCalendarViewModelFactory(timeSlotRepository, serviceId)
            )

            TimeSlotSelectionScreen(
                serviceName = serviceName, // Screen sadece serviceName bekliyor (güncellediğimiz koda göre)
                viewModel = vm,            // ViewModel parametresi eklendi
                currentRoute = currentRoute,
                onTabSelected = navigateToTab,
                onBackClick = { navController.popBackStack() },
                onAppointmentCreated = {
                    navController.navigate("appointmentSuccess") {
                        popUpTo("${TimeSlotDestinations.ROUTE_TIME_SLOT_SELECTION}/$serviceId/$serviceName/$type") {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable("appointmentSuccess") {
            // Görselin en sağındaki "Randevunuz oluşturulmuştur" ekranı
            AppointmentSuccessScreen(
                onNavigateToAppointments = {
                    val studentId = StudentSession.currentStudentId ?: ""
                    navController.navigate("${Screen.Appointments.route}?studentId=$studentId") {
                        popUpTo("appointmentSuccess") { inclusive = true }
                    }
                }
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
        // --- Staff Dashboard Bölümü ---
        composable(
            route = "staffDashboard/{staffId}?entry={entry}",
            arguments = listOf(
                navArgument("staffId") { type = NavType.StringType },
                navArgument("entry") { type = NavType.StringType; defaultValue = "staff" }
            )
        ) {backStackEntry ->
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
                currentRoute = "staff_home",
                onTabSelected = navigateToTab,
                onApprove = { vm.approve(it) },
                onReject = { vm.reject(it) }
            )
        }

        // Diğer yardımcı rotalar (Success, Password Reset vb.) buraya eklenebilir...
    }
}