package com.example.studentcenterapp.ui.service

import com.example.studentcenterapp.data.service.ServiceRepository
import com.example.studentcenterapp.model.Service
import com.example.studentcenterapp.ui.state.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ServiceListViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before fun setup() { Dispatchers.setMain(dispatcher) }
    @After fun tearDown() { Dispatchers.resetMain() }

    @Test
    fun loadServices_setsSuccess_whenRepositoryReturnsList() = runTest {
        val repo = FakeRepo(
            flowOf(
                listOf(
                    Service(
                        id = "1",
                        name = "S1",
                        description = "Desc",
                        durationMinutes = 30,
                        departmentId = "D1"
                    )
                )
            )
        )

        val vm = ServiceListViewModel(repo)

        vm.loadServices("D1")
        advanceUntilIdle()

        val state = vm.uiState.value
        assertTrue(state is UiState.Success && state.data.isNotEmpty())
    }

    @Test
    fun loadServices_setsError_whenRepositoryThrows() = runTest {
        val repo = FakeRepo(flow { throw RuntimeException("boom") })
        val vm = ServiceListViewModel(repo)

        vm.loadServices("D1")
        advanceUntilIdle()

        val state = vm.uiState.value
        assertTrue(state is UiState.Error && state.message.contains("boom"))
    }

    private class FakeRepo(private val source: Flow<List<Service>>) : ServiceRepository {
        override fun getServicesByDepartment(departmentId: String): Flow<List<Service>> = source
    }
}
