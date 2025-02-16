package io.ikutsu.osumusic.setting.presentation

import io.ikutsu.osumusic.core.data.BeatmapSource

data class SettingUiState(
    val beatmapSource: BeatmapSource? = null,
    val beatmapSourceOptions: List<BeatmapSource> = BeatmapSource.entries,
    val showInOriginalLang: Boolean = false
)
