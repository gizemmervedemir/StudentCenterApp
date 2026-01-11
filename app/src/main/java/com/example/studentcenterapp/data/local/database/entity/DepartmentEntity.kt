package com.example.studentcenterapp.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.studentcenterapp.model.Department

@Entity(tableName = "departments")
data class DepartmentEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val location: String = ""
) {
    fun toModel(): Department {
        return Department(
            id = id,
            name = name,
            description = description,
            location = location
        )
    }
}

fun Department.toEntity(): DepartmentEntity {
    return DepartmentEntity(
        id = id,
        name = name,
        description = description,
        location = location
    )
}

