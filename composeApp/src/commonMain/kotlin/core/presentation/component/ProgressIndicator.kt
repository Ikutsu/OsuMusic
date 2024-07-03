package io.ikutsu.osumusic.core.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import kotlin.math.abs

@Composable
fun ProgressIndicator(
    progress: () -> Float,
    height: Dp,
    trackColor: Color,
    indicatorColor: Color,
    strokeCap: StrokeCap,
    modifier: Modifier = Modifier,
) {
    val coercedProgress = { progress().coerceIn(0f, 1f) }
    Canvas(
        modifier = modifier.fillMaxWidth().height(height),
    ) {
        val strokeWidth = size.height
        drawIndicator(
            color = trackColor,
            startAt = 0f,
            endAt = 1f,
            strokeWidth = strokeWidth,
            strokeCap = strokeCap
        )
        drawIndicator(
            color = indicatorColor,
            startAt = 0f,
            endAt = coercedProgress(),
            strokeWidth = strokeWidth,
            strokeCap = strokeCap
        )
    }
}

private fun DrawScope.drawIndicator(
    color: Color,
    startAt: Float,
    endAt: Float,
    strokeWidth: Float,
    strokeCap: StrokeCap
) {
    val width = size.width
    val height = size.height

    val yOffset = height / 2

    val lineStart = width * startAt
    val lineEnd = width * endAt

    if (strokeCap == StrokeCap.Butt) {
        drawLine(color, Offset(lineStart, yOffset), Offset(lineEnd, yOffset), strokeWidth)
    } else {
        val strokeCapOffset = strokeWidth / 2
        val coerceRange = strokeCapOffset..(width - strokeCapOffset)
        val adjustedBarStart = lineStart.coerceIn(coerceRange)
        val adjustedBarEnd = lineEnd.coerceIn(coerceRange)

        if (abs(endAt - startAt) > 0) {
            // Progress line
            drawLine(
                color,
                Offset(adjustedBarStart, yOffset),
                Offset(adjustedBarEnd, yOffset),
                strokeWidth,
                strokeCap,
            )
        }
    }
}