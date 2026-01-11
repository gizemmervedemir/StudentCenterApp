package com.example.studentcenterapp.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.studentcenterapp.data.local.database.entity.DepartmentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DepartmentDao {
    @Query("SELECT * FROM departments")
    fun getAllDepartments(): Flow<List<DepartmentEntity>>

    @Query("SELECT COUNT(*) FROM departments")
    suspend fun getDepartmentCount(): Int

    @Query("SELECT * FROM departments WHERE id = :id")
    suspend fun getDepartmentById(id: String): DepartmentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(departments: List<DepartmentEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(department: DepartmentEntity)

    @Query("DELETE FROM departments")
    suspend fun deleteAll()
}

