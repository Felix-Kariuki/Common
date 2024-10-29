package com.flexcode.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.flexcode.common.ui.theme.CommonTheme
import com.flexcode.toasty.BottomToast
import com.flexcode.toasty.ToastType
import com.flexcode.toasty.TopToast

@Composable
fun SampleToasty(modifier: Modifier = Modifier) {
    Box {

        TopToast(
            modifier, ToastType.SUCCESS,
            "Uploaded successfully continue  ",
            onDismissCallback = {

            }
        )


        BottomToast(modifier, ToastType.ERROR)

    }
}

@Preview
@Composable
fun SampleToastyPreview(modifier: Modifier = Modifier) {
    CommonTheme {
        SampleToasty()
    }
}