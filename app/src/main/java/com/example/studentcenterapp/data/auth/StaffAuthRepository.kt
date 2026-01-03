package com.example.studentcenterapp.data.auth

interface StaffAuthRepository {
    suspend fun login(email: String, password: String): Result<String>   // returns staffId
    suspend fun signup(name: String, email: String, password: String): Result<String>
}
