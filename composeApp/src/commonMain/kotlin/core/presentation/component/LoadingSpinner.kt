package io.ikutsu.osumusic.core.presentation.component

import androidx.compose.animation.core.EaseInOutQuad
import androidx.compose.animation.core.EaseInOutQuart
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import core.presentation.res.OMIcon
import core.presentation.res.omicon.CircleSpinner

@Composable
fun LoadingSpinner(
    size: Dp = 96.dp
) {
    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = 45f,
        targetValue = 405f,
        animationSpec = InfiniteRepeatableSpec(
            animation = keyframes {
                45f at 0 using EaseInOutQuad
                225f at 900 using EaseInOutQuad
                405f at 1800 using EaseInOutQuad
                durationMillis = 1800
            }
        )
    )

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = InfiniteRepeatableSpec(
            animation = keyframes {
                0f at 0 using EaseInOutQuart
                90f at 900 using EaseInOutQuart
                180f at 1800 using EaseInOutQuart
                270f at 2700 using EaseInOutQuart
                360f at 3600 using EaseInOutQuart
                durationMillis = 3600
            }
        )
    )

    Box(
        modifier = Modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .rotate(rotation)
                .clip(RoundedCornerShape(size.div(4)))
                .background(Color.Black.copy(0.7f))
                .fillMaxSize()
        )
        Icon(
            imageVector = OMIcon.CircleSpinner,
            contentDescription = null,
            modifier = Modifier
                .rotate(angle)
                .size(size.times(0.7f))
        )
    }
}

@Composable
fun NoBackgroundLoadingSpinner(
    size: Dp = 96.dp
) {
    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = 45f,
        targetValue = 405f,
        animationSpec = InfiniteRepeatableSpec(
            animation = keyframes {
                45f at 0 using EaseInOutQuad
                225f at 900 using EaseInOutQuad
                405f at 1800 using EaseInOutQuad
                durationMillis = 1800
            }
        )
    )

    Icon(
        imageVector = OMIcon.CircleSpinner,
        contentDescription = null,
        modifier = Modifier
            .rotate(angle)
            .size(size)
    )
}