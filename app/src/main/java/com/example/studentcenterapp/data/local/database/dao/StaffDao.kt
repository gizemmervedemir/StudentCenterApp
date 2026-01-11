package com.example.studentcenterapp.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.studentcenterapp.data.local.database.entity.StaffEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StaffDao {
    @Query("SELECT * FROM staff")
    fun getAllStaff(): Flow<List<StaffEntity>>

    @Query("SELECT * FROM staff WHERE departmentId = :departmentId")
    fun getStaffByDepartmentId(departmentId: String): Flow<List<StaffEntity>>

    @Query("SELECT * FROM staff WHERE id = :id")
    suspend fun getStaffById(id: String): StaffEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(staff: List<StaffEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(staff: StaffEntity)

    @Query("DELETE FROM staff")
    suspend fun deleteAll()
}

