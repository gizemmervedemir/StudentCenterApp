package com.example.studentcenterapp.ui.theme


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val LightColorScheme = lightColorScheme(

    primary = PrimaryBlue,
    onPrimary = Color.White,

    secondary = PrimaryGreen,
    onSecondary = Color.White,

    error = ErrorRed,
    onError = Color.White,

    background = BackgroundLight, // genelde sayfa zemini → mavi
    onBackground = DarkText,

    surface = SurfaceLight,
    onSurface = DarkText,

    tertiary = PsychologicalCounseling,
    onTertiary = Color.White,

    outline = lightText,

    surfaceVariant = BottomBarBackground,
    onSurfaceVariant = Color.White,

    primaryContainer = AppointmentColor,
    onPrimaryContainer = Color.White,

    secondaryContainer = AppointmentOkColor,
    onSecondaryContainer = Color.White,

    tertiaryContainer = AdminAppointmentColor,
    onTertiaryContainer = Color.White,
)

@Composable
fun StudentCenterTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = StudentCenterTypography,
        shapes = Shapes,
        content = content
    )
}