package io.ikutsu.osumusic.core.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration


fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(commonModule + platformModule)
    }
}

val commonModule = presentationModule + networkModule + storageModule