package com.example.studentcenterapp.data.student

import com.example.studentcenterapp.model.Student

class StudentRepositoryImpl(
    private val dataSource: StudentDataSource
) : StudentRepository {

    override suspend fun getCurrentStudent(): Student {
        return dataSource.getCurrentStudent()
    }

    override suspend fun getStudentById(id: String): Student? {
        return dataSource.getStudentById(id)
    }

    override suspend fun login(email: String, password: String): Result<Student> {
        kotlinx.coroutines.delay(1000) // Gerçekçi bir bekleme

        // Şimdilik basit bir kontrol: E-posta eylul@example.com ve şifre 123456 ise girsin
        return if (email == "eylul@example.com" && password == "123456") {
            Result.success(getStudentById("stu2")!!) // Senin veri kaynağındaki Eylül
        } else {
            Result.failure(Exception("E-posta veya şifre hatalı!"))
        }
    }

    override suspend fun signup(
        name: String,
        surname: String,
        schoolNumber: String,
        email: String,
        password: String,
        birthDay: String,
        birthMonth: String,
        birthYear: String
    ): Result<Student> {
        kotlinx.coroutines.delay(1000)
        // Kayıt başarılı simülasyonu
        return Result.success(Student("new_id", "$name $surname", email, schoolNumber))
    }
}