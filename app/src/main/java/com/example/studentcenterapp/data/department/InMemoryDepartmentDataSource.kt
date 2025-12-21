package com.example.studentcenterapp.data.department

import com.example.studentcenterapp.model.Department
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class InMemoryDepartmentDataSource : DepartmentDataSource {

    private val departments = listOf(
        Department(id = "dep1", name = "Bireysel ve Akademik Gelişim", description = "…"),
        Department(id = "dep2", name = "Burs Yönetimi", description = "…"),
        Department(id = "dep3", name = "Kariyer Gelişimi", description = "…"),
        Department(id = "dep4", name = "Kültür Sanat", description = "…"),
        Department(id = "dep5", name = "Mezunlarla İletişim", description = "…"),
        Department(id = "dep6", name = "Psikolojik Danışmanlık ve Rehberlik", description = "…"),
        Department(id = "dep7", name = "Sağlıklı Beslenme ve Diyet", description = "…"),
        Department(id = "dep8", name = "Spor Yönetimi", description = "…"),
    )

    override fun getDepartments(): Flow<List<Department>> = flowOf(departments)
}