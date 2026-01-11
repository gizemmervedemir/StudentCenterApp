package com.example.studentcenterapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.studentcenterapp.data.local.database.dao.DepartmentDao
import com.example.studentcenterapp.data.local.database.dao.ServiceDao
import com.example.studentcenterapp.data.local.database.dao.StaffDao
import com.example.studentcenterapp.data.local.database.entity.DepartmentEntity
import com.example.studentcenterapp.data.local.database.entity.ServiceEntity
import com.example.studentcenterapp.data.local.database.entity.StaffEntity

@Database(
    entities = [
        DepartmentEntity::class,
        ServiceEntity::class,
        StaffEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun departmentDao(): DepartmentDao
    abstract fun serviceDao(): ServiceDao
    abstract fun staffDao(): StaffDao

    companion object {
        const val DATABASE_NAME = "student_center_db"
    }
}

