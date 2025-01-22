package io.ikutsu.osumusic.setting.di

import io.ikutsu.osumusic.setting.presentation.SettingViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val settingModule = module {
    viewModelOf(::SettingViewModel)
}