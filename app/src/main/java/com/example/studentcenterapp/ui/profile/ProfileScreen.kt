package com.example.studentcenterapp.ui.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.studentcenterapp.ui.common.AppBottomBar
import com.example.studentcenterapp.ui.common.AppTab
import com.example.studentcenterapp.ui.common.staffBottomTabs
import com.example.studentcenterapp.ui.common.studentBottomTabs
import com.example.studentcenterapp.ui.theme.PrimaryBlue

@Composable
fun ProfileScreen(
    userName: String,
    userEmail: String,
    isUserStaff: Boolean,
    onNavigateToPersonalInfos: () -> Unit,
    onLogout: () -> Unit,
    currentRoute: String?,
    onTabSelected: (AppTab) -> Unit
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            selectedImageUri = uri
            // upload callback / viewModel burada bağlanacak
        }
    }

    Scaffold(
        containerColor = PrimaryBlue, // ✅ arka plan mavi sabit
        bottomBar = {
            AppBottomBar(
                tabs = if (isUserStaff) staffBottomTabs else studentBottomTabs,
                currentRoute = currentRoute,
                onTabSelected = onTabSelected
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(PrimaryBlue)
        ) {
            // ✅ Header alanına alttan ekstra boşluk: avatar aşağı taşsın
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                ProfileHeader(
                    userName = userName,
                    userEmail = userEmail,
                    onImageClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                )
            }

            // ✅ Kartı yukarı çek: avatarın “üstüne yapışmış” gibi dursun
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .offset(y = (-28).dp),   // <-- burayı 20-40 dp arası ayarlayabilirsin
                color = Color(0xFFF2F2F2),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 40.dp,
                            start = 20.dp,
                            end = 20.dp
                        ) // <-- yukarı çıktığı için biraz top padding verdik
                ) {
                    ProfileMenuItem(
                        icon = Icons.Default.Person,
                        title = "Kişisel Bilgiler",
                        onClick = onNavigateToPersonalInfos
                    )
                    ProfileMenuItem(
                        icon = Icons.Default.Lock,
                        title = "Şifre Değiştir",
                        onClick = { /* TODO */ }
                    )
                    ProfileMenuItem(
                        icon = Icons.Default.ExitToApp,
                        title = "Oturumu Kapat",
                        textColor = Color.Red,
                        onClick = onLogout
                    )
                }
            }
        }
    }
}