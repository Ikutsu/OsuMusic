package io.ikutsu.osumusic.core.di

import coil3.ImageLoader
import coil3.PlatformContext
import coil3.disk.DiskCache
import io.ikutsu.osumusic.home.di.homeModule
import io.ikutsu.osumusic.player.di.playerModule
import io.ikutsu.osumusic.search.di.searchModule
import io.ikutsu.osumusic.setting.di.settingModule
import okio.FileSystem
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val presentationModule = module {
    singleOf(::imageLoader)
} + homeModule + searchModule + playerModule + settingModule

fun imageLoader(
    context: PlatformContext
) = ImageLoader.Builder(context)
        .diskCache {
            DiskCache.Builder()
                .maxSizePercent(0.03)
                .directory(FileSystem.SYSTEM_TEMPORARY_DIRECTORY / "om_image_cache")
                .build()
        }
        .build()