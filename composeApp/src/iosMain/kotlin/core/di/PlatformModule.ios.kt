package io.ikutsu.osumusic.core.di

import coil3.PlatformContext
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.ObservableSettings
import io.ikutsu.osumusic.player.player.OMPlayerController
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule: Module = module {
    singleOf(PlatformContext::INSTANCE)
    singleOf(::OMPlayerController)
    single { NSUserDefaultsSettings.Factory().create() } bind ObservableSettings::class
}