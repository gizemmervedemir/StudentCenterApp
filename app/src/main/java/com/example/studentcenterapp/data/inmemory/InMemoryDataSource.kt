package com.example.studentcenterapp.data.inmemory

import com.example.studentcenterapp.model.Appointment
import com.example.studentcenterapp.model.Department
import com.example.studentcenterapp.model.Service
import com.example.studentcenterapp.model.TimeSlot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

/**
 * Uygulama içi sahte database.
 * App açıkken RAM'de duruyor, kalıcı kayıt yok.
 */
class InMemoryDataSource {

    private val departments = MutableStateFlow<List<Department>>(emptyList())
    private val services = MutableStateFlow<List<Service>>(emptyList())
    private val timeSlots = MutableStateFlow<List<TimeSlot>>(emptyList())
    private val appointments = MutableStateFlow<List<Appointment>>(emptyList())

    // ---- Seed / initial data ----

    fun seedDepartments(items: List<Department>) {
        departments.value = items
    }

    fun seedServices(items: List<Service>) {
        services.value = items
    }

    fun seedTimeSlots(items: List<TimeSlot>) {
        timeSlots.value = items
    }

    fun seedAppointments(items: List<Appointment>) {
        appointments.value = items
    }

    // ---- Time slot operations ----

    fun getAvailableSlotsForService(serviceId: String): Flow<List<TimeSlot>> {
        return timeSlots
            .asStateFlow()
            .map { list ->
                list.filter { it.serviceId == serviceId && !it.isReserved }
            }
    }

    /**
     * Slot ID'ye göre rezerve eder.
     * Başarılıysa true, aksi halde false döner.
     */
    suspend fun reserveSlot(slotId: String): Boolean {
        val current = timeSlots.value
        val index = current.indexOfFirst { it.id == slotId && !it.isReserved }

        if (index == -1) return false

        val updatedSlot = current[index].copy(isReserved = true)
        val updatedList = current.toMutableList().also {
            it[index] = updatedSlot
        }
        timeSlots.value = updatedList
        return true
    }

    // ---- Appointment operations ----

    fun getAppointments(): Flow<List<Appointment>> = appointments.asStateFlow()

    suspend fun upsertAppointment(appointment: Appointment) {
        val current = appointments.value.toMutableList()
        val index = current.indexOfFirst { it.id == appointment.id }
        if (index == -1) {
            current.add(appointment)
        } else {
            current[index] = appointment
        }
        appointments.value = current
    }
}