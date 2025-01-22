package io.ikutsu.osumusic.core.presentation.provider

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.ikutsu.osumusic.setting.data.AppearanceSettings
import io.ikutsu.osumusic.setting.data.SettingRepository
import org.koin.compose.koinInject

@Composable
fun AppearanceSettingProvider(
    content: @Composable () -> Unit
) {
    val settingRepository: SettingRepository = koinInject()
    val appearanceSettings by settingRepository.getAppearanceSettings().collectAsStateWithLifecycle(
        AppearanceSettings(showInOriginalLang = false)
    )
    CompositionLocalProvider(
        LocalAppearanceSetting provides appearanceSettings
    ) {
        content()
    }
}

val LocalAppearanceSetting = compositionLocalOf {
    AppearanceSettings(
        showInOriginalLang = false
    )
}