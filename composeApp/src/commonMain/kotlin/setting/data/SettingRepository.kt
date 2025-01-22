package io.ikutsu.osumusic.setting.data

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import io.ikutsu.osumusic.core.data.BeatmapSource
import io.ikutsu.osumusic.core.data.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSettingsApi::class)
class SettingRepository(
    private val settingStorage: FlowSettings
) {
    fun getAllSettings(): Flow<AppSetting> =
        combine(
            getAppearanceSettings(),
            getSearchSettings()
        ) { appearanceSettings, searchSettings ->
            AppSetting(
                appearanceSettings = appearanceSettings,
                searchSettings = searchSettings
            )
        }

    suspend fun setAllSettings(appSetting: AppSetting) {
        setAppearanceSettings(appSetting.appearanceSettings)
        setSearchSettings(appSetting.searchSettings)
    }

    fun getAppearanceSettings(): Flow<AppearanceSettings> =
        settingStorage.getBooleanOrNullFlow(Constants.Setting.SHOW_IN_ORIGINAL_LANG).map {
            AppearanceSettings(
                showInOriginalLang = it ?: false
            )
        }

    suspend fun setAppearanceSettings(appearanceSettings: AppearanceSettings) {
        settingStorage.putBoolean(
            Constants.Setting.SHOW_IN_ORIGINAL_LANG,
            appearanceSettings.showInOriginalLang
        )
    }

    fun getSearchSettings(): Flow<SearchSettings> =
        settingStorage.getStringOrNullFlow(Constants.Setting.BEATMAP_SOURCE).map {
            SearchSettings(
                beatmapSource = it ?: BeatmapSource.SAYOBOT.name // Todo: Change the default to region-based
            )
        }

    suspend fun setSearchSettings(searchSettings: SearchSettings) {
        settingStorage.putString(Constants.Setting.BEATMAP_SOURCE, searchSettings.beatmapSource)
    }
}

data class AppSetting(
    val appearanceSettings: AppearanceSettings,
    val searchSettings: SearchSettings
)

@Serializable
data class AppearanceSettings(
    val showInOriginalLang: Boolean
)

@Serializable
data class SearchSettings(
    val beatmapSource: String
)