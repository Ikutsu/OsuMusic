package io.ikutsu.osumusic.player.player

import io.ikutsu.osumusic.core.domain.Music
import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryPlayback
import platform.AVFAudio.AVAudioSessionSetActiveOptionNotifyOthersOnDeactivation
import platform.AVFAudio.setActive
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerItem
import platform.AVFoundation.AVPlayerStatusFailed
import platform.AVFoundation.AVPlayerStatusReadyToPlay
import platform.AVFoundation.AVPlayerStatusUnknown
import platform.AVFoundation.AVPlayerTimeControlStatusPlaying
import platform.AVFoundation.AVPlayerTimeControlStatusWaitingToPlayAtSpecifiedRate
import platform.AVFoundation.addPeriodicTimeObserverForInterval
import platform.AVFoundation.currentItem
import platform.AVFoundation.duration
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.AVFoundation.playbackBufferEmpty
import platform.AVFoundation.playbackLikelyToKeepUp
import platform.AVFoundation.removeTimeObserver
import platform.AVFoundation.replaceCurrentItemWithPlayerItem
import platform.AVFoundation.seekToTime
import platform.AVFoundation.timeControlStatus
import platform.CoreMedia.CMTimeGetSeconds
import platform.CoreMedia.CMTimeMakeWithSeconds
import platform.Foundation.NSKeyValueObservingOptionNew
import platform.Foundation.NSURL.Companion.URLWithString
import platform.Foundation.addObserver
import platform.Foundation.removeObserver
import platform.darwin.NSEC_PER_SEC
import platform.darwin.NSObject
import platform.foundation.NSKeyValueObservingProtocol

