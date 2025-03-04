package io.ikutsu.osumusic.core.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

class KoinInitHelper {
    fun initKoin(config: KoinAppDeclaration? = null, applicationComponent: ApplicationComponent? = null) {
        startKoin {
            config?.invoke(this)
            modules(commonModule + applicationComponent?.module.orEmpty() + platformModule)
        }
    }
}

val commonModule = presentationModule + networkModule + storageModule