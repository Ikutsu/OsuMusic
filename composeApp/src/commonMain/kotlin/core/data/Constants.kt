package io.ikutsu.osumusic.core.data

object Constants {
    object Setting {
        const val BEATMAP_SOURCE = "beatmapSource"
    }
}

enum class BeatmapSource(val value: String) {
    SAYOBOT("Sayobot")
}