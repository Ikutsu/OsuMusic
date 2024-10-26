package io.ikutsu.osumusic.core.presentation.component

import androidx.compose.animation.core.EaseInOutQuint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.ikutsu.osumusic.core.presentation.theme.OM_Background
import io.ikutsu.osumusic.core.presentation.theme.OM_ShapeFull

@Composable
fun OMSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val containerWidth by animateDpAsState(
        if (checked) 61.dp else 45.dp,
        animationSpec = tween(durationMillis = 150, easing = EaseInOutQuint)
    )
    val circleHeight by animateDpAsState(
        if (checked) 0.dp else 11.dp,
        animationSpec = tween(durationMillis = 150, easing = EaseInOutQuint)
    )
    val circleWidth by animateDpAsState(
        if (checked) 0.dp else 38.dp,
        animationSpec = tween(durationMillis = 150, easing = EaseInOutQuint)
    )
    Box(
        modifier = Modifier
            .requiredWidth(54.dp)
            .requiredHeight(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .clip(OM_ShapeFull)
                .clickable { onCheckedChange(!checked) }
                .background(Color(0xFFFFCC22))
                .width(containerWidth)
                .height(18.dp)
        )
        Box(
            modifier = Modifier
                .clip(OM_ShapeFull)
                .background(OM_Background)
                .width(circleWidth)
                .height(circleHeight)
        )
    }
}