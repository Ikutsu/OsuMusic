package io.ikutsu.osumusic.core.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseOutQuint
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import core.presentation.res.OMIcon
import core.presentation.res.omicon.AngelDown
import io.ikutsu.osumusic.core.presentation.theme.OM_Background
import io.ikutsu.osumusic.core.presentation.theme.OM_Primary
import io.ikutsu.osumusic.core.presentation.theme.OM_ShapeMedium
import io.ikutsu.osumusic.core.presentation.util.OM_regular
import io.ikutsu.osumusic.core.presentation.util.VSpacer
import io.ikutsu.osumusic.core.presentation.util.WSpacer
import io.ikutsu.osumusic.core.presentation.util.noRippleClickable
import io.ikutsu.osumusic.core.presentation.util.sp

@Composable
fun <T> OMDropDownMenu(
    modifier: Modifier = Modifier,
    selectedItem: Int,
    items: List<T>,
    onItemSelected: (Int) -> Unit
) {
    val expended = remember { MutableTransitionState(false) }

    val rotation by animateFloatAsState(
        targetValue = if (expended.targetState) 180f else 0f
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .clip(OM_ShapeMedium)
                .noRippleClickable {
                    expended.targetState = !expended.currentState
                }
                .background(OM_Primary)
                .padding(horizontal = 12.dp)
                .fillMaxWidth()
                .height(48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = items[selectedItem].toString(),
                fontFamily = OM_regular,
                fontSize = 16.dp.sp
            )
            WSpacer()
            Icon(
                imageVector = OMIcon.AngelDown,
                contentDescription = "Expand/Collapse",
                tint = if (expended.targetState) Color(0xFFFFCC22) else Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .graphicsLayer {
                        rotationX = rotation
                    }
            )
        }
        VSpacer(4.dp)
        AnimatedVisibility(
            expended,
            enter = expandVertically(tween(durationMillis = 300, easing = EaseOutQuint)) + fadeIn(tween(durationMillis = 300, easing = EaseOutQuint)),
            exit = shrinkVertically(tween(durationMillis = 300, easing = EaseOutQuint)) + fadeOut(tween(durationMillis = 300, easing = EaseOutQuint))
        ) {
            Column(
                modifier = Modifier
                    .clip(OM_ShapeMedium)
                    .background(OM_Primary)
                    .padding(4.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items.forEachIndexed { index, item ->
                    Column(
                        modifier = Modifier
                            .clip(OM_ShapeMedium)
                            .clickable {
                                expended.targetState = false
                                onItemSelected(index)
                            }
                            .background(
                                if (index == selectedItem) OM_Background else Color.Transparent
                            )
                            .padding(horizontal = 8.dp)
                            .fillMaxWidth()
                            .height(36.dp)
                            ,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = item.toString(),
                            fontFamily = OM_regular,
                            fontSize = 14.dp.sp,
                        )
                    }
                }
            }
        }
    }
}