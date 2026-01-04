package com.example.studentcenterapp.ui.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.studentcenterapp.ui.theme.PrimaryBlue
import com.example.studentcenterapp.ui.theme.ButtonBackground

enum class ButtonVariant {
    ButtonBackground,      // normal mavi zemin - beyaz yazı
    Inverted      // welcome’daki gibi beyaz zemin - mavi yazı
}

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    variant: ButtonVariant = ButtonVariant.ButtonBackground,
    containerColor: Color? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
) {
    val colors = if (containerColor != null) {
        // Eğer dışarıdan renk gelmişse (Yeşil gibi), onu kullan
        ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = Color.White // Yeşil butonun yazısı beyaz olsun
        )
    } else {
        // Yoksa mevcut variant mantığın aynen çalışsın
        when (variant) {
            ButtonVariant.ButtonBackground
                 -> ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.surface
            )
            ButtonVariant.Inverted -> ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            )
        }
    }

    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        shape = MaterialTheme.shapes.small,
        contentPadding = contentPadding
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}