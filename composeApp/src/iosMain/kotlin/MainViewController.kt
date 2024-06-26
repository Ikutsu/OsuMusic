package io.ikutsu.osumusic

import androidx.compose.ui.window.ComposeUIViewController
import io.ikutsu.osumusic.core.di.commonModule
import io.ikutsu.osumusic.core.di.platformModule
import org.koin.core.context.startKoin

fun MainViewController() = ComposeUIViewController { App() }

fun startKoin() = startKoin {
    modules(commonModule + platformModule)
}