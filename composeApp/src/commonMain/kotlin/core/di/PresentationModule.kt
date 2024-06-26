package io.ikutsu.osumusic.core.di

import coil3.ImageLoader
import coil3.PlatformContext
import coil3.disk.DiskCache
import okio.FileSystem
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val presentationModule = module {
    singleOf(::imageLoader)
}

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