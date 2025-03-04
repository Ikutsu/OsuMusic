package io.ikutsu.osumusic

import android.app.Application
import io.ikutsu.osumusic.core.di.KoinInitHelper
import org.koin.android.ext.koin.androidContext

class OsuMusicApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        KoinInitHelper().initKoin(
            config = {
                androidContext(this@OsuMusicApplication)
            }
        )
    }
}