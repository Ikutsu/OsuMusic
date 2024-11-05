package io.ikutsu.osumusic.setting.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import io.ikutsu.osumusic.core.data.BeatmapSource
import io.ikutsu.osumusic.core.data.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalSettingsApi::class)
class SettingViewModel(
    private val settingStorage: FlowSettings
): ViewModel() {
    private val _uiState = MutableStateFlow(SettingUiState())
    val uiState: StateFlow<SettingUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    private fun load() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(
                    beatmapSourceOptions = BeatmapSource.entries.map { it.value }
                )
            }
            combine(
                settingStorage.getStringOrNullFlow(Constants.Setting.BEATMAP_SOURCE),
                settingStorage.getBooleanOrNullFlow(Constants.Setting.SHOW_IN_ORIGINAL_LANG)
            ) { beatmapSource, showInOriginalLang ->
                Pair(beatmapSource, showInOriginalLang)
            }.collect { (beatmapSource, showInOriginalLang) ->
                _uiState.update {
                    it.copy(
                        beatmapSource = BeatmapSource.valueOf(beatmapSource ?: BeatmapSource.SAYOBOT.name).value,
                        showInOriginalLang = showInOriginalLang ?: false
                    )
                }
            }
        }
    }

    fun setBeatmapSource(index: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            settingStorage.putString(Constants.Setting.BEATMAP_SOURCE, BeatmapSource.entries[index].name)
        }
    }

    fun setShowInOriginal(boolean: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            settingStorage.putBoolean(Constants.Setting.SHOW_IN_ORIGINAL_LANG, boolean)
        }
    }
}