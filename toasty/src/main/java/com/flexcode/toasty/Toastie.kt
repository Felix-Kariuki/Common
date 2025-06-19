package com.flexcode.toasty

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.DisplayMetrics
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/**
 * Custom toasty [Toastie]
 * @param modifier
 * @param toastType of type [ToastType] by default is [ToastType.SUCCESS]
 * @param messageColor the color of the message
 * @param message actual message to be shown
 * @param messageIcon icon on the toast
 * @param width
 * @param height
 * @param toastAlignment [ToastAlignment] aligns the toast to either: [ToastAlignment.TOP],
 * [ToastAlignment.BOTTOM]
 * @param showIcon controls whether the icon is visible or not
 * @param onDismissCallback Callback function to control what happens on dismiss the toast
 * @param toastPadding [PaddingValues] controls the padding of the Toast component
 * @param backgroundColor the back ground color of the toast component
 */
@Composable
fun Toastie(
    modifier: Modifier = Modifier,
    toastType: ToastType = ToastType.SUCCESS,
    message: String = "Success",
    messageColor: Color = MaterialTheme.colorScheme.onBackground,
    backgroundColor: Color = getColorForMessageType(toastType),
    height: Dp = 60.dp,
    messageIcon: ImageVector = Icons.Default.Done,
    showIcon: Boolean = false,
    width: Dp? = null,
    toastAlignment: ToastAlignment = ToastAlignment.TOP,
    toastPadding: PaddingValues = if (toastAlignment == ToastAlignment.TOP) {
        PaddingValues(top = 40.dp, start = 16.dp, end = 16.dp)
    } else {
        PaddingValues(start = 16.dp, end = 16.dp, bottom = 32.dp)
    },
    transition: Long = 2000,
    errorHaptic: Boolean = true,
    onDismissCallback: () -> Unit = {}
) {
    when (toastAlignment) {
        ToastAlignment.TOP -> {
            TopToast(
                modifier = modifier,
                toastType = toastType,
                message = message,
                messageColor = messageColor,
                backgroundColor = backgroundColor,
                height = height,
                messageIcon = messageIcon,
                showIcon = showIcon,
                width = width,
                toastPadding = toastPadding,
                transition = transition,
                errorHaptic = errorHaptic,
                onDismissCallback = onDismissCallback
            )
        }

        ToastAlignment.BOTTOM -> {
            BottomToast(
                modifier = modifier,
                toastType = toastType,
                messageIcon = messageIcon,
                showIcon = showIcon,
                message = message,
                messageColor = messageColor,
                backgroundColor = backgroundColor,
                height = height,
                toastPadding = toastPadding,
                width = width,
                onDismissCallback = onDismissCallback,
                transition = transition,
                errorHaptic = errorHaptic
            )
        }

    }
}

