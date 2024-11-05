package io.ikutsu.osumusic.core.di

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import io.ikutsu.osumusic.search.data.model.SearchHistory
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import org.koin.dsl.module

@OptIn(ExperimentalSettingsApi::class)
val storageModule = module {
    single { get<ObservableSettings>().toFlowSettings() }
    single {
        Realm.open(
            RealmConfiguration.create(
                schema = setOf(SearchHistory::class)
            )
        )
    }
}