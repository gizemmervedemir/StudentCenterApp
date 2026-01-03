package com.example.studentcenterapp.ui.common

data class EmptyStateConfig(
    val title: String,
    val message: String,
    val actionText: String? = null,
    val onAction: (() -> Unit)? = null
)
