package io.ikutsu.osumusic.search.data.local

import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class SearchLocalDataSource(
    private val realm: Realm
) {
    suspend fun saveSearchHistory(searchHistoryEntity: SearchHistoryEntity) {
        withContext(Dispatchers.IO) {
            realm.write {
                copyToRealm(searchHistoryEntity, UpdatePolicy.ALL)
            }
        }
    }

    fun getSearchHistory(): Flow<List<SearchHistoryEntity>> {
        return realm.query<SearchHistoryEntity>().asFlow().map { it.list }
    }
}