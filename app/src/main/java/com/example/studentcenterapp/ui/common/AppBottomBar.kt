package com.example.studentcenterapp.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.studentcenterapp.ui.theme.BottomBarBackground
import com.example.studentcenterapp.ui.theme.PrimaryGreen
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.studentcenterapp.R
data class AppTab(
    val route: String,
    val iconRes: Int,
    val contentDescription: String
)
val bottomTabs = listOf(
    AppTab(
        route = "home",
        iconRes = R.drawable.material_symbols_home_rounded,
        contentDescription = "Home"
    ),
    AppTab(
        route = "calendar",
        iconRes = R.drawable.solar_calendar_bold,
        contentDescription = "Calendar"
    ),
    AppTab(
        route = "chat",
        iconRes = R.drawable.mynaui_message_solid,
        contentDescription = "Messages"
    ),
    AppTab(
        route = "profile",
        iconRes = R.drawable.qlementine_icons_user_16,
        contentDescription = "Profile"
    )
)
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
            val isSelected = tab.route == currentRoute

            IconButton(onClick = { onTabSelected(tab) }) {
                Icon(
                    painter = painterResource(id = tab.iconRes),
                    contentDescription = tab.contentDescription,
                    tint = Color.Unspecified
                )
            }
        }
    }
}