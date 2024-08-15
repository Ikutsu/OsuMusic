package io.ikutsu.osumusic

import androidx.compose.ui.window.ComposeUIViewController
import io.ikutsu.osumusic.core.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}