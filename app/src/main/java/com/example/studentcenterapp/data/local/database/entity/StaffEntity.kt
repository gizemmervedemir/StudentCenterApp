package com.example.studentcenterapp.data.local.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.studentcenterapp.model.Staff

@Entity(
    tableName = "staff",
    indices = [Index(value = ["departmentId"])],
    foreignKeys = [
        ForeignKey(
            entity = DepartmentEntity::class,
            parentColumns = ["id"],
            childColumns = ["departmentId"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class StaffEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val role: String,
    val departmentId: String
) {
    fun toModel(): Staff {
        return Staff(
            id = id,
            name = name,
            role = role,
            departmentId = departmentId
        )
    }
}

fun Staff.toEntity(): StaffEntity {
    return StaffEntity(
        id = id,
        name = name,
        role = role,
        departmentId = departmentId
    )
}

