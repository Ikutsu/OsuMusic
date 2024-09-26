package io.ikutsu.osumusic.player.player

import io.ikutsu.osumusic.core.domain.Music
import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryPlayback
import platform.AVFAudio.AVAudioSessionInterruptionNotification
import platform.AVFAudio.AVAudioSessionInterruptionOptionShouldResume
import platform.AVFAudio.AVAudioSessionInterruptionOptions
import platform.AVFAudio.AVAudioSessionInterruptionType
import platform.AVFAudio.AVAudioSessionInterruptionTypeBegan
import platform.AVFAudio.AVAudioSessionInterruptionTypeEnded
import platform.AVFAudio.AVAudioSessionInterruptionTypeKey
import platform.AVFAudio.AVAudioSessionRouteChangeNotification
import platform.AVFAudio.AVAudioSessionRouteChangeReason
import platform.AVFAudio.AVAudioSessionRouteChangeReasonKey
import platform.AVFAudio.AVAudioSessionRouteChangeReasonNewDeviceAvailable
import platform.AVFAudio.AVAudioSessionRouteChangeReasonOldDeviceUnavailable
import platform.AVFAudio.AVAudioSessionSetActiveOptionNotifyOthersOnDeactivation
import platform.AVFAudio.setActive
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerItem
import platform.AVFoundation.AVPlayerItemDidPlayToEndTimeNotification
import platform.AVFoundation.AVPlayerStatusFailed
import platform.AVFoundation.AVPlayerStatusReadyToPlay
import platform.AVFoundation.AVPlayerStatusUnknown
import platform.AVFoundation.AVPlayerTimeControlStatusPlaying
import platform.AVFoundation.AVPlayerTimeControlStatusWaitingToPlayAtSpecifiedRate
import platform.AVFoundation.addPeriodicTimeObserverForInterval
import platform.AVFoundation.currentItem
import platform.AVFoundation.currentTime
import platform.AVFoundation.duration
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.AVFoundation.rate
import platform.AVFoundation.removeTimeObserver
import platform.AVFoundation.replaceCurrentItemWithPlayerItem
import platform.AVFoundation.seekToTime
import platform.AVFoundation.timeControlStatus
import platform.CoreMedia.CMTimeGetSeconds
import platform.CoreMedia.CMTimeMakeWithSeconds
import platform.Foundation.NSData
import platform.Foundation.NSKeyValueObservingOptionNew
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue
import platform.Foundation.NSURL
import platform.Foundation.NSURL.Companion.URLWithString
import platform.Foundation.NSURLErrorDomain
import platform.Foundation.addObserver
import platform.Foundation.dataWithContentsOfURL
import platform.Foundation.removeObserver
import platform.MediaPlayer.MPChangePlaybackPositionCommandEvent
import platform.MediaPlayer.MPMediaItemArtwork
import platform.MediaPlayer.MPMediaItemPropertyArtist
import platform.MediaPlayer.MPMediaItemPropertyArtwork
import platform.MediaPlayer.MPMediaItemPropertyPlaybackDuration
import platform.MediaPlayer.MPMediaItemPropertyTitle
import platform.MediaPlayer.MPNowPlayingInfoCenter
import platform.MediaPlayer.MPNowPlayingInfoPropertyDefaultPlaybackRate
import platform.MediaPlayer.MPNowPlayingInfoPropertyElapsedPlaybackTime
import platform.MediaPlayer.MPNowPlayingInfoPropertyPlaybackRate
import platform.MediaPlayer.MPRemoteCommandCenter
import platform.MediaPlayer.MPRemoteCommandHandlerStatus
import platform.MediaPlayer.MPRemoteCommandHandlerStatusSuccess
import platform.UIKit.UIImage
import platform.darwin.DISPATCH_QUEUE_PRIORITY_DEFAULT
import platform.darwin.NSEC_PER_SEC
import platform.darwin.NSObject
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_global_queue
import platform.darwin.dispatch_get_main_queue
import platform.foundation.NSKeyValueObservingProtocol
import platform.objc.sel_registerName

