package com.example.studentcenterapp.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_session")

class UserSessionManager(private val context: Context) {

    private object PreferencesKeys {
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val STUDENT_ID = stringPreferencesKey("student_id")
        val STUDENT_EMAIL = stringPreferencesKey("student_email")
        val STUDENT_NAME = stringPreferencesKey("student_name")
        val STAFF_ID = stringPreferencesKey("staff_id")
        val STAFF_EMAIL = stringPreferencesKey("staff_email")
        val USER_TYPE = stringPreferencesKey("user_type") // "student" or "staff"
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.IS_LOGGED_IN] ?: false
    }

    val currentStudentId: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.STUDENT_ID]
    }

    val currentStaffId: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.STAFF_ID]
    }

    val userType: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.USER_TYPE]
    }

    suspend fun saveStudentSession(
        studentId: String,
        email: String,
        name: String
    ) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_LOGGED_IN] = true
            preferences[PreferencesKeys.STUDENT_ID] = studentId
            preferences[PreferencesKeys.STUDENT_EMAIL] = email
            preferences[PreferencesKeys.STUDENT_NAME] = name
            preferences[PreferencesKeys.USER_TYPE] = "student"
            // Clear staff data
            preferences.remove(PreferencesKeys.STAFF_ID)
            preferences.remove(PreferencesKeys.STAFF_EMAIL)
        }
    }

    suspend fun saveStaffSession(
        staffId: String,
        email: String
    ) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_LOGGED_IN] = true
            preferences[PreferencesKeys.STAFF_ID] = staffId
            preferences[PreferencesKeys.STAFF_EMAIL] = email
            preferences[PreferencesKeys.USER_TYPE] = "staff"
            // Clear student data
            preferences.remove(PreferencesKeys.STUDENT_ID)
            preferences.remove(PreferencesKeys.STUDENT_EMAIL)
            preferences.remove(PreferencesKeys.STUDENT_NAME)
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    suspend fun getStudentIdSync(): String? {
        return context.dataStore.data.map { it[PreferencesKeys.STUDENT_ID] }.first()
    }

    suspend fun getStaffIdSync(): String? {
        return context.dataStore.data.map { it[PreferencesKeys.STAFF_ID] }.first()
    }
}