@Deprecated(
    message = "Top toast is now deprecated migrate it to Toastie",
    replaceWith = ReplaceWith("Toastie")
)
@Composable
fun TopToast(
    modifier: Modifier = Modifier,
    toastType: ToastType = ToastType.SUCCESS,
    message: String = "Success",
    messageColor: Color = MaterialTheme.colorScheme.onBackground,
    backgroundColor: Color = getColorForMessageType(toastType),
    height: Dp = 60.dp, messageIcon: ImageVector = Icons.Default.Done,
    showIcon: Boolean = false,
    width: Dp? = null, toastAlignment: Alignment = Alignment.TopCenter,
    toastPadding: PaddingValues = PaddingValues(top = 40.dp, start = 16.dp, end = 16.dp),
    transition: Long = 2000,
    errorHaptic: Boolean = true,
    onDismissCallback: () -> Unit = {},
) {
    var transitionStarted by remember { mutableStateOf(false) }
    var clipShape by remember { mutableStateOf(CircleShape) }
    var slideDownAnimation by remember { mutableStateOf(true) }
    var animationStarted by remember { mutableStateOf(false) }
    var showMessage by remember { mutableStateOf(false) }

    val displayMetrics: DisplayMetrics = LocalContext.current.resources.displayMetrics
    val screenWidthInDp = width ?: (displayMetrics.widthPixels / displayMetrics.density).dp

    val boxWidth by animateDpAsState(
        targetValue = if (transitionStarted) screenWidthInDp else 30.dp,
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
        label = "Box width",
    )

    val boxHeight by animateDpAsState(
        targetValue = if (transitionStarted) height else 30.dp,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "Box height",
    )

    val slideY by animateDpAsState(
        targetValue = if (slideDownAnimation) (-100).dp else 0.dp,
        animationSpec = tween(durationMillis = 200),
        label = "Slide parameter in DP",
    )

    val context = LocalContext.current

    if (!animationStarted) {
        LaunchedEffect(Unit) {
            // if (message.isNotEmpty()) {
            slideDownAnimation = false

            //  transitioning to rectangle
            delay(100)
            transitionStarted = true
            clipShape = RoundedCornerShape(12.dp, 12.dp, 12.dp, 12.dp)
            showMessage = true

            //transitioning back to circle
            delay(transition)
            transitionStarted = false
            showMessage = false


            // slide down
            delay(150)
            clipShape = CircleShape
            slideDownAnimation = true
            animationStarted = true

            onDismissCallback()
            //}
        }
    }

    LaunchedEffect(toastType) {
        if (toastType == ToastType.ERROR && errorHaptic) {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val pattern = longArrayOf(0, 50, 50, 100)
                val effect = VibrationEffect.createWaveform(pattern, -1)
                vibrator.vibrate(effect)
            } else {
                vibrator.vibrate(longArrayOf(0, 50, 50, 100), -1)
            }
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(toastPadding)
    ) {
        Box(
            modifier = modifier
                .size(boxWidth, boxHeight)
                .offset(y = slideY)
                .clip(clipShape)
                .background(backgroundColor)
                .align(alignment = Alignment.TopCenter),
            contentAlignment = Alignment.Center,
        ) {
            if (showMessage) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (showIcon) {
                        Spacer(modifier = modifier.padding(start = 8.dp))
                        Icon(
                            imageVector = if (toastType == ToastType.ERROR) {
                                Icons.Default.Error
                            } else {
                                messageIcon
                            },
                            contentDescription = message,
                            tint = messageColor, modifier = modifier.size(20.dp),
                        )
                    }

                    Spacer(modifier = modifier.padding(start = 16.dp))

                    Text(
                        text = message,
                        color = messageColor,
                        fontSize = 15.sp,
                        modifier = modifier.padding(4.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

        }
    }
}

@Deprecated(
    message = "Bottom toast is now deprecated migrate it to Toastie",
    replaceWith = ReplaceWith("Toastie")
)
@Composable
internal fun BottomToast(
    modifier: Modifier = Modifier,
    toastType: ToastType = ToastType.ERROR,
    messageIcon: ImageVector = Icons.Default.Error,
    showIcon: Boolean = true,
    backgroundColor: Color = getColorForMessageType(toastType),
    message: String = "OOps An Error Occurred try again later",
    messageColor: Color = MaterialTheme.colorScheme.onBackground,
    height: Dp = 60.dp,
    toastPadding: PaddingValues = PaddingValues(16.dp),
    width: Dp? = null,
    onDismissCallback: () -> Unit = {},
    transition: Long = 2000,
    errorHaptic: Boolean = true,
) {
    var transitionStarted by remember { mutableStateOf(false) }
    var clipShape by remember { mutableStateOf(CircleShape) }
    var slideAnimation by remember { mutableStateOf(true) }
    var animationStarted by remember { mutableStateOf(false) }
    var showMessage by remember { mutableStateOf(false) }
    var dismissCallback by remember { mutableStateOf(false) }

    val displayMetrics: DisplayMetrics = LocalContext.current.resources.displayMetrics
    val screenHeightInDp = (displayMetrics.heightPixels / displayMetrics.density).dp
    val screenWidthInDp = width ?: (displayMetrics.widthPixels / displayMetrics.density).dp

    val boxWidth by animateDpAsState(
        targetValue = if (transitionStarted) screenWidthInDp else 30.dp,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "Box width",
    )

    val boxHeight by animateDpAsState(
        targetValue = if (transitionStarted) height else 30.dp,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "Box Height",
    )

    val slideY by animateDpAsState(
        targetValue = if (slideAnimation) 100.dp else 0.dp,
        animationSpec = tween(durationMillis = 200),
        label = "Slide parameter in DP",
    )

    val context = LocalContext.current

    if (!animationStarted) {
        LaunchedEffect(Unit) {
            slideAnimation = false

            //  transitioning to rectangle
            delay(330)
            transitionStarted = true
            clipShape = RoundedCornerShape(12.dp, 12.dp, 12.dp, 12.dp)
            showMessage = true

            //transitioning back to circle
            delay(transition)
            transitionStarted = false
            showMessage = false

            // slide down
            delay(330)
            clipShape = CircleShape
            slideAnimation = true
            animationStarted = true
            dismissCallback = true
        }
    }

    LaunchedEffect(toastType) {
        if (toastType == ToastType.ERROR && errorHaptic) {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val pattern = longArrayOf(0, 50, 50, 100)
                val effect = VibrationEffect.createWaveform(pattern, -1)
                vibrator.vibrate(effect)
            } else {
                vibrator.vibrate(longArrayOf(0, 50, 50, 100), -1)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(toastPadding),
    ) {
        Box(
            modifier = modifier
                .size(boxWidth, boxHeight)
                .offset(y = slideY)
                .clip(clipShape)
                .background(backgroundColor)
                .align(alignment = Alignment.BottomCenter),
            contentAlignment = Alignment.Center,
        ) {
            if (showMessage) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (showIcon) {
                        Spacer(modifier = modifier.padding(start = 8.dp))
                        Icon(
                            imageVector = messageIcon, contentDescription = message,
                            tint = messageColor, modifier = modifier.size(20.dp),
                        )
                    }

                    Text(
                        text = message,
                        color = messageColor,
                        fontSize = 15.sp,
                        modifier = modifier.padding(4.dp)
                    )

                }

            }

            if (dismissCallback) onDismissCallback()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TopToastPreview(modifier: Modifier = Modifier) {
    Box {
        Toastie(
            modifier, ToastType.ERROR,
            "Uploaded successfully continue  ",
            onDismissCallback = {

            },
            toastAlignment = ToastAlignment.BOTTOM
        )

        BottomToast(modifier, ToastType.ERROR)
    }
}