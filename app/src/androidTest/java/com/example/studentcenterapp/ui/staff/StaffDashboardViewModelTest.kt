package com.example.studentcenterapp.ui.staff

import com.example.studentcenterapp.data.staff.StaffRepository
import com.example.studentcenterapp.model.Appointment
import com.example.studentcenterapp.ui.state.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StaffDashboardViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadsPendingAppointmentsForStaff() = runTest {
        val repo = FakeStaffRepo().apply {
            seed(
                listOf(
                    appt(id = "a1", staffId = "staff1", status = "pending"),
                    appt(id = "a2", staffId = "staff1", status = "pending"),
                    appt(id = "a3", staffId = "staff2", status = "pending"),
                    appt(id = "a4", staffId = "staff1", status = "approved")
                )
            )
        }

        val vm = StaffDashboardViewModel(staffId = "staff1", repository = repo)

        advanceUntilIdle()

        val state = vm.uiState.value
        assertTrue(state is UiState.Success)

        val list = (state as UiState.Success<List<Appointment>>).data
        assertEquals(2, list.size)
        assertTrue(list.all { it.staffId == "staff1" && it.status == "pending" })
    }

    @Test
    fun approveUpdatesStatusAndRemovesFromPending() = runTest {
        val repo = FakeStaffRepo().apply {
            seed(listOf(appt(id = "a1", staffId = "staff1", status = "pending")))
        }

        val vm = StaffDashboardViewModel(staffId = "staff1", repository = repo)

        advanceUntilIdle()

        vm.approve("a1")
        advanceUntilIdle()

        val state = vm.uiState.value
        assertTrue(state is UiState.Success)

        val list = (state as UiState.Success<List<Appointment>>).data
        assertTrue(list.isEmpty())
    }

    @Test
    fun rejectInvalidIdSetsActionErrorWithoutCrash() = runTest {
        val repo = FakeStaffRepo().apply { seed(emptyList()) }
        val vm = StaffDashboardViewModel(staffId = "staff1", repository = repo)

        advanceUntilIdle()

        vm.reject("missing")
        advanceUntilIdle()

        val err = vm.actionError.value
        assertTrue(!err.isNullOrBlank())
    }

    private class FakeStaffRepo : StaffRepository {

        private val store = mutableMapOf<String, Appointment>()
        private val pendingFlow = MutableStateFlow<List<Appointment>>(emptyList())

        fun seed(list: List<Appointment>) {
            store.clear()
            list.forEach { store[it.id] = it }
            emitFor("staff1")
            emitFor("staff2")
        }

        private fun emitFor(staffId: String) {
            val pending = store.values
                .filter { it.staffId == staffId && it.status == "pending" }
                .sortedBy { it.id }

            // tek flow tutuyoruz; testte sadece staff1'e bakıyoruz
            if (staffId == "staff1") pendingFlow.value = pending
        }

        override fun getPendingAppointmentsForStaff(staffId: String): Flow<List<Appointment>> {
            // basit: sadece staff1’i simüle ediyoruz
            return pendingFlow.asStateFlow()
        }

        override suspend fun approveAppointment(appointmentId: String): Result<Unit> {
            val a = store[appointmentId]
                ?: return Result.failure(IllegalArgumentException("Invalid appointmentId"))

            store[appointmentId] = a.copy(status = "approved")
            emitFor(a.staffId)
            return Result.success(Unit)
        }

        override suspend fun rejectAppointment(appointmentId: String): Result<Unit> {
            val a = store[appointmentId]
                ?: return Result.failure(IllegalArgumentException("Invalid appointmentId"))

            store[appointmentId] = a.copy(status = "cancelled")
            emitFor(a.staffId)
            return Result.success(Unit)
        }
    }

    private fun appt(id: String, staffId: String, status: String) =
        Appointment(
            id = id,
            studentId = "s1",
            staffId = staffId,
            serviceId = "svc1",
            timeSlotId = "t1",
            status = status
        )
}
