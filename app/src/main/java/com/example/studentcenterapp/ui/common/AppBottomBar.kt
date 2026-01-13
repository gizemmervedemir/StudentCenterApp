package com.example.studentcenterapp.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.studentcenterapp.ui.theme.BottomBarBackground
import com.example.studentcenterapp.ui.theme.PrimaryGreen
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.studentcenterapp.R
import com.example.studentcenterapp.navigation.Screen


/**
 * Bottom Bar üzerindeki her bir sekmeyi temsil eden veri modeli.
 */
data class AppTab(
    val route: String,
    val iconRes: Int,
    val contentDescription: String
)

/**
 * Dinamik olarak tab listesi oluşturmak için yardımcı fonksiyon.
 * Gereksiz kod tekrarını önler.
 */
private fun createTabs(homeRoute: String) = listOf(
    AppTab(route = homeRoute, iconRes = R.drawable.home_new, contentDescription = "Home"),
    AppTab(route = Screen.StudentCalendar.route, iconRes = R.drawable.solar_calendar_bold, contentDescription = "Calendar"),
    AppTab(route = Screen.Chat.route, iconRes = R.drawable.mynaui_message_solid, contentDescription = "Messages"),
    AppTab(route = Screen.StudentProfile.route, iconRes = R.drawable.outline_account_circle_24, contentDescription = "Profile")
)

// Mevcut NavHost yapını bozmamak için bu değişkenleri dışarıya açık bırakıyoruz
val studentBottomTabs = createTabs(homeRoute = "departments")
val staffBottomTabs = createTabs(homeRoute = "staff_home")

@Composable
fun AppBottomBar(
    tabs: List<AppTab>,
    currentRoute: String?,
    onTabSelected: (AppTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(BottomBarBackground)
            .padding(horizontal = 32.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        tabs.forEach { tab ->
            // Seçili olma kontrolü: Mevcut rota, tab rotasını içeriyor mu?
            // "staffDashboard/123" rotası "staff_home" tetiklendiğinde Home'un yeşil kalmasını sağlar.
            val isSelected = remember(currentRoute) {
                if (currentRoute == null) return@remember false

                // Eğer gelen rota tab rotasıyla tamamen aynıysa VEYA tab rotasını içeriyorsa
                currentRoute == tab.route || currentRoute.contains(tab.route, ignoreCase = true)
            }

            IconButton(onClick = { onTabSelected(tab) }) {
                Icon(
                    painter = painterResource(id = tab.iconRes),
                    contentDescription = tab.contentDescription,
                    // Seçiliyse Yeşil (PrimaryGreen), değilse Beyaz
                    tint = if (isSelected) PrimaryGreen else Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}