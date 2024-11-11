package io.ikutsu.osumusic.core.di

import io.ikutsu.osumusic.home.di.homeModule
import io.ikutsu.osumusic.player.di.playerModule
import io.ikutsu.osumusic.search.di.searchModule
import io.ikutsu.osumusic.setting.di.settingModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration


fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(commonModule + platformModule)
    }
}

val screenModule = homeModule + searchModule + playerModule + settingModule

val commonModule = presentationModule + networkModule + storageModule + screenModule