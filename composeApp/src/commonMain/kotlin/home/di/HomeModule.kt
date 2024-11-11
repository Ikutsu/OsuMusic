package io.ikutsu.osumusic.home.di

import io.ikutsu.osumusic.home.presentation.HomeViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val homeModule = module {
    singleOf(::HomeViewModel)
}