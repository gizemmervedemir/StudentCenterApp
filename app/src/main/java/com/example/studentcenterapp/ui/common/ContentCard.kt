package com.example.studentcenterapp.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.studentcenterapp.ui.theme.Shapes
import com.example.studentcenterapp.ui.theme.SurfaceLight

@Composable
fun ContentCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier,
        color = SurfaceLight,
        shape = Shapes.medium // top radius 25dp, bottom radius 0dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            content = content
        )
    }
}