@OptIn(ExperimentalForeignApi::class)
actual class OMPlayerController {
    private lateinit var player: AVPlayer
    private lateinit var audioSession: AVAudioSession
    private lateinit var nowPlayingInfoCenter: MPNowPlayingInfoCenter
    private lateinit var remoteCommandCenter: MPRemoteCommandCenter
    private var music: Music? = null
    private var playerItem: AVPlayerItem? = null
    private var listeners: OMPlayerListener? = null
    private var timeObserver: Any? = null
    private var sessionInterruptionObserver: Any? = null
    private var sessionRouteChangeObserver: Any? = null

    init {
        setUpAudioSession()
    }

    // --- Setup ---
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
            dispatch_get_main_queue()
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

    private fun setupNotificationObservers() {
        sessionInterruptionObserver()
        sessionRouteChangeObserver()
    }

    private fun sessionInterruptionObserver() {
        sessionInterruptionObserver = NSNotificationCenter.defaultCenter.addObserverForName(
            name = AVAudioSessionInterruptionNotification,
            `object` = audioSession,
            queue = NSOperationQueue.mainQueue
        ) { notification ->
            if (notification?.userInfo == null) return@addObserverForName
            val interruptionType =
                notification.userInfo!![AVAudioSessionInterruptionTypeKey] as? AVAudioSessionInterruptionType
            when (interruptionType) {
                AVAudioSessionInterruptionTypeBegan -> {
                    pause()
                }

                AVAudioSessionInterruptionTypeEnded -> {
                    val options =
                        notification.userInfo!![AVAudioSessionInterruptionTypeKey] as? AVAudioSessionInterruptionOptions
                    if (options == AVAudioSessionInterruptionOptionShouldResume) {
                        play()
                    }
                }

                else -> return@addObserverForName
            }
        }
    }

    private fun sessionRouteChangeObserver() {
        sessionRouteChangeObserver = NSNotificationCenter.defaultCenter.addObserverForName(
            name = AVAudioSessionRouteChangeNotification,
            `object` = audioSession,
            queue = NSOperationQueue.mainQueue
        ) { notification ->
            if (notification?.userInfo == null) return@addObserverForName
            val changeReason =
                notification.userInfo!![AVAudioSessionRouteChangeReasonKey] as? AVAudioSessionRouteChangeReason
            when (changeReason) {
                AVAudioSessionRouteChangeReasonNewDeviceAvailable -> {
                    play()
                }

                AVAudioSessionRouteChangeReasonOldDeviceUnavailable -> {
                    pause()
                }

                else -> return@addObserverForName
            }
        }
    }

    private fun removeAudioSessionObservers() {
        if (sessionInterruptionObserver != null) {
            NSNotificationCenter.defaultCenter.removeObserver(sessionInterruptionObserver!!)
            sessionInterruptionObserver = null
        }
        if (sessionRouteChangeObserver != null) {
            NSNotificationCenter.defaultCenter.removeObserver(sessionRouteChangeObserver!!)
            sessionRouteChangeObserver = null
        }
    }

    actual fun registerListener(listener: OMPlayerListener) {
        setupPlayer()
        setupNotificationObservers()
        nowPlayingInfoCenter = MPNowPlayingInfoCenter.defaultCenter()
        remoteCommandCenter = MPRemoteCommandCenter.sharedCommandCenter()
//        activatePlaybackCommands(true)
//        activateChangePlaybackPositionCommand(true)
        this.listeners = listener
    }

    // --- Player ---
    @OptIn(ExperimentalForeignApi::class)
    actual fun addPlayerItem(musics: List<Music>) {
        listeners?.onProgress(0L)
        player.pause()

        listeners?.currentPlayerState(OMPlayerState.Buffering)
        listeners?.currentMusic(musics.first())
        music = musics.first()

        removeCurrentItemObservers()
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
        NSNotificationCenter.defaultCenter.addObserverForName(
            name = AVPlayerItemDidPlayToEndTimeNotification,
            `object` = player.currentItem,
            queue = NSOperationQueue.mainQueue,
        ) {
            listeners?.currentPlayerState(OMPlayerState.Stopped)
        }
    }

    private fun play() {
        activateAudioSession()
        player.play()
    }

    private fun pause() {
        player.pause()
        listeners?.currentPlayerState(OMPlayerState.Paused)
        deactivateAudioSession()
    }

    private fun togglePlayPause() {
        if (isPlaying) {
            pause()
        } else {
            play()
        }
    }

    private fun seekToPosition(progress: Float) {
        val duration = CMTimeGetSeconds(
            player.currentItem?.duration ?: CMTimeMakeWithSeconds(
                0.0,
                NSEC_PER_SEC.toInt()
            )
        )
        val position = duration * progress
        pause()
        player.currentItem?.seekToTime(
            CMTimeMakeWithSeconds(
                position,
                NSEC_PER_SEC.toInt()
            )
        )
        updatePlayBackMetadata()
        play()
    }

    actual fun onPlayerEvent(
        event: OMPlayerEvent,
        selectedAudioIndex: Int
    ) {
        when (event) {
            OMPlayerEvent.PlayPause -> {
                togglePlayPause()
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
                seekToPosition(event.progress)
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

    private val isPlaying: Boolean
        get() = player.timeControlStatus == AVPlayerTimeControlStatusPlaying

    actual fun release() {
        player.pause()
        if (timeObserver != null) {
            player.removeTimeObserver(timeObserver!!)
            timeObserver = null
        }
        seekToInitialTime()
        deactivateAudioSession()
        deactivateAllRemoteCommands()
        player.removeObserver(playerItemObserver, "currentItem")
        player.removeObserver(timeControlObserver, "timeControlStatus")
        removeAudioSessionObservers()
    }

    // --- Audio Session ---
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

    // --- Now Playing Info ---
    private fun setNowPlayingInfo() {
        if (music == null) return
        val nowPlayingInfo = nowPlayingInfoCenter.nowPlayingInfo?.toMutableMap() ?: mutableMapOf()
        nowPlayingInfo[MPMediaItemPropertyTitle] = music!!.title
        nowPlayingInfo[MPMediaItemPropertyArtist] = music!!.artist
        nowPlayingInfoCenter.nowPlayingInfo = nowPlayingInfo

        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT.toLong(), 0u)) {
            val imageData = NSData.dataWithContentsOfURL(URLWithString(music!!.coverUrl)!!)
            val image = imageData?.let { UIImage(data = it) }
            dispatch_async(dispatch_get_main_queue()) {
                if (image is UIImage) {
                    val artwork = MPMediaItemArtwork(boundsSize = image.size) { _ -> image }
                    nowPlayingInfo[MPMediaItemPropertyArtwork] = artwork
                    nowPlayingInfoCenter.nowPlayingInfo = nowPlayingInfo
                }
            }
        }
    }

    private fun updatePlayBackMetadata() {
        if (playerItem == null) return
        val nowPlayingInfo = nowPlayingInfoCenter.nowPlayingInfo?.toMutableMap() ?: mutableMapOf()
        nowPlayingInfo[MPMediaItemPropertyPlaybackDuration] = CMTimeGetSeconds(playerItem!!.duration)
        nowPlayingInfo[MPNowPlayingInfoPropertyElapsedPlaybackTime] = CMTimeGetSeconds(playerItem!!.currentTime())
        nowPlayingInfo[MPNowPlayingInfoPropertyPlaybackRate] = player.rate
        nowPlayingInfo[MPNowPlayingInfoPropertyDefaultPlaybackRate] = player.rate
        nowPlayingInfoCenter.nowPlayingInfo = nowPlayingInfo
    }

    // --- Remote Control ---
    private fun activatePlaybackCommands(enabled: Boolean) {
        if (enabled) {
            remoteCommandCenter.playCommand.addTarget(this, sel_registerName("handlePlayCommandEvent"))
            remoteCommandCenter.pauseCommand.addTarget(this, sel_registerName("handlePauseCommandEvent"))
            remoteCommandCenter.togglePlayPauseCommand.addTarget(this, sel_registerName("handleTogglePlayPauseCommandEvent"))
        } else {
            remoteCommandCenter.playCommand.removeTarget(this, sel_registerName("handlePlayCommandEvent"))
            remoteCommandCenter.pauseCommand.removeTarget(this, sel_registerName("handlePauseCommandEvent"))
            remoteCommandCenter.togglePlayPauseCommand.removeTarget(this, sel_registerName("handleTogglePlayPauseCommandEvent"))
        }
        remoteCommandCenter.playCommand.enabled = enabled
        remoteCommandCenter.pauseCommand.enabled = enabled
        remoteCommandCenter.stopCommand.enabled = enabled
        remoteCommandCenter.togglePlayPauseCommand.enabled = enabled
    }

//    fun activateNextTrackCommand(enabled: Boolean) {
//        if (enabled) {
//            remoteCommandCenter.nextTrackCommand.addTarget(this, sel_registerName(::handleNextTrackCommandEvent))
//        } else {
//            remoteCommandCenter.nextTrackCommand.removeTarget(this, sel_registerName(::handleNextTrackCommandEvent))
//        }
//        remoteCommandCenter.nextTrackCommand.enabled = enabled
//    }
//
//    fun activatePreviousTrackCommand(enabled: Boolean) {
//        if (enabled) {
//            remoteCommandCenter.previousTrackCommand.addTarget(this, sel_registerName(::handlePreviousTrackCommandEvent))
//        } else {
//            remoteCommandCenter.previousTrackCommand.removeTarget(this, sel_registerName(::handlePreviousTrackCommandEvent))
//        }
//        remoteCommandCenter.previousTrackCommand.enabled = enabled
//    }

    fun activateChangePlaybackPositionCommand(enabled: Boolean) {
        if (enabled) {
            remoteCommandCenter.changePlaybackPositionCommand.addTarget(this, sel_registerName("handleChangePlaybackPositionCommandEvent"))
        } else {
            remoteCommandCenter.changePlaybackPositionCommand.removeTarget(this, sel_registerName("handleChangePlaybackPositionCommandEvent"))
        }
        remoteCommandCenter.changePlaybackPositionCommand.enabled = enabled
    }

    fun deactivateAllRemoteCommands() {
        activatePlaybackCommands(false)
//        activateNextTrackCommand(false)
//        activatePreviousTrackCommand(false)
        activateChangePlaybackPositionCommand(false)
    }

    private fun handlePlayCommandEvent(): MPRemoteCommandHandlerStatus {
        play()
        return MPRemoteCommandHandlerStatusSuccess
    }

    private fun handlePauseCommandEvent(): MPRemoteCommandHandlerStatus {
        pause()
        return MPRemoteCommandHandlerStatusSuccess
    }

    private fun handleTogglePlayPauseCommandEvent(): MPRemoteCommandHandlerStatus {
        togglePlayPause()
        return MPRemoteCommandHandlerStatusSuccess
    }

//    private fun handleNextTrackCommandEvent(): MPRemoteCommandHandlerStatus {
//        return if (playerItem != null) {
//            switchNext()
//            MPRemoteCommandHandlerStatusSuccess
//        } else {
//            MPRemoteCommandHandlerStatusNoSuchContent
//        }
//    }

//    private fun handlePreviousTrackCommandEvent(): MPRemoteCommandHandlerStatus {
//        return if (playerItem != null) {
//            switchPrevious()
//            MPRemoteCommandHandlerStatusSuccess
//        } else {
//            MPRemoteCommandHandlerStatusNoSuchContent
//        }
//    }

    private fun handleChangePlaybackPositionCommandEvent(event: MPChangePlaybackPositionCommandEvent): MPRemoteCommandHandlerStatus {
        seekToPosition(event.positionTime.toFloat())
        return MPRemoteCommandHandlerStatusSuccess
    }

    // --- Observers ---
    private fun removeCurrentItemObservers() {
        player.currentItem?.removeObserver(itemStatusObserver, "status")
        NSNotificationCenter.defaultCenter.removeObserver(
            this,
            AVPlayerItemDidPlayToEndTimeNotification,
            player.currentItem
        )
    }

    private val playerItemObserver: NSObject = object : NSObject(), NSKeyValueObservingProtocol {
        override fun observeValueForKeyPath(
            keyPath: String?,
            ofObject: Any?,
            change: Map<Any?, *>?,
            context: COpaquePointer?
        ) {
            playerItem = player.currentItem

            setNowPlayingInfo()
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
                    listeners?.totalDuration(
                        CMTimeGetSeconds(playerItem!!.duration).toLong().times(1000)
                    )
                    seekToInitialTime()
                }

                AVPlayerStatusFailed -> {
                    val errorMsg = when (playerItem?.error?.domain) {
                        null -> return
                        NSURLErrorDomain -> "Source unavailable"
                        else -> playerItem?.error?.localizedFailureReason
                    }
                    listeners?.onError(errorMsg ?: "Player error")
                    listeners?.currentPlayerState(OMPlayerState.Error)
                }

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
                AVPlayerTimeControlStatusWaitingToPlayAtSpecifiedRate -> {
                    if (player.currentItem?.error == null) {
                        listeners?.currentPlayerState(OMPlayerState.Buffering)
                    }
                }

                else -> return
            }
        }
    }
}
