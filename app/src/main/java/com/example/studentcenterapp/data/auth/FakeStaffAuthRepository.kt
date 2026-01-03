package com.example.studentcenterapp.data.auth

import kotlinx.coroutines.delay

class FakeStaffAuthRepository : StaffAuthRepository {
    override suspend fun login(email: String, password: String): Result<String> {
        delay(250)
        if (email.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("Email and password are required"))
        }
        return Result.success("staff1")
    }

    override suspend fun signup(name: String, email: String, password: String): Result<String> {
        delay(250)
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("All fields are required"))
        }
        return Result.success("staff1")
    }
}
