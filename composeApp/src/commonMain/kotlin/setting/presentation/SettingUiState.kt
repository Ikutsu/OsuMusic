package io.ikutsu.osumusic.setting.presentation

data class SettingUiState(
    val beatmapSource: String = "",
    val beatmapSourceOptions: List<String> = emptyList(),
    val showInOriginalLang: Boolean = false
)
