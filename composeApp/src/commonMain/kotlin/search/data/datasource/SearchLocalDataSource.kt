package io.ikutsu.osumusic.search.data.datasource

import io.ikutsu.osumusic.search.data.model.SearchHistory
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class SearchLocalDataSource(
    private val realm: Realm
) {
    suspend fun saveSearchHistory(searchHistory: SearchHistory) {
        withContext(Dispatchers.IO) {
            realm.write {
                copyToRealm(searchHistory)
            }
        }
    }

    fun getSearchHistory(): Flow<List<SearchHistory>> {
        return realm.query<SearchHistory>().asFlow().map { it.list }
    }
}