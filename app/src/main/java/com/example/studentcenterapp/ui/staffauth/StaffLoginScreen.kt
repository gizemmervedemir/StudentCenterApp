package com.example.studentcenterapp.ui.staffauth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
fun StaffLoginScreen(
    vm: StaffLoginViewModel,
    onSignupClick: () -> Unit,
    onSuccess: (staffId: String) -> Unit
) {
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
        topBar = { AppTopBar(title = "Staff Login") },
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
                Text("Welcome back", style = MaterialTheme.typography.titleLarge)

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
                    text = if (state is UiState.Loading) "Logging in..." else "Login",
                    enabled = state !is UiState.Loading,
                    onClick = vm::login
                )

                Spacer(Modifier.height(4.dp))

                TextButton(onClick = onSignupClick) {
                    Text("Don’t have an account? Sign up")
                }
            }
        }
    }
}
