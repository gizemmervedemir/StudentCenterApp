package com.example.studentcenterapp.viewmodel.appointment

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.studentcenterapp.data.student.StudentSession
import com.example.studentcenterapp.data.timeslot.TimeSlotRepository
import com.example.studentcenterapp.model.TimeSlot
import com.example.studentcenterapp.navigation.ConfirmationNavKeys
import com.example.studentcenterapp.navigation.Screen
import java.text.SimpleDateFormat
import java.util.Locale

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

                // ✅ Services screen entry'sinden (previous) gelen değerler:
                val prevHandle = navController.previousBackStackEntry?.savedStateHandle

                val studentId = prevHandle
                    ?.get<String>(ConfirmationNavKeys.KEY_STUDENT_ID)
                    .orEmpty()
                    .ifBlank { StudentSession.currentStudentId.ifBlank { "student" } }

                val departmentName =
                    prevHandle?.get<String>(ConfirmationNavKeys.KEY_DEPARTMENT_NAME).orEmpty()

                val serviceName =
                    prevHandle?.get<String>(ConfirmationNavKeys.KEY_SERVICE_NAME).orEmpty()

                // ✅ slot -> confirm data
                val timeSlotId = slot.id
                val dateTimeText = "${slot.date} ${slot.startTime} - ${slot.endTime}"

                // ✅ TimeSlot modelinde millis yok → date + startTime parse etmeye çalış
                val scheduledStartMillis = slot.toStartMillisOrNow()

                // ✅ Confirm screen previousBackStackEntry.savedStateHandle okuyacak.
                // O yüzden veriyi current entry (TimeSlotSelection) handle’a yazıyoruz:
                val handle = navController.currentBackStackEntry?.savedStateHandle
                handle?.set(ConfirmationNavKeys.KEY_STUDENT_ID, studentId)
                handle?.set(ConfirmationNavKeys.KEY_DEPARTMENT_NAME, departmentName)
                handle?.set(ConfirmationNavKeys.KEY_SERVICE_ID, serviceId) // composable içindeki serviceId
                handle?.set(ConfirmationNavKeys.KEY_SERVICE_NAME, serviceName)
                handle?.set(ConfirmationNavKeys.KEY_TIMESLOT_ID, timeSlotId)
                handle?.set(ConfirmationNavKeys.KEY_START_MILLIS, scheduledStartMillis)
                handle?.set(ConfirmationNavKeys.KEY_DATE_TEXT, dateTimeText)

                navController.navigate(Screen.Confirm.route)
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

/**
 * ✅ TimeSlot date + startTime -> epoch millis
 * java.time kullanmıyoruz (desugaring gerektirmesin diye).
 *
 * date için birkaç olası format dener:
 * - yyyy-MM-dd
 * - dd/MM/yyyy
 * - dd.MM.yyyy
 *
 * startTime formatı: HH:mm (örn: 10:30)
 *
 * Parse olmazsa NOW döner.
 */
private fun TimeSlot.toStartMillisOrNow(): Long {
    val dateStr = date.trim()
    val timeStr = startTime.trim()

    val patterns = listOf(
        "yyyy-MM-dd HH:mm",
        "dd/MM/yyyy HH:mm",
        "dd.MM.yyyy HH:mm"
    )

    val input = "$dateStr $timeStr"

    for (p in patterns) {
        try {
            val sdf = SimpleDateFormat(p, Locale.getDefault())
            sdf.isLenient = false
            val parsed = sdf.parse(input)
            if (parsed != null) return parsed.time
        } catch (_: Exception) {
            // try next pattern
        }
    }

    return System.currentTimeMillis()
}
