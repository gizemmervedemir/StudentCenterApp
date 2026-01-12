package com.example.studentcenterapp.viewmodel.department

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studentcenterapp.model.Department
import com.example.studentcenterapp.ui.common.AppBottomBar
import com.example.studentcenterapp.ui.common.AppTab
import com.example.studentcenterapp.ui.common.ContentCard
import com.example.studentcenterapp.ui.common.AppTopBar
import com.example.studentcenterapp.ui.common.bottomTabs
import com.example.studentcenterapp.ui.theme.PrimaryBlue
import com.example.studentcenterapp.ui.state.UiState
import com.example.studentcenterapp.ui.theme.DarkText
import com.example.studentcenterapp.ui.theme.ErrorRed
import com.example.studentcenterapp.ui.theme.Figtree
import com.example.studentcenterapp.ui.theme.GreyBox
import com.example.studentcenterapp.ui.theme.lightText

@Composable
fun DepartmentListScreen(
    state: UiState<List<Department>>,
    currentRoute: String?,
    userName: String,
    onTabSelected: (AppTab) -> Unit,
    onDepartmentClick: (departmentId: String, departmentName: String) -> Unit

) {
    Scaffold(
        topBar = { AppTopBar(title = "Departmanlar") },
        bottomBar = {
            AppBottomBar(
                tabs = bottomTabs,
                currentRoute = currentRoute,
                onTabSelected = onTabSelected
            )
        },
        containerColor = PrimaryBlue // Arka plan mavisi
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ContentCard(modifier = Modifier.fillMaxSize()) {
                DepartmentListContent(
                    state = state,
                    userName= userName,
                    onDepartmentClick = onDepartmentClick
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DepartmentListContent(
    state: UiState<List<Department>>,
    userName: String,
    onDepartmentClick:(departmentId: String, departmentName: String) -> Unit
) {
    // 8 birimlik liste (state.data içinden gelecek)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 21.dp) // Merhaba Deniz solundaki 21
    ) {
        Spacer(modifier = Modifier.height(26.dp)) // Tepeden Merhaba Deniz'e kadar 26

        Text(
            text = "Merhaba, $userName.",
            style = TextStyle(
                fontFamily = Figtree,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            ),
            color = DarkText
        )

        Spacer(modifier = Modifier.height(23.dp)) // Merhaba Deniz altından arama çubuğuna kadar 23

        // Birimleri Ara (Arama Çubuğu)
        OutlinedTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth() // Yatayda kartı ortalar
                .height(59.dp), // Dikey uzunluğu 59 (Figma'dan)
            placeholder = {
                Text(
                    "Birimleri ara",
                    style = TextStyle(fontFamily = Figtree, fontWeight = FontWeight.Medium, fontSize = 14.sp),
                    color = lightText
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    modifier = Modifier.size(19.dp), // Butonun dikey uzunluğu 19
                    tint = lightText
                )
            },
            shape = RoundedCornerShape(25.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF2F2F2),
                unfocusedContainerColor = Color(0xFFF2F2F2),
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = PrimaryBlue
            )
        )

        Spacer(modifier = Modifier.height(10.dp)) // Birimleri ara altından kart başlangıcına kadar 10

        // Birimlerin Olduğu Kart (Kaydırmalı Bölüm)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp) // Sabit yükseklik (6 tanesi sığar, kalan 2'si scroll olur)
                .background(GreyBox, RoundedCornerShape(25.dp))
                .padding(8.dp)
        ) {
            when (state) {
                is UiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                is UiState.Success -> {
                    // LazyColumn otomatik olarak kaydırma sağlar
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(state.data, key = { it.id }) { dep ->
                            DepartmentRow(
                                title = dep.name,
                                isSelected = dep.name.contains("Psikolojik"),
                                onClick = {onDepartmentClick(dep.id, dep.name)}
                            )
                        }
                    }
                }
                is UiState.Error -> Text(state.message, color = ErrorRed)
            }
        }

        Spacer(modifier = Modifier.height(17.dp)) // Kart altından yaklaşan randevulara kadar 17
        // Yaklaşan Randevular Kısmı (Placeholder)
        //Text("Yaklaşan Randevular", fontWeight = FontWeight.Bold)
        // Buraya gerekirse ayrı bir Composable eklenebilir.
    }
}

@Composable
private fun DepartmentRow(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    // Figma'daki spesifik mavi tonu: #1CA5D6
    val selectedBlue = Color(0xFF1CA5D6)
    val contentColor = if (isSelected) selectedBlue else DarkText

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Sol taraftaki dikey çubuk
        Box(
            modifier = Modifier
                .width(6.dp)
                .height(26.dp)
                .background(contentColor, RoundedCornerShape(10.dp))
        )

        Text(
            text = title,
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f),
            // Figma: Bold 15.5sp
            style = TextStyle(
                fontFamily = Figtree,
                fontWeight = FontWeight.Bold,
                fontSize = 15.5.sp
            ),
            color = contentColor
        )

        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(24.dp)
        )
    }
}