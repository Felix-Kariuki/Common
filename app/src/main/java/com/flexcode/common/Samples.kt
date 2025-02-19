package com.flexcode.common

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.flexcode.common.ui.theme.CommonTheme
import com.flexcode.toasty.ToastAlignment
import com.flexcode.toasty.ToastType
import com.flexcode.toasty.Toastie
import com.flexcode.toasty.TopToast

@Composable
fun SampleToasty(modifier: Modifier = Modifier) {
    Box {
        Toastie(
            modifier, ToastType.SUCCESS,
            "Uploaded successfully continue  ",
            onDismissCallback = {

            },
            toastAlignment = ToastAlignment.TOP
        )

        Toastie(
            modifier, ToastType.ERROR,
            "Error",
            onDismissCallback = {

            },
            toastAlignment = ToastAlignment.BOTTOM,
            transition = 1000
        )

    }
}

@Preview
@Composable
fun SampleToastyPreview(modifier: Modifier = Modifier) {
    CommonTheme {
        SampleToasty()
    }
}