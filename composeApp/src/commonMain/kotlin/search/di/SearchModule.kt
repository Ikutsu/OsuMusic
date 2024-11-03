package io.ikutsu.osumusic.search.di

import io.ikutsu.osumusic.search.data.api.OsuDirectBeatmapSearchApi
import io.ikutsu.osumusic.search.data.api.SayobotBeatmapSearchApi
import io.ikutsu.osumusic.search.data.datasource.SearchRemoteDataSource
import io.ikutsu.osumusic.search.data.repository.SearchRepository
import io.ikutsu.osumusic.search.presentation.SearchViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val searchModel = module {
    singleOf(::SayobotBeatmapSearchApi)
    singleOf(::OsuDirectBeatmapSearchApi)
    singleOf(::SearchRemoteDataSource)
    singleOf(::SearchRepository)
    viewModelOf(::SearchViewModel)
}