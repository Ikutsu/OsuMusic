package io.ikutsu.osumusic.core.di

import org.koin.core.module.Module

interface ApplicationComponent {
    val module: List<Module>
}