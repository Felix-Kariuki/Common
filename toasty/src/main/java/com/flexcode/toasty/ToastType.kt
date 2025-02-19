package com.flexcode.toasty

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.flexcode.toasty.ToastAlignment.BOTTOM
import com.flexcode.toasty.ToastAlignment.TOP
import com.flexcode.toasty.ToastType.ERROR
import com.flexcode.toasty.ToastType.SUCCESS

/**
 * Enum to control the toast types [SUCCESS],[ERROR], Default is Success state
 */
enum class ToastType {
    SUCCESS,
    ERROR,
    //  DEFAULT
}

/**
 * Toast alignment [TOP],[BOTTOM], BY DEFAULT the [ToastAlignment] is [ToastAlignment.TOP]
 */
enum class ToastAlignment {
    TOP, BOTTOM
}

@Composable
internal fun getColorForMessageType(toastType: ToastType): Color {
    return when (toastType) {
        SUCCESS -> Color(0xFF5DBE55)
        ERROR -> Color(0xFFF44336)
        // DEFAULT -> MaterialTheme.colorScheme.primary
    }
}