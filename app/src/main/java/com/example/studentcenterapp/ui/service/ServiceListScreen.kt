package com.example.studentcenterapp.ui.service

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.studentcenterapp.model.Service
import com.example.studentcenterapp.ui.common.AppBottomBar
import com.example.studentcenterapp.ui.common.AppTab
import com.example.studentcenterapp.ui.common.AppTopBar
import com.example.studentcenterapp.ui.common.ContentCard
import com.example.studentcenterapp.ui.common.bottomTabs
import com.example.studentcenterapp.ui.state.UiState
import com.example.studentcenterapp.ui.theme.PrimaryBlue

@Composable
fun ServiceListScreen(
    state: UiState<List<Service>>,
    currentRoute: String?,
    onTabSelected: (AppTab) -> Unit,
    onServiceClick: (serviceId: String) -> Unit
) {
    Scaffold(
        topBar = { AppTopBar(title = "Services") },
        bottomBar = {
            AppBottomBar(
                tabs = bottomTabs,
                currentRoute = currentRoute,
                onTabSelected = onTabSelected
            )
        },
        containerColor = PrimaryBlue
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ContentCard(modifier = Modifier.fillMaxSize()) {
                when (state) {
                    is UiState.Loading -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }

                    is UiState.Error -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                text = state.message,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }

                    is UiState.Success -> {
                        val services = state.data
                        if (services.isEmpty()) {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text("No services found.", style = MaterialTheme.typography.bodyLarge)
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 21.dp, vertical = 16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(services, key = { it.id }) { service ->
                                    ServiceListItem(
                                        service = service,
                                        onClick = { onServiceClick(service.id) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ServiceListItem(
    service: Service,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = service.name, style = MaterialTheme.typography.titleMedium)
            Text(
                text = service.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 6.dp)
            )
            Text(
                text = "${service.durationMinutes} min",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
