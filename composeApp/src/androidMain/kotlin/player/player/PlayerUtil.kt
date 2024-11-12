package io.ikutsu.osumusic.player.player

import androidx.media3.common.MediaItem
import io.ikutsu.osumusic.core.domain.Music

fun MediaItem.toMusic(): Music {
    return Music(
        title = this.mediaMetadata.title.toString(),
        artist = this.mediaMetadata.artist.toString(),
        creator = this.mediaMetadata.extras?.getString("creator") ?: "",
        diff = this.mediaMetadata.extras?.getFloat("diff") ?: 0f,
        coverUrl = this.mediaMetadata.artworkUri.toString(),
        backgroundUrl = this.mediaMetadata.extras?.getString("backgroundUrl") ?: "",
        source = this.mediaId
    )
}