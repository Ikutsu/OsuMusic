package io.ikutsu.osumusic.player.player

import androidx.core.bundle.bundleOf
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import io.ikutsu.osumusic.core.domain.Music

fun MediaItem.toMusic(): Music {
    return Music(
        title = this.mediaMetadata.title.toString(),
        unicodeTitle = this.mediaMetadata.extras?.getString("unicodeTitle") ?: "",
        artist = this.mediaMetadata.artist.toString(),
        unicodeArtist = this.mediaMetadata.extras?.getString("unicodeArtist") ?: "",
        creator = this.mediaMetadata.extras?.getString("creator") ?: "",
        difficulty = this.mediaMetadata.extras?.getFloat("difficulties") ?: 0f,
        coverUrl = this.mediaMetadata.artworkUri.toString(),
        backgroundUrl = this.mediaMetadata.extras?.getString("backgroundUrl") ?: "",
        source = this.mediaId
    )
}

fun Music.toMediaItem(): MediaItem {
    return MediaItem.Builder()
        .setMediaId(this.source)
        .setUri(this.source)
        .setMediaMetadata(
            MediaMetadata.Builder()
                .setTitle(this.title)
                .setArtist(this.artist)
                .setArtworkUri(this.coverUrl.toUri())
                .setExtras(
                    bundleOf(
                        "unicodeTitle" to this.unicodeTitle,
                        "unicodeArtist" to this.unicodeArtist,
                        "creator" to this.creator,
                        "difficulties" to this.difficulty,
                        "backgroundUrl" to this.backgroundUrl
                    )
                )
                .build()
        )
        .build()
}