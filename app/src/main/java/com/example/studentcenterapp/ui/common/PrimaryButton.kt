package com.example.studentcenterapp.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.studentcenterapp.ui.theme.PrimaryBlue

enum class ButtonVariant {
    Primary,      // normal mavi zemin - beyaz yazı
    Inverted      // welcome’daki gibi beyaz zemin - mavi yazı
}

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    variant: ButtonVariant = ButtonVariant.Primary
) {
    val colors = when (variant) {
        ButtonVariant.Primary -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor   = MaterialTheme.colorScheme.surface
        )
        ButtonVariant.Inverted -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface,  // beyaz
            contentColor   = MaterialTheme.colorScheme.primary     // mavi
        )
    }

    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}