@OptIn(ExperimentalForeignApi::class)
actual class OMPlayerController {
    private lateinit var player: AVPlayer
    private lateinit var audioSession: AVAudioSession
    private var playerItem: AVPlayerItem? = null
    private var listeners: OMPlayerListener? = null
    private var timeObserver: Any? = null

    init {
        setUpAudioSession()
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun setUpAudioSession() {
        try {
            audioSession = AVAudioSession.sharedInstance()
            audioSession.setCategory(AVAudioSessionCategoryPlayback, null)
        } catch (e: Exception) {
            println("Error setting up audio session: ${e.message}")
        }
    }

    private fun setupPlayer() {
        player = AVPlayer()
        timeObserver = player.addPeriodicTimeObserverForInterval(
            CMTimeMakeWithSeconds(1.0, NSEC_PER_SEC.toInt()),
            null
        ) { time ->
            listeners?.onProgress(CMTimeGetSeconds(time).times(1000).toLong())
        }
        player.addObserver(
            observer = timeControlObserver,
            forKeyPath = "timeControlStatus",
            options = NSKeyValueObservingOptionNew,
            context = null
        )
    }

    actual fun registerListener(listener: OMPlayerListener) {
        setupPlayer()
        this.listeners = listener
    }

    @OptIn(ExperimentalForeignApi::class)
    actual fun addPlayerItem(musics: List<Music>) {
        listeners?.onProgress(0L)
        player.pause()
        listeners?.currentPlayerState(OMPlayerState.Buffering)
        removeCurrentItemObservers()
        listeners?.currentMusic(musics.first())
        val url = URLWithString(musics.first().source)
        player.addObserver(
            observer = playerItemObserver,
            forKeyPath = "currentItem",
            options = NSKeyValueObservingOptionNew,
            context = null
        )
        AVPlayerItem(url ?: return).apply {
            player.replaceCurrentItemWithPlayerItem(this)
            addObserver(
                observer = itemStatusObserver,
                forKeyPath = "status",
                options = NSKeyValueObservingOptionNew,
                context = null
            )
//            asset.loadValuesAsynchronouslyForKeys(listOf("progress")) {
//                // Skip to the next track when the current one ends
//                listeners?.totalDuration(CMTimeGetSeconds(asset.progress).toLong())
//            }
        }
    }

    private fun play() {
        activateAudioSession()
        player.play()
        listeners?.currentPlayerState(OMPlayerState.Playing)
    }

    private fun pause() {
        player.pause()
        listeners?.currentPlayerState(OMPlayerState.Paused)
        deactivateAudioSession()
    }



    @OptIn(ExperimentalForeignApi::class)
    actual fun onPlayerEvent(
        event: OMPlayerEvent,
        selectedAudioIndex: Int
    ) {
        when(event) {
            OMPlayerEvent.PlayPause -> {
                if (isPlaying) {
                    pause()
                } else {
                    play()
                }
            }
            OMPlayerEvent.Previous -> {
                return // TODO: Implement playlist
            }
            OMPlayerEvent.Next -> {
                return // TODO: Implement playlist
            }
            OMPlayerEvent.SelectedAudioChange -> {
                return // TODO: Implement playlist
            }
            is OMPlayerEvent.SeekTo -> {
                val duration = CMTimeGetSeconds(player.currentItem?.duration ?: CMTimeMakeWithSeconds(0.0, NSEC_PER_SEC.toInt()))
                val position = duration * event.progress
                pause()
                player.currentItem?.seekToTime(CMTimeMakeWithSeconds(position, NSEC_PER_SEC.toInt()))
                play()
            }
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun seekToInitialTime() {
        player.seekToTime(
            CMTimeMakeWithSeconds(
                seconds = 0.0,
                preferredTimescale = 1
            )
        )
    }

    actual fun release() {
        player.pause()
        if (timeObserver != null) {
            player.removeTimeObserver(timeObserver!!)
            timeObserver = null
        }
        seekToInitialTime()
        deactivateAudioSession()
        player.removeObserver(playerItemObserver, "currentItem")
        player.removeObserver(timeControlObserver, "timeControlStatus")
    }

    private fun activateAudioSession() {
        updateAudioSessionActive(true)
    }

    private fun deactivateAudioSession() {
        updateAudioSessionActive(false)
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun updateAudioSessionActive(active: Boolean) {
        try {
            audioSession.setActive(
                active = active,
                withOptions = AVAudioSessionSetActiveOptionNotifyOthersOnDeactivation,
                error = null
            )
        } catch (e: Exception) {
            println("Failed to update the audio session active to $active")
        }
    }

    private fun removeCurrentItemObservers() {
        player.currentItem?.removeObserver(itemStatusObserver, "status")
    }

    private val playerItemObserver: NSObject = object : NSObject(), NSKeyValueObservingProtocol {
        override fun observeValueForKeyPath(
            keyPath: String?,
            ofObject: Any?,
            change: Map<Any?, *>?,
            context: COpaquePointer?
        ) {
            playerItem = player.currentItem
            if (player.currentItem?.playbackBufferEmpty == true) {
                listeners?.currentPlayerState(OMPlayerState.Buffering)
            } else if (player.currentItem?.playbackLikelyToKeepUp == true) {
                listeners?.currentPlayerState(OMPlayerState.Playing)
            }

            // Now playing
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    private val itemStatusObserver: NSObject = object : NSObject(), NSKeyValueObservingProtocol {

        override fun observeValueForKeyPath(
            keyPath: String?,
            ofObject: Any?,
            change: Map<Any?, *>?,
            context: COpaquePointer?
        ) {
            if (playerItem == null) return

            when (player.currentItem?.status) {
                AVPlayerStatusUnknown -> return
                AVPlayerStatusReadyToPlay -> {
                    listeners?.totalDuration(CMTimeGetSeconds(playerItem!!.duration).toLong().times(1000))
                    seekToInitialTime()
                }
                AVPlayerStatusFailed -> OMPlayerState.Error
                else -> return
            }
        }
    }

    @ExperimentalForeignApi
    private val timeControlObserver: NSObject = object : NSObject(), NSKeyValueObservingProtocol {

        override fun observeValueForKeyPath(
            keyPath: String?,
            ofObject: Any?,
            change: Map<Any?, *>?,
            context: COpaquePointer?
        ) {
            when (player.timeControlStatus) {
                AVPlayerTimeControlStatusPlaying -> listeners?.currentPlayerState(OMPlayerState.Playing)
                AVPlayerTimeControlStatusWaitingToPlayAtSpecifiedRate -> listeners?.currentPlayerState(OMPlayerState.Buffering)
                else -> return
            }
        }
    }

    private val isPlaying: Boolean
        get() = player.timeControlStatus == AVPlayerTimeControlStatusPlaying
}
