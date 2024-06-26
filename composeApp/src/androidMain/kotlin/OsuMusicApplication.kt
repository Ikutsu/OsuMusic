package io.ikutsu.osumusic

import android.app.Application
import coil3.PlatformContext
import io.ikutsu.osumusic.core.di.commonModule
import io.ikutsu.osumusic.core.di.platformModule
import org.koin.core.context.startKoin
import org.koin.dsl.module

class OsuMusicApplication: Application() {

    private val platformContextModule = module {
        single<PlatformContext> { this@OsuMusicApplication }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(commonModule + platformModule + platformContextModule)
        }
    }
}