package com.example.studentcenterapp.viewmodel.appointment

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.studentcenterapp.data.student.StudentSession
import com.example.studentcenterapp.data.timeslot.TimeSlotRepository
import com.example.studentcenterapp.ui.screens.TimeSlotSelectionScreen

object TimeSlotDestinations {
    const val ROUTE_TIME_SLOT_SELECTION = "timeSlotSelection"
    const val ROUTE_SUCCESS = "appointmentSuccess"

    fun timeSlotSelectionRoute(serviceId: String, serviceName: String, type: String): String =
        "$ROUTE_TIME_SLOT_SELECTION/$serviceId/$serviceName/$type"
}

fun NavGraphBuilder.timeSlotGraph(
    navController: NavHostController,
    timeSlotRepository: TimeSlotRepository,
    currentRoute: String?,
    onTabSelected: (com.example.studentcenterapp.ui.common.AppTab) -> Unit
) {
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

        // ✅ CalendarViewModel kullanıyoruz
        val vm: TimeSlotCalendarViewModel = viewModel(
            factory = TimeSlotCalendarViewModelFactory(timeSlotRepository, serviceId)
        )

        TimeSlotSelectionScreen(
            serviceName = serviceName,
            viewModel = vm,
            currentRoute = currentRoute,
            onTabSelected = onTabSelected,
            onBackClick = { navController.popBackStack() },
            onAppointmentCreated = {
                navController.navigate(TimeSlotDestinations.ROUTE_SUCCESS) {
                    popUpTo("${TimeSlotDestinations.ROUTE_TIME_SLOT_SELECTION}/$serviceId/$serviceName/$type") {
                        inclusive = true
                    }
                }
            }
        )
    }

    composable(TimeSlotDestinations.ROUTE_SUCCESS) {
        com.example.studentcenterapp.ui.screens.AppointmentSuccessScreen(
            onNavigateToAppointments = {
                navController.navigate("appointments?studentId=${StudentSession.currentStudentId}") {
                    popUpTo(TimeSlotDestinations.ROUTE_SUCCESS) { inclusive = true }
                }
            }
        )
    }
}