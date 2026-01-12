package com.example.studentcenterapp.ui.service

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.studentcenterapp.R
import com.example.studentcenterapp.model.Service
import com.example.studentcenterapp.ui.common.*
import com.example.studentcenterapp.ui.common.studentBottomTabs // Eksik olan spesifik import
import com.example.studentcenterapp.ui.state.UiState
import com.example.studentcenterapp.ui.theme.DarkText
import com.example.studentcenterapp.ui.theme.Figtree
import com.example.studentcenterapp.ui.theme.PrimaryBlue
import com.example.studentcenterapp.ui.theme.lightText

@Composable
fun ServiceListScreen(
    state: UiState<List<Service>>,
    currentRoute: String?,
    onTabSelected: (AppTab) -> Unit,
    onServiceClick: (serviceId: String, serviceName: String, type: String) -> Unit,
    onBackClick: () -> Unit,
    onRetry: (() -> Unit)? = null
) {
    val scrollState = rememberScrollState()

    var showTypeSelection by remember { mutableStateOf(false) }
    var selectedServiceInfo by remember { mutableStateOf<Pair<String, String>?>(null) }

    Scaffold(
        topBar = { AppTopBar(title = "") },
        bottomBar = {
            // bottomTabs yerine studentBottomTabs kullanıldı
            AppBottomBar(
                tabs = studentBottomTabs,
                currentRoute = currentRoute,
                onTabSelected = onTabSelected
            )
        },
        containerColor = PrimaryBlue
    ) { innerPadding ->
        ContentCard(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (state) {
                is UiState.Loading -> LoadingView()
                is UiState.Error -> ErrorView(message = state.message, onRetry = onRetry)
                is UiState.Success -> {
                    val service = state.data.firstOrNull()

                    if (service == null) {
                        EmptyStateScreen(config = EmptyStateConfig(title = "Hizmet Yok", message = "Detay bulunamadı."))
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 24.dp, vertical = 16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = "Geri",
                                tint = Color(0xFF707070),
                                modifier = Modifier
                                    .clickable { onBackClick() }
                                    .size(24.dp)
                            )

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .verticalScroll(scrollState)
                                    .fillMaxWidth()
                            ) {
                                Spacer(modifier = Modifier.height(8.dp))
                                val imageResId = getDrawableResource(service.imageName)
                                Box(
                                    modifier = Modifier.fillMaxWidth().height(200.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = imageResId),
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Fit
                                    )
                                }
                                Spacer(modifier = Modifier.height(24.dp))
                                Text(
                                    text = service.name,
                                    style = TextStyle(fontFamily = Figtree, fontWeight = FontWeight.Bold, fontSize = 24.sp),
                                    color = DarkText
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = service.description,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = lightText,
                                    textAlign = TextAlign.Start
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                            }

                            PrimaryButton(
                                text = "Randevu Al",
                                onClick = {
                                    selectedServiceInfo = Pair(service.id, service.name)
                                    showTypeSelection = true
                                },
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }
            }
        }
    }

    if (showTypeSelection) {
        Dialog(onDismissRequest = { if (showTypeSelection) showTypeSelection = false }) {
            Surface(
                shape = RoundedCornerShape(28.dp),
                color = Color(0xFFF2F2F2),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Lütfen seçim yapınız",
                        style = TextStyle(fontFamily = Figtree, fontWeight = FontWeight.Bold, fontSize = 18.sp),
                        color = DarkText,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = {
                                val info = selectedServiceInfo
                                if (info != null) {
                                    onServiceClick(info.first, info.second, "online")
                                    if (showTypeSelection) showTypeSelection = false
                                }
                            },
                            modifier = Modifier.weight(1f).height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7D7D7D)),
                            shape = RoundedCornerShape(25.dp)
                        ) {
                            Text("Online", color = Color.White, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                val info = selectedServiceInfo
                                if (info != null) {
                                    onServiceClick(info.first, info.second, "office")
                                    if (showTypeSelection) showTypeSelection = false
                                }
                            },
                            modifier = Modifier.weight(1f).height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7D7D7D)),
                            shape = RoundedCornerShape(25.dp)
                        ) {
                            Text("Ofis", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

private fun getDrawableResource(imageName: String?): Int {
    return when (imageName) {
        "ic_gelisim" -> R.drawable.ic_gelisim
        "ic_burs" -> R.drawable.ic_burs
        "ic_kariyer" -> R.drawable.ic_kariyer
        "beslenme__" -> R.drawable.beslenme__
        "ic_mezun" -> R.drawable.ic_mezun
        "ic_psikolojik" -> R.drawable.yenipng
        "ic_kultur" -> R.drawable.ic_kultur
        "ic_spor" -> R.drawable.ic_spor
        else -> R.drawable.logo_oldx
    }
}