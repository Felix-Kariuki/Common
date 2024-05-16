package com.flexcode.toasty

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

enum class ToastType {
    SUCCESS,
    ERROR,
    DEFAULT
}

@Composable
fun getColorForMessageType(toastType: ToastType): Color {
    return when (toastType) {
        ToastType.SUCCESS -> Color(0xFF5DBE55)
        ToastType.ERROR -> Color(0xFFF44336)
        ToastType.DEFAULT -> MaterialTheme.colorScheme.primary
    }
}