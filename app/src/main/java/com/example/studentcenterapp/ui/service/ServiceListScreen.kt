package com.example.studentcenterapp.ui.service

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studentcenterapp.R
import com.example.studentcenterapp.model.Service
import com.example.studentcenterapp.ui.common.*
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
    onServiceClick: (serviceId: String, serviceName: String) -> Unit,
    onBackClick: () -> Unit, // Geri dönüş için yeni parametre
    onRetry: (() -> Unit)? = null
) {
    // Kaydırma durumunu saklamak için
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            AppTopBar(title = "")
        },
        bottomBar = {
            AppBottomBar(tabs = bottomTabs, currentRoute = currentRoute, onTabSelected = onTabSelected)
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
                            // 1. Geri Butonu
                            Icon(
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = "Geri",
                                tint = Color(0xFF707070),
                                modifier = Modifier
                                    .clickable { onBackClick() }
                                    .size(24.dp)
                            )

                            // 2. Kaydırılabilir İçerik Alanı
                            // weight(1f) vererek butonu aşağı iteriz ama içeriği kendi içinde kaydırırız
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .verticalScroll(scrollState)
                                    .fillMaxWidth()
                            ) {
                                Spacer(modifier = Modifier.height(8.dp))

                                // Dinamik Görsel
                                val imageResId = getDrawableResource(service.imageName)
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp), // Sabit yükseklik düzeni korur
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

                                // Birim Başlığı
                                Text(
                                    text = service.name,
                                    style = TextStyle(
                                        fontFamily = Figtree,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 24.sp
                                    ),
                                    color = DarkText
                                )

                            Spacer(modifier = Modifier.height(12.dp))

                                // Birim Açıklaması
                                Text(
                                    text = service.description,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = lightText,
                                    textAlign = TextAlign.Start
                                )

                                // Metin bitince butonla arasında nefes payı bırakmak için
                                Spacer(modifier = Modifier.height(16.dp))
                            }

                            // 3. Sabit "Randevu Al" Butonu
                            // Column dışında olduğu için her zaman en altta çakılı durur
                            PrimaryButton(
                                text = "Randevu Al",
                                onClick = { onServiceClick(service.id, service.name) },
                                modifier = Modifier.align(Alignment.CenterHorizontally))
                            )
                        }
                    }
                }
            }
        }
    }
}
private fun ColumnScope.getDrawableResource(imageName: String?): Int {
    return when (imageName) {
        "ic_gelisim" -> R.drawable.ic_gelisim
        "ic_burs" -> R.drawable.ic_burs
        "ic_kariyer" -> R.drawable.ic_kariyer
        "beslenme__" -> R.drawable.beslenme__
        "ic_mezun" -> R.drawable.ic_mezun
        "ic_psikolojik" -> R.drawable.yenipng
        "ic_kultur" -> R.drawable.ic_kultur
        "ic_spor" -> R.drawable.ic_spor

        // Buraya kullandığın tüm ikonları ekle
        else -> R.drawable.logo_oldx // Varsayılan görsel
    }
}
