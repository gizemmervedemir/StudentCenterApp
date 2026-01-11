package com.example.studentcenterapp.data.timeslot

import com.example.studentcenterapp.data.inmemory.InMemoryDataSource
import com.example.studentcenterapp.model.TimeSlot
import kotlinx.coroutines.flow.Flow

/**
 * TimeSlot ile ilgili tüm işlemler bu repo üzerinden yapılır.
 * ViewModel'ler interface'i kullanır, içeride InMemoryDataSource var.
 */
class TimeSlotRepositoryImpl(
    private val inMemoryDataSource: InMemoryDataSource
) : TimeSlotRepository {

    override fun getAvailableSlots(serviceId: String): Flow<List<TimeSlot>> {
        return inMemoryDataSource.getAvailableSlotsForService(serviceId)
    }

    override suspend fun reserveSlot(slotId: String): Boolean {
        return inMemoryDataSource.reserveSlot(slotId)
    }
}