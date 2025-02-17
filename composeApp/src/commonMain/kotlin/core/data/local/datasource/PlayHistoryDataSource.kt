package io.ikutsu.osumusic.core.data.local.datasource

import io.ikutsu.osumusic.core.data.model.PlayHistoryEntity
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
    suspend fun savePlayHistory(playHistoryEntity: PlayHistoryEntity) {
        withContext(Dispatchers.IO) {
            realm.write {
                copyToRealm(playHistoryEntity, UpdatePolicy.ALL)
            }
        }
    }

    fun getPlayHistory(): Flow<List<PlayHistoryEntity>> {
        return realm.query<PlayHistoryEntity>().asFlow().map { it.list }
    }
}