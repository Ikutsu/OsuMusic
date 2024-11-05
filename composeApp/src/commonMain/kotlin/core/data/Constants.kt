package io.ikutsu.osumusic.core.data

object Constants {
    object Setting {
        const val BEATMAP_SOURCE = "beatmapSource"
        const val SHOW_IN_ORIGINAL_LANG = "showInOriginal"
    }
}

enum class BeatmapSource(val value: String) {
    SAYOBOT("Sayobot"),
    OSU_DIRECT("OsuDirect")
}