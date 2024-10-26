package io.ikutsu.osumusic.core.di

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import org.koin.dsl.module

@OptIn(ExperimentalSettingsApi::class)
val storageModule = module {
    single { get<ObservableSettings>().toFlowSettings() }
}