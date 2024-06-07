package core.presentation.util

import androidx.compose.ui.graphics.Color

val difficultyColourSpectrum = listOf(
    0.1f to Color(0xFF4290FB),
    1.25f to Color(0xFF4FC0FF),
    2.0f to Color(0xFF4FFFD5),
    2.5f to Color(0xFF7CFF4F),
    3.3f to Color(0xFFF6F05C),
    4.2f to Color(0xFFFF8068),
    4.9f to Color(0xFFFF4E6F),
    5.8f to Color(0xFFC645B8),
    6.7f to Color(0xFF6563DE),
    7.7f to Color(0xFF18158E),
    9.0f to Color(0xFF000000)
).toMap()

fun Float.getDiffColor(): Color {
    val colorMap = difficultyColourSpectrum
    val keys = colorMap.keys.sorted()
    return when {
        this < keys.first() -> Color(0xFFAAAAAA)
        this > keys.last() -> Color(0xFF000000)
        else -> {
            val lowerBound = keys.findLast { it < this } ?: keys.first()
            val upperBound = keys.find { it > this } ?: keys.last()
            val lowerColor = colorMap[lowerBound]!!
            val upperColor = colorMap[upperBound]!!
            interpolateColor(lowerColor, upperColor, (this - lowerBound) / (upperBound - lowerBound))
        }
    }
}

fun interpolateColor(startColor: Color, endColor: Color, fraction: Float): Color {

    val r = (endColor.red - startColor.red) * fraction + startColor.red
    val g = (endColor.green - startColor.green) * fraction + startColor.green
    val b = (endColor.blue - startColor.blue) * fraction + startColor.blue

    return Color(r, g, b)
}

