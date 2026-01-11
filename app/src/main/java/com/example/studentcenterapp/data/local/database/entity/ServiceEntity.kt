package com.example.studentcenterapp.data.local.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index
import com.example.studentcenterapp.model.Service

@Entity(
    tableName = "services",
    indices = [Index(value = ["departmentId"])],
    foreignKeys = [
        ForeignKey(
            entity = DepartmentEntity::class,
            parentColumns = ["id"],
            childColumns = ["departmentId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ServiceEntity(
    @PrimaryKey
    val id: String,
    val departmentId: String,
    val name: String,
    val description: String,
    val durationMinutes: Int
) {
    fun toModel(): Service {
        return Service(
            id = id,
            departmentId = departmentId,
            name = name,
            description = description,
            durationMinutes = durationMinutes
        )
    }
}

fun Service.toEntity(): ServiceEntity {
    return ServiceEntity(
        id = id,
        departmentId = departmentId,
        name = name,
        description = description,
        durationMinutes = durationMinutes
    )
}

