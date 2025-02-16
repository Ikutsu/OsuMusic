package io.ikutsu.osumusic.setting.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ikutsu.osumusic.core.data.BeatmapSource
import io.ikutsu.osumusic.setting.data.AppearanceSettings
import io.ikutsu.osumusic.setting.data.SearchSettings
import io.ikutsu.osumusic.setting.data.SettingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingViewModel(
    private val settingRepository: SettingRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(SettingUiState())
    val uiState: StateFlow<SettingUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    private fun load() {
        viewModelScope.launch(Dispatchers.IO) {
            settingRepository.getAllSettings().collect { settings ->
                _uiState.update {
                    it.copy(
                        beatmapSource = BeatmapSource.valueOf(settings.searchSettings.beatmapSource),
                        showInOriginalLang = settings.appearanceSettings.showInOriginalLang
                    )
                }
            }
        }
    }

    fun onAction(action: SettingAction) {
        when (action) {
            is SettingAction.SetBeatmapSource -> setBeatmapSource(action.index)
            is SettingAction.SetShowInOriginal -> setShowInOriginal(action.boolean)
            else -> Unit
        }
    }

    private fun setBeatmapSource(index: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            settingRepository.setSearchSettings(
                SearchSettings(
                    beatmapSource = BeatmapSource.entries[index].name
                )
            )
        }
    }

    private fun setShowInOriginal(boolean: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            settingRepository.setAppearanceSettings(
                AppearanceSettings(
                    showInOriginalLang = boolean
                )
            )
        }
    }
}