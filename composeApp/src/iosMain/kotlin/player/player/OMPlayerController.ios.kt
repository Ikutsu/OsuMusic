package io.ikutsu.osumusic.player.player

import io.ikutsu.osumusic.core.domain.Music

actual class OMPlayerController {
    actual fun registerListener(listener: OMPlayerListener) {
    }

    actual fun addPlayerItem(musics: List<Music>) {
    }

    actual fun onPlayerEvent(
        event: OMPlayerEvent,
        selectedAudioIndex: Int
    ) {
    }

    actual fun getCurrentPosition(): Long {
        TODO("Not yet implemented")
    }

    actual fun release() {
    }


}