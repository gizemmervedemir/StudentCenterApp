package com.example.studentcenterapp

import com.example.studentcenterapp.data.inmemory.InMemoryDataSource
import com.example.studentcenterapp.data.timeslot.TimeSlotRepositoryImpl
import com.example.studentcenterapp.model.TimeSlot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime

class TimeSlotSelectionViewModelTest {

    @Test
    fun `repository filters slots by serviceId`() = runBlocking {
        // arrange
        val dataSource = InMemoryDataSource()
        val serviceId = "service-1"

        val now = LocalDateTime.now()

        val slots = listOf(
            TimeSlot(
                id = "slot-1",
                serviceId = serviceId,
                start = now,
                end = now.plusMinutes(30),
                isReserved = false
            ),
            TimeSlot(
                id = "slot-2",
                serviceId = "other-service",
                start = now,
                end = now.plusMinutes(30),
                isReserved = false
            )
        )

        dataSource.seedTimeSlots(slots)

        val repository = TimeSlotRepositoryImpl(dataSource)

        // act
        val result = repository.getAvailableSlots(serviceId).first()

        // assert
        assertEquals(1, result.size)
        assertEquals("slot-1", result.first().id)
    }
}