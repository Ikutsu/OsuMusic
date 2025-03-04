package io.ikutsu.osumusic.core.di

import io.ikutsu.osumusic.core.player.NativePlayerBridge
import org.koin.dsl.module

data class IosApplicationComponent(
    private val nativePlayerBridge: NativePlayerBridge
): ApplicationComponent{
    override val module = listOf(
        module {
            single<NativePlayerBridge> { nativePlayerBridge }
        }
    )
}
