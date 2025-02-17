package io.ikutsu.osumusic.setting.data

import androidx.compose.ui.text.intl.Locale
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import io.ikutsu.osumusic.core.domain.BeatmapSource
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
        settingStorage.getBooleanOrNullFlow(SettingKeys.SHOW_IN_ORIGINAL_LANG).map {
            AppearanceSettings(
                showInOriginalLang = it ?: false
            )
        }

    suspend fun setAppearanceSettings(appearanceSettings: AppearanceSettings) {
        settingStorage.putBoolean(
            SettingKeys.SHOW_IN_ORIGINAL_LANG,
            appearanceSettings.showInOriginalLang
        )
    }

    fun getSearchSettings(): Flow<SearchSettings> =
        settingStorage.getStringOrNullFlow(SettingKeys.BEATMAP_SOURCE).map {
            SearchSettings(
                beatmapSource = it ?: if (Locale.current.region == "CN") {
                    BeatmapSource.SAYOBOT.name  // Sayobot server is based in China, so it's the default source for Chinese users
                } else {
                    BeatmapSource.OSU_DIRECT.name // OsuDirect is the default source for other regions
                }
            )
        }

    suspend fun setSearchSettings(searchSettings: SearchSettings) {
        settingStorage.putString(SettingKeys.BEATMAP_SOURCE, searchSettings.beatmapSource)
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