package com.example.studentcenterapp

import com.example.studentcenterapp.data.timeslot.TimeSlotRepository
import com.example.studentcenterapp.model.TimeSlot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class TimeSlotSelectionViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `loads slots successfully for serviceId`() = runTest {
        val repo = FakeTimeSlotRepository(
            initialSlots = listOf(
                slot("s1", "service-1", reserved = false),
                slot("s2", "service-1", reserved = false),
                slot("x1", "service-2", reserved = false),
                slot("r1", "service-1", reserved = true) // reserved -> gelmemeli
            )
        )

        val vm = TimeSlotSelectionViewModel(repo, "service-1")

        advanceUntilIdle()

        val state = vm.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertEquals(2, state.slots.size)
        assertTrue(state.slots.all { it.serviceId == "service-1" })
        assertTrue(state.slots.none { it.isReserved })
    }

    @Test
    fun `empty state when no slots available`() = runTest {
        val repo = FakeTimeSlotRepository(
            initialSlots = listOf(
                slot("r1", "service-1", reserved = true), // reserved
                slot("x1", "service-2", reserved = false) // başka service
            )
        )

        val vm = TimeSlotSelectionViewModel(repo, "service-1")

        advanceUntilIdle()

        val state = vm.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertTrue(state.slots.isEmpty())
    }

    @Test
    fun `error state when repository throws`() = runTest {
        val repo = object : TimeSlotRepository {
            override fun getAvailableSlots(serviceId: String): Flow<List<TimeSlot>> =
                flow { throw RuntimeException("boom") }

            override suspend fun reserveSlot(slotId: String): Boolean = false
        }

        val vm = TimeSlotSelectionViewModel(repo, "service-1")

        advanceUntilIdle()

        val state = vm.uiState.value
        assertFalse(state.isLoading)
        assertEquals("boom", state.errorMessage)
        assertTrue(state.slots.isEmpty())
    }

    @Test
    fun `selecting a slot updates selectedSlotId`() = runTest {
        val repo = FakeTimeSlotRepository(
            initialSlots = listOf(
                slot("s1", "service-1", reserved = false),
                slot("s2", "service-1", reserved = false)
            )
        )

        val vm = TimeSlotSelectionViewModel(repo, "service-1")
        advanceUntilIdle()

        val first = vm.uiState.value.slots.first()
        vm.onSlotClicked(first)

        assertEquals(first.id, vm.uiState.value.selectedSlotId)
    }

    @Test
    fun `confirmSelectedSlot reserves selected slot and returns true`() = runTest {
        val repo = FakeTimeSlotRepository(
            initialSlots = listOf(
                slot("s1", "service-1", reserved = false),
                slot("s2", "service-1", reserved = false)
            )
        )

        val vm = TimeSlotSelectionViewModel(repo, "service-1")
        advanceUntilIdle()

        vm.onSlotClicked(vm.uiState.value.slots.first { it.id == "s1" })

        val ok = vm.confirmSelectedSlot()
        assertTrue(ok)

        // Repo tarafında reserve oldu mu?
        assertTrue(repo.isReserved("s1"))
    }

    // -----------------------------
    // Helpers
    // -----------------------------

    private fun slot(id: String, serviceId: String, reserved: Boolean): TimeSlot {
        return TimeSlot(
            id = id,
            serviceId = serviceId,
            date = "2026-01-11",
            startTime = "10:00",
            endTime = "10:30",
            isReserved = reserved
        )
    }

    private class FakeTimeSlotRepository(
        initialSlots: List<TimeSlot>
    ) : TimeSlotRepository {

        private val slotsFlow = MutableStateFlow(initialSlots)

        override fun getAvailableSlots(serviceId: String): Flow<List<TimeSlot>> {
            // ViewModel’in beklediği gibi: serviceId filtresi + reserved olmayanlar
            return MutableStateFlow(
                slotsFlow.value.filter { it.serviceId == serviceId && !it.isReserved }
            )
        }

        override suspend fun reserveSlot(slotId: String): Boolean {
            val current = slotsFlow.value.toMutableList()
            val idx = current.indexOfFirst { it.id == slotId && !it.isReserved }
            if (idx == -1) return false

            current[idx] = current[idx].copy(isReserved = true)
            slotsFlow.value = current
            return true
        }

        fun isReserved(slotId: String): Boolean =
            slotsFlow.value.firstOrNull { it.id == slotId }?.isReserved == true
    }
}

/**
 * Dispatchers.Main test ortamında yok => setMain şart.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val dispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher() {

    override fun starting(description: Description) {
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}