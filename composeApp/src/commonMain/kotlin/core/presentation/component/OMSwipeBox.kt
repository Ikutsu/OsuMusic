package io.ikutsu.osumusic.core.presentation.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun OMSwipeBox(
    allowSwipeLeft: Boolean = true,
    allowSwipeRight: Boolean = true,
    maxOffset: Float = 0.25f,
    triggerThreshold: Float = 0.2f,
    onSwipeRelease: () -> Unit = {},
    backgroundContent: @Composable RowScope.() -> Unit,
    upperContent: @Composable RowScope.() -> Unit
) {
    require(maxOffset in 0f..1f) { "maxOffset must be in range [0, 1]" }
    require(triggerThreshold in 0f..1f) { "triggerThreshold must be in range [0, 1]" }
    require(maxOffset >= triggerThreshold) { "maxOffset must be greater than or equal to triggerThreshold" }

    val offsetX = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    Box {
        Row(content = backgroundContent, modifier = Modifier.matchParentSize())
        Row(
            content = upperContent,
            modifier = Modifier
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            val thresholdOffset = triggerThreshold * size.width
                            val isTriggered =
                                offsetX.value > thresholdOffset || offsetX.value < -thresholdOffset
                            if (isTriggered) { onSwipeRelease() }
                            coroutineScope.launch {
                                offsetX.animateTo(0f, animationSpec = tween(300))
                            }
                        }
                    ) { _, dragAmount ->
                        val newOffset = (offsetX.value + dragAmount)
                            .coerceIn(
                                if (allowSwipeLeft) -maxOffset * size.width else 0f,
                                if (allowSwipeRight) maxOffset * size.width else 0f
                            )
                        coroutineScope.launch {
                            offsetX.snapTo(newOffset)
                        }
                    }
                }
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
        )
    }
}