package com.example.studentcenterapp.ui.timeslot

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.studentcenterapp.data.timeslot.TimeSlotRepository
import com.example.studentcenterapp.model.TimeSlot

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
    // 1) ServiceList -> TimeSlotSelection
    composable(
        route = "${TimeSlotDestinations.ROUTE_TIME_SLOT_SELECTION}/{${TimeSlotDestinations.ARG_SERVICE_ID}}",
        arguments = listOf(
            navArgument(TimeSlotDestinations.ARG_SERVICE_ID) {
                type = NavType.StringType
            }
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
                // TODO: Seçilen slot ile confirmation / appointment akışına git
                // Örneğin:
                // navController.navigate(ConfirmationDestinations.route(slot.id))
            }
        )
    }

    // 2) Opsiyonel: Calendar route
    composable(
        route = "${TimeSlotDestinations.ROUTE_TIME_SLOT_CALENDAR}/{${TimeSlotDestinations.ARG_SERVICE_ID}}",
        arguments = listOf(
            navArgument(TimeSlotDestinations.ARG_SERVICE_ID) {
                type = NavType.StringType
            }
        )
    ) { backStackEntry ->
        val serviceId =
            backStackEntry.arguments?.getString(TimeSlotDestinations.ARG_SERVICE_ID).orEmpty()

        val vm: TimeSlotCalendarViewModel = viewModel(
            factory = TimeSlotCalendarViewModelFactory(timeSlotRepository, serviceId)
        )

        TimeSlotCalendarView(
            viewModel = vm
        )
    }
}