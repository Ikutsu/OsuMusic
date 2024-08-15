package io.ikutsu.osumusic

import android.app.Application
import io.ikutsu.osumusic.core.di.initKoin
import org.koin.android.ext.koin.androidContext

class OsuMusicApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@OsuMusicApplication)
        }
    }
}