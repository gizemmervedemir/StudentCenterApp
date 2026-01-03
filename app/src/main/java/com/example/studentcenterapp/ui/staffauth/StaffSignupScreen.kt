package com.example.studentcenterapp.ui.staffauth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.studentcenterapp.ui.common.AppTopBar
import com.example.studentcenterapp.ui.common.ContentCard
import com.example.studentcenterapp.ui.common.PrimaryButton
import com.example.studentcenterapp.ui.state.UiState
import com.example.studentcenterapp.ui.theme.PrimaryBlue

@Composable
fun StaffSignupScreen(
    vm: StaffSignupViewModel,
    onSuccess: (staffId: String) -> Unit
) {
    val name by vm.name.collectAsState()
    val email by vm.email.collectAsState()
    val password by vm.password.collectAsState()
    val state by vm.uiState.collectAsState()
    val navStaffId by vm.navStaffId.collectAsState()

    if (navStaffId != null) {
        val id = navStaffId!!
        vm.consumeNav()
        onSuccess(id)
    }

    Scaffold(
        topBar = { AppTopBar(title = "Staff Signup") },
        containerColor = PrimaryBlue
    ) { padding ->
        ContentCard(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Create account", style = MaterialTheme.typography.titleLarge)

                OutlinedTextField(
                    value = name,
                    onValueChange = vm::onNameChange,
                    label = { Text("Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = vm::onEmailChange,
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = vm::onPasswordChange,
                    label = { Text("Password") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                if (state is UiState.Error) {
                    Text(
                        text = (state as UiState.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                PrimaryButton(
                    text = if (state is UiState.Loading) "Signing up..." else "Sign up",
                    enabled = state !is UiState.Loading,
                    onClick = vm::signup
                )
            }
        }
    }
}