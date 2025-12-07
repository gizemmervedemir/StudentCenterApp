package com.example.studentcenterapp.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.studentcenterapp.R
import com.example.studentcenterapp.ui.common.PrimaryButton
import com.example.studentcenterapp.ui.theme.PrimaryBlue
import androidx.compose.foundation.Image
import com.example.studentcenterapp.ui.common.ButtonVariant

@Composable
fun WelcomeScreen(
    onStudentClick: () -> Unit = {},
    onStaffClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBlue),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(230.dp)) // TOP → LOGO

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App logo",
                modifier = Modifier
                    .width(150.dp)
                    .height(150.dp)
            )

            Spacer(modifier = Modifier.height(142.dp)) // LOGO → HOŞ GELDİNİZ

            Text(
                text = "Hoş Geldiniz",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(modifier = Modifier.height(20.dp)) // HOŞ GELDİNİZ → ÖĞRENCİ



            PrimaryButton(
                text = "Öğrenci",
                onClick = onStudentClick,
                modifier = Modifier
                    .width(113.dp)
                    .height(47.dp),
                variant = ButtonVariant.Inverted
            )

            Spacer(modifier = Modifier.height(21.dp)) // ÖĞRENCİ → STAFF

            PrimaryButton(
                text = "Staff",
                onClick = onStaffClick,
                modifier = Modifier
                    .width(113.dp)
                    .height(47.dp),
                variant = ButtonVariant.Inverted
            )

            Spacer(modifier = Modifier.height(168.dp)) // STAFF → ALT BOŞLUK
        }
    }
}