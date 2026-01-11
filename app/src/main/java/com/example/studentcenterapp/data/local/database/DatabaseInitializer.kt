package com.example.studentcenterapp.data.local.database

import com.example.studentcenterapp.data.local.database.entity.DepartmentEntity
import com.example.studentcenterapp.data.local.database.entity.ServiceEntity
import com.example.studentcenterapp.data.local.database.entity.StaffEntity

/**
 * Database initializer that populates Room database with initial static data
 * on first app launch
 */
class DatabaseInitializer(
    private val database: AppDatabase
) {
    /**
     * Populates database only if it's empty (to avoid duplicate data on app restart)
     */
    suspend fun populateDatabaseIfEmpty() {
        val departmentCount = database.departmentDao().getDepartmentCount()
        
        if (departmentCount == 0) {
            populateDatabase()
        }
    }

    suspend fun populateDatabase() {
        // Clear existing data (if any)
        database.departmentDao().deleteAll()
        database.serviceDao().deleteAll()
        database.staffDao().deleteAll()

        // Insert Departments
        val departments = getInitialDepartments()
        database.departmentDao().insertAll(departments.map { it.toEntity() })

        // Insert Services
        val services = getInitialServices()
        database.serviceDao().insertAll(services.map { it.toEntity() })

        // Insert Staff
        val staff = getInitialStaff()
        database.staffDao().insertAll(staff.map { it.toEntity() })
    }

    private fun getInitialDepartments(): List<com.example.studentcenterapp.model.Department> {
        return listOf(
            com.example.studentcenterapp.model.Department(
                id = "0",
                name = "Individual and Academic Development Office",
                description = "Supports students in their personal and academic improvement.",
                location = "Rectorate Building - Ground Floor"
            ),
            com.example.studentcenterapp.model.Department(
                id = "1",
                name = "Scholarship Management",
                description = "Handles student scholarship processes and financial aid inquiries.",
                location = "Rectorate Building - Ground Floor"
            ),
            com.example.studentcenterapp.model.Department(
                id = "2",
                name = "Career Development",
                description = "Provides career planning services, workshops, and internship support.",
                location = "Rectorate Building - Ground Floor"
            ),
            com.example.studentcenterapp.model.Department(
                id = "3",
                name = "Culture and Art",
                description = "Organizes cultural, artistic, and creative student activities.",
                location = "Rectorate Building - Ground Floor"
            ),
            com.example.studentcenterapp.model.Department(
                id = "4",
                name = "Alumni Relations",
                description = "Connects graduates with the university and coordinates alumni activities.",
                location = "Rectorate Building - Ground Floor"
            ),
            com.example.studentcenterapp.model.Department(
                id = "5",
                name = "Psychological Counseling and Guidance",
                description = "Provides psychological support, counseling, and guidance services.",
                location = "Rectorate Building - Ground Floor"
            ),
            com.example.studentcenterapp.model.Department(
                id = "6",
                name = "Health Nutrition",
                description = "Supports student wellness and offers nutritional guidance.",
                location = "Rectorate Building - Ground Floor"
            ),
            com.example.studentcenterapp.model.Department(
                id = "7",
                name = "Sport",
                description = "Manages sports facilities, training programs, and athletic activities.",
                location = "Rectorate Building - Ground Floor"
            ),
            com.example.studentcenterapp.model.Department(
                id = "8",
                name = "Student Council",
                description = "Represents student interests and coordinates council initiatives.",
                location = "Rectorate Building - Ground Floor"
            ),
            com.example.studentcenterapp.model.Department(
                id = "9",
                name = "Student Clubs",
                description = "Supports student clubs and extracurricular activities.",
                location = "Rectorate Building - Ground Floor"
            )
        )
    }

    private fun getInitialServices(): List<com.example.studentcenterapp.model.Service> {
        return listOf(
            // Department 0 - Individual and Academic Development
            com.example.studentcenterapp.model.Service(
                id = "0",
                departmentId = "0",
                name = "Academic Advising",
                description = "One-on-one session for academic performance and planning.",
                durationMinutes = 30
            ),
            com.example.studentcenterapp.model.Service(
                id = "1",
                departmentId = "0",
                name = "Personal Development Consultation",
                description = "Guidance for student personal growth and skill development.",
                durationMinutes = 45
            ),
            // Department 1 - Scholarship Management
            com.example.studentcenterapp.model.Service(
                id = "2",
                departmentId = "1",
                name = "Scholarship Application Review",
                description = "Review of scholarship eligibility and required documentation.",
                durationMinutes = 20
            ),
            com.example.studentcenterapp.model.Service(
                id = "3",
                departmentId = "1",
                name = "Scholarship Appeal Consultation",
                description = "Meeting to discuss scholarship appeal requests.",
                durationMinutes = 30
            ),
            // Department 2 - Career Development
            com.example.studentcenterapp.model.Service(
                id = "4",
                departmentId = "2",
                name = "CV & Resume Review",
                description = "Professional review of a student's resume or CV.",
                durationMinutes = 30
            ),
            com.example.studentcenterapp.model.Service(
                id = "5",
                departmentId = "2",
                name = "Mock Interview Practice",
                description = "Simulation of real job interview scenarios.",
                durationMinutes = 45
            ),
            // Department 3 - Culture and Art
            com.example.studentcenterapp.model.Service(
                id = "6",
                departmentId = "3",
                name = "Art Club Registration",
                description = "Support for joining art-related workshops and clubs.",
                durationMinutes = 15
            ),
            com.example.studentcenterapp.model.Service(
                id = "7",
                departmentId = "3",
                name = "Event Participation Request",
                description = "Application for cultural and artistic event participation.",
                durationMinutes = 20
            ),
            // Department 4 - Alumni Relations
            com.example.studentcenterapp.model.Service(
                id = "8",
                departmentId = "4",
                name = "Alumni ID Request",
                description = "Assistance for obtaining alumni identification documents.",
                durationMinutes = 15
            ),
            com.example.studentcenterapp.model.Service(
                id = "9",
                departmentId = "4",
                name = "Career Support for Alumni",
                description = "Career development guidance tailored for graduates.",
                durationMinutes = 40
            ),
            // Department 5 - Psychological Counseling
            com.example.studentcenterapp.model.Service(
                id = "10",
                departmentId = "5",
                name = "Individual Counseling Session",
                description = "One-on-one psychological support and guidance.",
                durationMinutes = 50
            ),
            com.example.studentcenterapp.model.Service(
                id = "11",
                departmentId = "5",
                name = "Group Therapy Participation",
                description = "Group-based psychological counseling session.",
                durationMinutes = 60
            ),
            // Department 6 - Health Nutrition
            com.example.studentcenterapp.model.Service(
                id = "12",
                departmentId = "6",
                name = "Diet & Nutrition Consultation",
                description = "Consultation with a nutrition specialist.",
                durationMinutes = 30
            ),
            com.example.studentcenterapp.model.Service(
                id = "13",
                departmentId = "6",
                name = "Health Assessment Review",
                description = "General health and dietary needs evaluation.",
                durationMinutes = 20
            ),
            // Department 7 - Sport
            com.example.studentcenterapp.model.Service(
                id = "14",
                departmentId = "7",
                name = "Gym Training Session",
                description = "Fitness appointment with a sports specialist.",
                durationMinutes = 45
            ),
            com.example.studentcenterapp.model.Service(
                id = "15",
                departmentId = "7",
                name = "Sports Facility Reservation",
                description = "Reservation for sports areas or equipment.",
                durationMinutes = 30
            ),
            // Department 8 - Student Council
            com.example.studentcenterapp.model.Service(
                id = "16",
                departmentId = "8",
                name = "Student Council Support Request",
                description = "Consultation regarding student council issues.",
                durationMinutes = 25
            ),
            com.example.studentcenterapp.model.Service(
                id = "17",
                departmentId = "8",
                name = "Project Proposal Review",
                description = "Evaluation of student club or council project proposals.",
                durationMinutes = 40
            ),
            // Department 9 - Student Clubs
            com.example.studentcenterapp.model.Service(
                id = "18",
                departmentId = "9",
                name = "Club Membership Registration",
                description = "Assistance for joining one of the student clubs.",
                durationMinutes = 15
            ),
            com.example.studentcenterapp.model.Service(
                id = "19",
                departmentId = "9",
                name = "Event Organization Support",
                description = "Consultation for planning student club events.",
                durationMinutes = 30
            )
        )
    }

    private fun getInitialStaff(): List<com.example.studentcenterapp.model.Staff> {
        return listOf(
            com.example.studentcenterapp.model.Staff(
                id = "0",
                name = "Dr. Selin Kaya",
                role = "Student Advisor",
                departmentId = "0"
            ),
            com.example.studentcenterapp.model.Staff(
                id = "1",
                name = "Mert Öztürk",
                role = "Scholarship Coordinator",
                departmentId = "1"
            ),
            com.example.studentcenterapp.model.Staff(
                id = "2",
                name = "Aslı Demir",
                role = "Career Specialist",
                departmentId = "2"
            ),
            com.example.studentcenterapp.model.Staff(
                id = "3",
                name = "Can Yıldırım",
                role = "Arts & Culture Officer",
                departmentId = "3"
            ),
            com.example.studentcenterapp.model.Staff(
                id = "4",
                name = "Ece Güneş",
                role = "Alumni Relations Manager",
                departmentId = "4"
            ),
            com.example.studentcenterapp.model.Staff(
                id = "5",
                name = "Serkan Önal",
                role = "Psychological Counselor",
                departmentId = "5"
            ),
            com.example.studentcenterapp.model.Staff(
                id = "6",
                name = "Derya Arslan",
                role = "Health & Nutrition Specialist",
                departmentId = "6"
            ),
            com.example.studentcenterapp.model.Staff(
                id = "7",
                name = "Umut Kaplan",
                role = "Sports Center Coach",
                departmentId = "7"
            ),
            com.example.studentcenterapp.model.Staff(
                id = "8",
                name = "Melis Aksoy",
                role = "Student Council Coordinator",
                departmentId = "8"
            ),
            com.example.studentcenterapp.model.Staff(
                id = "9",
                name = "Burak Şahin",
                role = "Clubs & Events Moderator",
                departmentId = "9"
            )
        )
    }

    /**
     * Manually trigger database population (useful for testing or re-initialization)
     */
    suspend fun reinitializeDatabase() {
        populateDatabase()
    }
}

