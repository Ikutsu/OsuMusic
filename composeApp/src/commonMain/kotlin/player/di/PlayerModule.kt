package io.ikutsu.osumusic.player.di

import io.ikutsu.osumusic.player.presentation.PlayerViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val playerModule = module {
    viewModelOf(::PlayerViewModel)
}