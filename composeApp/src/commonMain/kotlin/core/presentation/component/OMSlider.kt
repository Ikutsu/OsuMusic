package io.ikutsu.osumusic.core.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.MutatorMutex
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.DragScope
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import androidx.compose.ui.util.fastFirst
import androidx.compose.ui.util.lerp
import io.ikutsu.osumusic.core.presentation.theme.OM_ShapeFull
import kotlinx.coroutines.coroutineScope
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

//Modified code from material3 1.2.1

private enum class SliderID {
    Track,
    Thumb
}

@Composable
fun OMSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    onValueChangeFinished: (() -> Unit)? = null,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    inactiveTrackColor: Color = Color(0xFF673401),
    activeTrackColor: Color = Color(0xFFFFCC22),
    thumbSize: Dp = 8.dp,
    trackHeight: Dp = 4.dp,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    modifier: Modifier = Modifier
) {
    val onValueChangeFinishedState = rememberUpdatedState(onValueChangeFinished)
    val state = remember(
        valueRange
    ) {
        SliderState(
            value,
            { onValueChangeFinishedState.value?.invoke() },
            valueRange
        )
    }

    state.onValueChange = onValueChange
    state.onValueChangeFinished = onValueChangeFinished
    state.value = value

    SliderLayout(
        state = state,
        thumbSize = thumbSize,
        trackHeight = trackHeight,
        inactiveTrackColor = inactiveTrackColor,
        activeTrackColor = activeTrackColor,
        thumbColor = activeTrackColor,
        interactionSource = interactionSource,
        modifier = modifier
    )
}

@Composable
private fun SliderLayout(
    state: SliderState,
    thumbSize: Dp,
    trackHeight: Dp,
    inactiveTrackColor: Color,
    activeTrackColor: Color,
    thumbColor: Color = activeTrackColor,
    interactionSource: MutableInteractionSource,
    modifier: Modifier = Modifier
) {

    val press = Modifier.sliderTapModifier(
        state,
        interactionSource
    )
    val drag = Modifier.draggable(
        orientation = Orientation.Horizontal,
        interactionSource = interactionSource,
        onDragStopped = { state.gestureEndAction() },
        startDragImmediately = state.isDragging,
        state = state
    )

    Layout(
        content = {
            Box(
                modifier = Modifier
                    .layoutId(SliderID.Thumb)
            ) {
                Thumb(
                    thumbSize = thumbSize,
                    color = thumbColor
                )
            }

            Box(
                modifier = Modifier
                    .layoutId(SliderID.Track)
            ) {
                Track(
                    state = state,
                    inactiveTrackColor = inactiveTrackColor,
                    activeTrackColor = activeTrackColor,
                    trackHeight = trackHeight
                )
            }
        },
        modifier = modifier
            .requiredSizeIn(minWidth = thumbSize, minHeight = trackHeight)
            .then(press)
            .then(drag)
    ) { measurable, constraints ->
        val thumbPlaceable = measurable.fastFirst {
            it.layoutId == SliderID.Thumb
        }.measure(constraints)

        val trackPlaceable = measurable.fastFirst {
            it.layoutId == SliderID.Track
        }.measure(
            constraints.offset(
                horizontal = - thumbPlaceable.width
            )
        )

        val sliderWidth = thumbPlaceable.width + trackPlaceable.width
        val sliderHeight = max(thumbPlaceable.height, trackPlaceable.height)

        state.updateDimensions(
            thumbPlaceable.width.toFloat(),
            sliderWidth
        )

        val trackOffsetX = thumbPlaceable.width / 2
        val thumbOffsetX = ((trackPlaceable.width) * state.coercedValueAsFraction).roundToInt()
        val thumbOffsetY = (sliderHeight - thumbPlaceable.height) / 2
        val trackOffsetY = (sliderHeight - trackPlaceable.height) / 2

        layout(sliderWidth, sliderHeight) {
            trackPlaceable.placeRelative(
                x = trackOffsetX,
                y = trackOffsetY
            )
            thumbPlaceable.placeRelative(
                x = thumbOffsetX,
                y = thumbOffsetY
            )
        }
    }
}

@Composable
private fun Track(
    state: SliderState,
    inactiveTrackColor: Color,
    activeTrackColor: Color,
    trackHeight: Dp,
    modifier: Modifier = Modifier
) {
    val trackHeightPx = with(LocalDensity.current) {
        trackHeight.toPx()
    }

    println(trackHeightPx)
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(trackHeight)
    ) {
        val leftOffset = Offset(0f, center.y)
        val rightOffset = Offset(size.width, center.y)

        // Inactive track
        drawLine(
            color = inactiveTrackColor,
            start = leftOffset,
            end = rightOffset,
            strokeWidth = trackHeightPx,
            cap = StrokeCap.Round
        )

        // Active track
        drawLine(
            color = activeTrackColor,
            start = leftOffset,
            end = Offset(leftOffset.x + (rightOffset.x - leftOffset.x) * state.coercedValueAsFraction, center.y),
            strokeWidth = trackHeightPx,
            cap = StrokeCap.Round
        )
    }
}

