package com.example.studentcenterapp

import com.example.studentcenterapp.data.appointment.AppointmentRecord
import com.example.studentcenterapp.data.appointment.AppointmentRepository
import com.example.studentcenterapp.ui.confirmation.AppointmentConfirmationViewModel
import com.example.studentcenterapp.viewmodel.appointment.AppointmentListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
class AppointmentViewModelsTest {

    private val testDispatcher: TestDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // -------------------------
    // Fake Repository
    // -------------------------
    private class FakeAppointmentRepository(
        initial: List<AppointmentRecord> = emptyList(),
        private val createResult: Result<Unit> = Result.success(Unit),
        private val throwOnGet: Throwable? = null
    ) : AppointmentRepository {

        private val _items = MutableStateFlow(initial)

        override fun getAppointmentsForStudent(studentId: String): Flow<List<AppointmentRecord>> {
            if (throwOnGet != null) {
                return flow { throw throwOnGet }
            }
            // Senin repo impl’in zaten filtreliyorsa fark etmez; burada sade tutuyoruz
            return _items
        }

        override suspend fun createAppointment(
            studentId: String,
            serviceId: String,
            timeSlotId: String,
            scheduledStartMillis: Long
        ): Result<Unit> {
            createResult.onSuccess {
                val newItem = AppointmentRecord(
                    id = UUID.randomUUID().toString(),
                    studentId = studentId,
                    serviceId = serviceId,
                    timeSlotId = timeSlotId,
                    scheduledStartMillis = scheduledStartMillis,
                    status = "PENDING"
                )
                _items.update { it + newItem }
            }
            return createResult
        }

        override suspend fun cancelAppointment(appointmentId: String): Result<Unit> {
            _items.update { list -> list.filterNot { it.id == appointmentId } }
            return Result.success(Unit)
        }
    }

    // -------------------------
    // AppointmentListViewModel tests
    // -------------------------
    @Test
    fun appointmentList_emptyList_setsEmptyState() = runTest {
        val repo = FakeAppointmentRepository(initial = emptyList())

        val vm = AppointmentListViewModel(
            studentId = "student-1",
            appointmentRepository = repo
        )

        advanceUntilIdle()

        val state = vm.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertTrue(state.isEmpty)
        assertTrue(state.visibleAppointments.isEmpty())
    }

    @Test
    fun appointmentList_separatesUpcomingAndPast_byStatus() = runTest {
        val items = listOf(
            appt(status = "PENDING"),
            appt(status = "APPROVED"),
            appt(status = "CANCELLED")
        )
        val repo = FakeAppointmentRepository(initial = items)

        val vm = AppointmentListViewModel(
            studentId = "student-1",
            appointmentRepository = repo
        )

        advanceUntilIdle()

        val state = vm.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)

        // Senin VM mantığına göre:
        // upcoming: pending + approved
        // past: cancelled
        assertEquals(2, state.upcomingAppointments.size)
        assertEquals(1, state.pastAppointments.size)
        assertTrue(state.pastAppointments.all { it.status.equals("cancelled", ignoreCase = true) })
    }

    @Test
    fun appointmentList_repositoryError_setsErrorMessage() = runTest {
        val repo = FakeAppointmentRepository(
            initial = emptyList(),
            throwOnGet = RuntimeException("boom")
        )

        val vm = AppointmentListViewModel(
            studentId = "student-1",
            appointmentRepository = repo
        )

        advanceUntilIdle()

        val state = vm.uiState.value
        assertFalse(state.isLoading)
        assertNotNull(state.errorMessage)
    }

    // -------------------------
    // AppointmentConfirmationViewModel tests
    // -------------------------
    @Test
    fun confirmation_success_setsIsSuccess() = runTest {
        val repo = FakeAppointmentRepository(createResult = Result.success(Unit))
        val vm = AppointmentConfirmationViewModel(appointmentRepository = repo)

        // Eğer senin VM imzan farklıysa sadece burayı eşleştir:
        vm.setSelection(
            studentId = "student-1",
            departmentName = "Computer Eng",
            serviceId = "service-1",
            serviceName = "Counseling",
            timeSlotId = "slot-1",
            scheduledStartMillis = 123L,
            dateTimeText = "2026-01-11 10:00"
        )

        vm.confirm()
        advanceUntilIdle()

        val state = vm.uiState.value
        assertTrue(state.isSuccess)
        assertNull(state.errorMessage)
        assertFalse(state.isLoading)
    }

    @Test
    fun confirmation_failure_setsErrorMessage() = runTest {
        val repo = FakeAppointmentRepository(
            createResult = Result.failure(RuntimeException("create failed"))
        )
        val vm = AppointmentConfirmationViewModel(appointmentRepository = repo)

        vm.setSelection(
            studentId = "student-1",
            departmentName = "Computer Eng",
            serviceId = "service-1",
            serviceName = "Counseling",
            timeSlotId = "slot-1",
            scheduledStartMillis = 123L,
            dateTimeText = "2026-01-11 10:00"
        )

        vm.confirm()
        advanceUntilIdle()

        val state = vm.uiState.value
        assertFalse(state.isSuccess)
        assertNotNull(state.errorMessage)
        assertFalse(state.isLoading)
    }

    private fun appt(
        id: String = UUID.randomUUID().toString(),
        studentId: String = "student-1",
        serviceId: String = "service-1",
        timeSlotId: String = "slot-1",
        startMillis: Long = 0L,
        status: String = "PENDING"
    ): AppointmentRecord {
        return AppointmentRecord(
            id = id,
            studentId = studentId,
            serviceId = serviceId,
            timeSlotId = timeSlotId,
            scheduledStartMillis = startMillis,
            status = status
        )
    }
}
