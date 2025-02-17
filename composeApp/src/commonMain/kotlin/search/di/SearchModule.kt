package io.ikutsu.osumusic.search.di

import io.ikutsu.osumusic.search.data.local.SearchLocalDataSource
import io.ikutsu.osumusic.search.data.remote.SearchRemoteDataSource
import io.ikutsu.osumusic.search.data.remote.api.osu_direct.OsuDirectBeatmapSearchApi
import io.ikutsu.osumusic.search.data.remote.api.sayobot.SayobotBeatmapSearchApi
import io.ikutsu.osumusic.search.data.repository.SearchRepository
import io.ikutsu.osumusic.search.presentation.SearchViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val searchModule = module {
    singleOf(::SayobotBeatmapSearchApi)
    singleOf(::OsuDirectBeatmapSearchApi)
    singleOf(::SearchRemoteDataSource)
    singleOf(::SearchLocalDataSource)
    singleOf(::SearchRepository)
    viewModelOf(::SearchViewModel)
}