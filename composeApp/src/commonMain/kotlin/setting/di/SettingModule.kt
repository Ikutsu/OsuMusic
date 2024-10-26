package io.ikutsu.osumusic.setting.di

import com.russhwolf.settings.ExperimentalSettingsApi
import io.ikutsu.osumusic.setting.presentation.SettingViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

@OptIn(ExperimentalSettingsApi::class)
val settingModule = module {
    viewModelOf(::SettingViewModel)
}