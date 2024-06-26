package io.ikutsu.osumusic.core.di

import coil3.PlatformContext
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val platformModule: Module = module {
    singleOf(PlatformContext::INSTANCE)
}