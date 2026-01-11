package com.example.studentcenterapp.data.timeslot

import com.example.studentcenterapp.model.TimeSlot
import kotlinx.coroutines.flow.Flow

interface TimeSlotRepository {

    /**
     * Seçilen serviceId için uygun (rezerve edilmemiş) slotları döner.
     */
    fun getAvailableSlots(serviceId: String): Flow<List<TimeSlot>>

    /**
     * Slot ID'yi rezerve etmeye çalışır.
     * Başarılıysa true, aksi halde false döner.
     */
    suspend fun reserveSlot(slotId: String): Boolean
}