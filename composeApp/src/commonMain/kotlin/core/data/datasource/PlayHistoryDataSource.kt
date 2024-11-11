package io.ikutsu.osumusic.core.data.datasource

import io.ikutsu.osumusic.core.data.model.PlayHistory
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class PlayHistoryDataSource(
    private val realm: Realm
) {
    suspend fun savePlayHistory(playHistory: PlayHistory) {
        withContext(Dispatchers.IO) {
            realm.write {
                copyToRealm(playHistory, UpdatePolicy.ALL)
            }
        }
    }

    fun getPlayHistory(): Flow<List<PlayHistory>> {
        return realm.query<PlayHistory>().asFlow().map { it.list }
    }
}