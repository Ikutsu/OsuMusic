package io.ikutsu.osumusic.setting.presentation

sealed interface SettingAction {
    data object OnBackClick : SettingAction
    data class SetBeatmapSource(val index: Int) : SettingAction
    data class SetShowInOriginal(val boolean: Boolean) : SettingAction
}