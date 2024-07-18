package io.ikutsu.osumusic.core.presentation.util

fun Long.formatMilliseconds(): String {
    val totalSeconds = this / 1000
    val minutes = totalSeconds / 60
    val seconds = (totalSeconds % 60)
    val secondsStr = if (seconds < 10) "0$seconds" else seconds
    return "$minutes:$secondsStr"
}