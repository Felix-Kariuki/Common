package com.flexcode.toasty

import android.util.DisplayMetrics
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
 * Custom Business triply toasty
 * @param modifier
 * @param toastType of type [ToastType] by default is [ToastType.SUCCESS]
 * @param messageColor the color of the message
 * @param message actual message to be shown
 * @param messageIcon icon on the toast
 * @param width
 * @param height
 * @param toastAlignment  controls  where toast will be shown default[Alignment.TopCenter]
 * @param showIcon controls whether the icon is visible
 *
 */
@Composable
fun TopToast(
    modifier: Modifier = Modifier,
    toastType: ToastType = ToastType.SUCCESS,
    message: String = "Success", messageColor: Color = MaterialTheme.colorScheme.onBackground,
    height: Dp = 60.dp, messageIcon: ImageVector = Icons.Default.Done,
    showIcon: Boolean = true,
    width: Dp? = null, toastAlignment: Alignment = Alignment.TopCenter,
    // onDismissCallback: @Composable () -> Unit = {},
    onDismissCallback: () -> Unit = {},
) {
    var transitionStarted by remember { mutableStateOf(false) }
    var clipShape by remember { mutableStateOf(CircleShape) }
    var slideDownAnimation by remember { mutableStateOf(true) }
    var animationStarted by remember { mutableStateOf(false) }
    var showMessage by remember { mutableStateOf(false) }
    var dismissCallback by remember { mutableStateOf(false) }

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
            delay(2000)
            transitionStarted = false
            showMessage = false


            // slide down
            delay(150)
            clipShape = CircleShape
            slideDownAnimation = true
            animationStarted = true
            dismissCallback = true


            //}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(top = 38.dp, start = 16.dp, end = 16.dp),
    ) {
        Box(
            modifier = modifier
                .size(boxWidth, boxHeight)
                .offset(y = slideY)
                .clip(clipShape)
                .background(getColorForMessageType(toastType))
                .align(alignment = toastAlignment),
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
//                    ResultTextComposable(
//                        text = message, textColor = messageColor,
//                        fontSize = 15.sp,
//                        style = MaterialTheme.typography.bodyLarge,
//                        textAlign = TextAlign.Center,
//                        modifier = modifier.padding(
//                            MaterialTheme.spacing.extraSmall,
//                        ),
//                    )
                }
            }

            if (dismissCallback) onDismissCallback()
        }
    }
}

@Composable
fun BottomToast(
    modifier: Modifier = Modifier,
    toastType: ToastType = ToastType.ERROR,
    messageIcon: ImageVector = Icons.Default.Error,
    message: String = "OOps An Error Occurred try again later",
    messageColor: Color = MaterialTheme.colorScheme.onPrimary,
    height: Dp = 60.dp,
    width: Dp? = null,
    onDismissCallback: @Composable () -> Unit = {},
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

    if (!animationStarted) {
        LaunchedEffect(Unit) {
            slideAnimation = false

            //  transitioning to rectangle
            delay(330)
            transitionStarted = true
            clipShape = RoundedCornerShape(12.dp, 12.dp, 12.dp, 12.dp)
            showMessage = true

            //transitioning back to circle
            delay(2000)
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(16.dp),
    ) {
        Box(
            modifier = modifier
                .size(boxWidth, boxHeight)
                .offset(y = slideY)
                .clip(clipShape)
                .background(getColorForMessageType(toastType))
                .align(alignment = Alignment.BottomCenter),
            contentAlignment = Alignment.Center,
        ) {
            if (showMessage) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Spacer(modifier = modifier.padding(start = 8.dp))
                    Icon(
                        imageVector = messageIcon, contentDescription = message,
                        tint = messageColor, modifier = modifier.size(20.dp),
                    )


                    Text(
                        text = message,
                        color = messageColor,
                        fontSize = 15.sp,
                        modifier = modifier.padding(4.dp)
                    )

//                    ResultTextComposable(
//                        text = message, textColor = messageColor,
//                        fontSize = 15.sp,
//                        style = MaterialTheme.typography.bodyLarge,
//                        textAlign = TextAlign.Center,
//                        modifier = modifier.padding(
//                           4.dp
//                        ),
//                    )
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

        TopToast(

            modifier, ToastType.ERROR,
            "Uploaded successfully continue  ",
            onDismissCallback = {

            },
        )


        BottomToast(modifier, ToastType.ERROR)

    }
}