package io.ikutsu.osumusic.search.data.model

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class SearchHistory: RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var beatmapId: Int = 0
    var title: String = ""
    var titleUnicode: String = ""
    var artist: String = ""
    var artistUnicode: String = ""
    var creator: String = ""
    var difficulty: RealmList<Float> = realmListOf()
    var coverUrl: String = ""
    var audioUrl: String = ""
}