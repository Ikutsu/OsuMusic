package io.ikutsu.osumusic.core.di

import io.ikutsu.osumusic.search.di.searchModel
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration


fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(commonModule + platformModule)
    }
}

val commonModule = presentationModule + networkModule + searchModel