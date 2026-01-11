package com.example.studentcenterapp.viewmodel.appointment

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.studentcenterapp.data.timeslot.TimeSlotRepository
import com.example.studentcenterapp.model.TimeSlot
import com.example.studentcenterapp.viewmodel.appointment.TimeSlotCalendarView
import com.example.studentcenterapp.viewmodel.appointment.TimeSlotCalendarViewModel
import com.example.studentcenterapp.viewmodel.appointment.TimeSlotCalendarViewModelFactory
import com.example.studentcenterapp.viewmodel.appointment.TimeSlotSelectionViewModel
import com.example.studentcenterapp.viewmodel.appointment.TimeSlotSelectionViewModelFactory

/**
 * Time slot flow için route sabitleri.
 */
object TimeSlotDestinations {
    const val ARG_SERVICE_ID = "serviceId"

    const val ROUTE_TIME_SLOT_SELECTION = "timeSlotSelection"
    const val ROUTE_TIME_SLOT_CALENDAR = "timeSlotCalendar"

    fun timeSlotSelectionRoute(serviceId: String): String =
        "$ROUTE_TIME_SLOT_SELECTION/$serviceId"

    fun timeSlotCalendarRoute(serviceId: String): String =
        "$ROUTE_TIME_SLOT_CALENDAR/$serviceId"
}

/**
 * Time slot ile ilgili tüm ekranları tek bir graph altında toplar.
 * Bunu ana NavHost içinde çağıracağız.
 */
fun NavGraphBuilder.timeSlotGraph(
    navController: NavHostController,
    timeSlotRepository: TimeSlotRepository
) {
    // 1) TimeSlotSelection route
    composable(
        route = "${TimeSlotDestinations.ROUTE_TIME_SLOT_SELECTION}/{${TimeSlotDestinations.ARG_SERVICE_ID}}",
        arguments = listOf(
            navArgument(TimeSlotDestinations.ARG_SERVICE_ID) { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val serviceId =
            backStackEntry.arguments?.getString(TimeSlotDestinations.ARG_SERVICE_ID).orEmpty()

        val vm: TimeSlotSelectionViewModel = viewModel(
            factory = TimeSlotSelectionViewModelFactory(timeSlotRepository, serviceId)
        )

        TimeSlotSelectionScreen(
            viewModel = vm,
            onSlotConfirmed = { slot: TimeSlot ->
                // TODO: confirmation / appointment akışına bağlanacak
                // navController.navigate("confirm/${slot.id}")
            },
            onOpenCalendar = {
                navController.navigate(TimeSlotDestinations.timeSlotCalendarRoute(serviceId))
            }
        )
    }

    // 2) TimeSlotCalendar route
    composable(
        route = "${TimeSlotDestinations.ROUTE_TIME_SLOT_CALENDAR}/{${TimeSlotDestinations.ARG_SERVICE_ID}}",
        arguments = listOf(
            navArgument(TimeSlotDestinations.ARG_SERVICE_ID) { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val serviceId =
            backStackEntry.arguments?.getString(TimeSlotDestinations.ARG_SERVICE_ID).orEmpty()

        val vm: TimeSlotCalendarViewModel = viewModel(
            factory = TimeSlotCalendarViewModelFactory(timeSlotRepository, serviceId)
        )

        TimeSlotCalendarView(
            viewModel = vm,
            onBack = { navController.popBackStack() }
        )
    }
}