package io.ikutsu.osumusic.core.presentation.util

import androidx.compose.ui.graphics.Brush
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

enum class LevelTier {
    Iron,
    Bronze,
    Silver,
    Gold,
    Platinum,
    Rhodium,
    Radiant,
    Lustrous
}

fun LevelTier.forColour(): Brush {
    return when (this) {
        LevelTier.Iron -> Brush.verticalGradient(listOf(Color(0xFFBAB3AB)))
        LevelTier.Bronze -> Brush.verticalGradient(listOf(Color(0xFFB88F7A), Color(0xFF855C47)))
        LevelTier.Silver -> Brush.verticalGradient(listOf(Color(0xFFE0E0EB), Color(0xFFA3A3C2)))
        LevelTier.Gold -> Brush.verticalGradient(listOf(Color(0xFFF0E4A8), Color(0xFFE0C952)))
        LevelTier.Platinum -> Brush.verticalGradient(listOf(Color(0xFFA8F0EF), Color(0xFF52E0DF)))
        LevelTier.Rhodium -> Brush.verticalGradient(listOf(Color(0xFFD9F8D3), Color(0xFFA0CF96)))
        LevelTier.Radiant -> Brush.verticalGradient(listOf(Color(0xFF97DCFF), Color(0xFFED82FF)))
        LevelTier.Lustrous -> Brush.verticalGradient(listOf(Color(0xFFFFE600), Color(0xFFED82FF)))
    }
}

fun mapLevelToTierColour(level: Int): Brush {
    var tier = LevelTier.Iron

    if (level > 0) {
        tier = LevelTier.entries[level / 20]
    }

    if (level >= 105) {
        tier = LevelTier.Radiant
    }

    if (level >= 110) {
        tier = LevelTier.Lustrous
    }

    return tier.forColour()
}