@Composable
private fun Thumb(
    thumbSize: Dp,
    color: Color,
    modifier: Modifier = Modifier
) {
    Spacer(
        modifier = modifier
            .size(thumbSize)
            .background(color, OM_ShapeFull)
    )
}

class SliderState(
    value: Float = 0f,
    onValueChangeFinished: (() -> Unit)? = null,
    val valueRange: ClosedFloatingPointRange<Float> = 0f..1f
) : DraggableState {

    private var valueState by mutableFloatStateOf(value)

    /**
     * [Float] that indicates the current value that the thumb
     * currently is in respect to the track.
     */
    var value: Float
        set(newVal) {
            val coercedValue = newVal.coerceIn(valueRange.start, valueRange.endInclusive)
            val snappedValue = snapValueToTick(
                coercedValue,
                valueRange.start,
                valueRange.endInclusive
            )
            valueState = snappedValue
        }
        get() = valueState

    override suspend fun drag(
        dragPriority: MutatePriority,
        block: suspend DragScope.() -> Unit
    ): Unit = coroutineScope {
        isDragging = true
        scrollMutex.mutateWith(dragScope, dragPriority, block)
        isDragging = false
    }

    override fun dispatchRawDelta(delta: Float) {
        val maxPx = max(totalWidth - thumbWidth / 2, 0f)
        val minPx = min(thumbWidth / 2, maxPx)
        rawOffset = (rawOffset + delta + pressOffset)
        pressOffset = 0f
        val offsetInTrack = snapValueToTick(rawOffset, minPx, maxPx)
        val scaledUserValue = scaleToUserValue(minPx, maxPx, offsetInTrack)
        if (scaledUserValue != this.value) {
            if (onValueChange != null) {
                onValueChange?.let { it(scaledUserValue) }
            } else {
                this.value = scaledUserValue
            }
        }
    }

    /**
     * callback in which value should be updated
     */
    internal var onValueChange: ((Float) -> Unit)? = null

    var onValueChangeFinished: (() -> Unit)? = onValueChangeFinished
        internal set

    private var totalWidth by mutableIntStateOf(0)
    private var thumbWidth by mutableFloatStateOf(0f)

    internal val coercedValueAsFraction
        get() = calcFraction(
            valueRange.start,
            valueRange.endInclusive,
            value.coerceIn(valueRange.start, valueRange.endInclusive)
        )

    internal var isDragging by mutableStateOf(false)
        private set

    internal fun updateDimensions(
        newThumbWidth: Float,
        newTotalWidth: Int
    ) {
        thumbWidth = newThumbWidth
        totalWidth = newTotalWidth
    }

    internal val gestureEndAction = {
        if (!isDragging) {
            // check isDragging in case the change is still in progress (touch -> drag case)
            this.onValueChangeFinished?.invoke()
        }
    }

    internal fun onPress(pos: Offset) {
        pressOffset = pos.x - rawOffset
    }

    private var rawOffset by mutableFloatStateOf(scaleToOffset(0f, 0f, value))
    private var pressOffset by mutableFloatStateOf(0f)
    private val dragScope: DragScope = object : DragScope {
        override fun dragBy(pixels: Float): Unit = dispatchRawDelta(pixels)
    }

    private val scrollMutex = MutatorMutex()

    private fun scaleToUserValue(minPx: Float, maxPx: Float, offset: Float) =
        scale(minPx, maxPx, offset, valueRange.start, valueRange.endInclusive)

    private fun scaleToOffset(minPx: Float, maxPx: Float, userValue: Float) =
        scale(valueRange.start, valueRange.endInclusive, userValue, minPx, maxPx)
}

private fun snapValueToTick(
    current: Float,
    minPx: Float,
    maxPx: Float
): Float {
    // target is a closest anchor to the `current`, if exists
    return floatArrayOf()
        .minByOrNull { abs(lerp(minPx, maxPx, it) - current) }
        ?.run { lerp(minPx, maxPx, this) }
        ?: current
}

private fun scale(a1: Float, b1: Float, x1: Float, a2: Float, b2: Float) =
    lerp(a2, b2, calcFraction(a1, b1, x1))

private fun calcFraction(a: Float, b: Float, pos: Float) =
    (if (b - a == 0f) 0f else (pos - a) / (b - a)).coerceIn(0f, 1f)

private fun Modifier.sliderTapModifier(
    state: SliderState,
    interactionSource: MutableInteractionSource,
) = pointerInput(state, interactionSource) {
        detectTapGestures(
            onPress = { state.onPress(it) },
            onTap = {
                state.dispatchRawDelta(0f)
                state.gestureEndAction()
            }
        )
    }