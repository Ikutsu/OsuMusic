package io.ikutsu.osumusic.core.player

import io.ikutsu.osumusic.core.domain.Music
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryPlayback
import platform.AVFAudio.AVAudioSessionInterruptionNotification
import platform.AVFAudio.AVAudioSessionInterruptionType
import platform.AVFAudio.AVAudioSessionInterruptionTypeBegan
import platform.AVFAudio.AVAudioSessionInterruptionTypeEnded
import platform.AVFAudio.AVAudioSessionInterruptionTypeKey
import platform.AVFAudio.AVAudioSessionModeDefault
import platform.AVFAudio.AVAudioSessionRouteSharingPolicyLongFormAudio
import platform.AVFAudio.AVAudioSessionSetActiveOptionNotifyOthersOnDeactivation
import platform.AVFAudio.setActive
import platform.AVFoundation.AVPlayerItem
import platform.CoreMedia.CMTimeGetSeconds
import platform.CoreMedia.CMTimeMake
import platform.CoreMedia.CMTimeMakeWithSeconds
import platform.Foundation.NSData
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue
import platform.Foundation.NSURL.Companion.URLWithString
import platform.Foundation.dataWithContentsOfURL
import platform.MediaPlayer.MPMediaItemArtwork
import platform.MediaPlayer.MPMediaItemPropertyAlbumTitle
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
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_global_queue
import platform.darwin.dispatch_get_main_queue
import platform.darwin.sel_registerName
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

@OptIn(ExperimentalForeignApi::class)
actual class OMPlayerController : KoinComponent {
    private lateinit var audioSession: AVAudioSession
    private lateinit var nowPlayingInfoCenter: MPNowPlayingInfoCenter
    private lateinit var remoteCommandCenter: MPRemoteCommandCenter

    private val player: NativePlayerBridge by inject()
    private var sessionInterruptionObserver: Any? = null
    private var sessionRouteChangeObserver: Any? = null

    private val nowPlayingInfo: MutableMap<Any?, Any?> = mutableMapOf()

    private val _playbackState: MutableStateFlow<OMPlayerPlaybackState> =
        MutableStateFlow(OMPlayerPlaybackState())
    actual val playbackState: StateFlow<OMPlayerPlaybackState> = _playbackState.asStateFlow()

    private val _queueState: MutableStateFlow<OMPlayerQueueState> =
        MutableStateFlow(OMPlayerQueueState())
    actual val queueState: StateFlow<OMPlayerQueueState> = _queueState.asStateFlow()

    // --- Setup ---
    @OptIn(ExperimentalForeignApi::class)
    private fun setUpAudioSession() {
        try {
            audioSession = AVAudioSession.sharedInstance()
            audioSession.setCategory(
                AVAudioSessionCategoryPlayback,
                AVAudioSessionModeDefault,
                AVAudioSessionRouteSharingPolicyLongFormAudio,
                null)
        } catch (e: Exception) {
            println("Error setting up audio session: ${e.message}")
        }
    }

//    private fun setupNotificationObservers() {
//        sessionInterruptionObserver()
//        sessionRouteChangeObserver()
//    }
//
//    private fun sessionInterruptionObserver() {
//        sessionInterruptionObserver = NSNotificationCenter.defaultCenter.addObserverForName(
//            name = AVAudioSessionInterruptionNotification,
//            `object` = audioSession,
//            queue = NSOperationQueue.mainQueue
//        ) { notification ->
//            if (notification?.userInfo == null) return@addObserverForName
//            val interruptionType =
//                notification.userInfo!![AVAudioSessionInterruptionTypeKey] as? AVAudioSessionInterruptionType
//            when (interruptionType) {
//                AVAudioSessionInterruptionTypeBegan -> {
//                    pause()
//                }
//
//                AVAudioSessionInterruptionTypeEnded -> {
//                    val options =
//                        notification.userInfo!![AVAudioSessionInterruptionTypeKey] as? AVAudioSessionInterruptionOptions
//                    if (options == AVAudioSessionInterruptionOptionShouldResume) {
//                        play()
//                    }
//                }
//
//                else -> return@addObserverForName
//            }
//        }
//    }
//
//    private fun sessionRouteChangeObserver() {
//        sessionRouteChangeObserver = NSNotificationCenter.defaultCenter.addObserverForName(
//            name = AVAudioSessionRouteChangeNotification,
//            `object` = audioSession,
//            queue = NSOperationQueue.mainQueue
//        ) { notification ->
//            if (notification?.userInfo == null) return@addObserverForName
//            val changeReason =
//                notification.userInfo!![AVAudioSessionRouteChangeReasonKey] as? AVAudioSessionRouteChangeReason
//            when (changeReason) {
//                AVAudioSessionRouteChangeReasonNewDeviceAvailable -> {
//                    play()
//                }
//
//                AVAudioSessionRouteChangeReasonOldDeviceUnavailable -> {
//                    pause()
//                }
//
//                else -> return@addObserverForName
//            }
//        }
//    }

//    private fun removeAudioSessionObservers() {
//        if (sessionInterruptionObserver != null) {
//            NSNotificationCenter.defaultCenter.removeObserver(sessionInterruptionObserver!!)
//            sessionInterruptionObserver = null
//        }
//        if (sessionRouteChangeObserver != null) {
//            NSNotificationCenter.defaultCenter.removeObserver(sessionRouteChangeObserver!!)
//            sessionRouteChangeObserver = null
//        }
//    }

    actual fun initializePlayer() {
        setUpAudioSession()
        player.setOnStateChangeListener { state ->
            _playbackState.update {
                it.copy(playerState = state)
            }
        }
        player.setOnFinishListener {
            onPlayerEvent(OMPlayerEvent.Next)
        }
        player.setOnErrorListener { errorMsg ->
            _playbackState.update {
                it.copy(errorMessage = errorMsg)
            }
        }
//        setupNotificationObservers()
        nowPlayingInfoCenter = MPNowPlayingInfoCenter.defaultCenter()
        remoteCommandCenter = MPRemoteCommandCenter.sharedCommandCenter()

        observeAudioSession()
        setupRemoteCommands()
    }

    // --- Player ---
    actual fun setPlayerItem(musics: List<Music>) {
        _queueState.update { it.copy(playerQueue = musics) }
        pause()

        setCurrentMusic(musics[0])
        play()
    }


    private fun setCurrentMusic(music: Music) {
        clearProgress()
        _queueState.update { it.copy(currentMusic = music) }

        val sourceUrl = music.source

        player.setSource(sourceUrl)
        setNowPlayingInfo()
        if (sourceUrl.endsWith(".ogg")) {
            _playbackState.update {
                it.copy(
                    playerState = OMPlayerState.Error,
                    errorMessage = "Unsupported audio format"
                )
            }
        }
    }

    actual fun addToQueue(music: Music) {
        _queueState.update { it.copy(playerQueue = it.playerQueue + music) }
    }

    actual fun removeFromQueue(index: Int) {
        if (index in _queueState.value.playerQueue.indices) {
            _queueState.update {
                it.copy(
                    playerQueue = it.playerQueue.toMutableList().apply { removeAt(index) }
                )
            }
        }
    }

    private fun play() {
        activateAudioSession()
        player.play()

        nowPlayingInfo[MPNowPlayingInfoPropertyPlaybackRate] = 1.0
        nowPlayingInfo[MPNowPlayingInfoPropertyElapsedPlaybackTime] =
            player.getCurrentPosition()
        nowPlayingInfoCenter.nowPlayingInfo = nowPlayingInfo
    }

    private fun pause() {
        player.pause()
        deactivateAudioSession()

        nowPlayingInfo[MPNowPlayingInfoPropertyPlaybackRate] = 0.0
        nowPlayingInfo[MPNowPlayingInfoPropertyElapsedPlaybackTime] =
            player.getCurrentPosition()
        nowPlayingInfoCenter.nowPlayingInfo = nowPlayingInfo
    }

    private fun togglePlayPause() {
        if (player.isPlaying()) {
            pause()
        } else {
            play()
        }
    }

    private fun seekToPosition(progress: Float) {
        player.seekTo(progress * player.getDuration())
    }

    actual fun updateProgress() {
        _playbackState.update {
            val currentTime = player.getCurrentPosition()
            val currentTimeInSeconds = if (currentTime.isNaN()) 0.0.seconds else currentTime.seconds
            val duration = player.getDuration()
            val durationInSeconds = if (duration.isNaN()) 0.0.seconds else duration.seconds
            updatePlayBackMetadata(duration, currentTime)
            it.copy(
                duration = durationInSeconds,
                progress = OMPlayerPlaybackState.Progress(
                    progress = (currentTimeInSeconds / durationInSeconds).toFloat(),
                    time = currentTimeInSeconds,
                    timestamp = Clock.System.now().toEpochMilliseconds(),
                ),
            )
        }
    }

    actual fun onPlayerEvent(
        event: OMPlayerEvent,
        selectedAudioIndex: Int
    ) {
        val playQueue = _queueState.value.playerQueue
        val currentMusicIndex = playQueue.indexOf(_queueState.value.currentMusic)
        when (event) {
            OMPlayerEvent.PlayPause -> {
                togglePlayPause()
            }

            OMPlayerEvent.Previous -> {
                if (playQueue.isNotEmpty() && currentMusicIndex > 0) {
                    setCurrentMusic(playQueue[currentMusicIndex - 1])
                }
            }

            OMPlayerEvent.Next -> {
                if (playQueue.isNotEmpty() && currentMusicIndex < playQueue.size - 1) {
                    setCurrentMusic(playQueue[currentMusicIndex + 1])
                }
            }

            OMPlayerEvent.SelectedAudioChange -> {
                if (selectedAudioIndex in playQueue.indices) {
                    setCurrentMusic(playQueue[selectedAudioIndex])
                }
            }

            is OMPlayerEvent.SeekTo -> {
                seekToPosition(event.progress)
            }
        }
    }

    private fun seekToInitialTime() {
        player.seekTo(0.0)
    }

    actual fun releasePlayer() {
        player.release()
        seekToInitialTime()
        deactivateAudioSession()
        deactivateAllRemoteCommands()
//        removeAudioSessionObservers()
//        removeCurrentItemObservers()
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
            println("Audio session set to active: $active")
        } catch (e: Exception) {
            println("Failed to update the audio session active to $active")
        }
    }

    private fun observeAudioSession() {
        NSNotificationCenter.defaultCenter.addObserverForName(
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
                    play()
                }
            }
        }
    }

    // --- Now Playing Info ---
    private fun setNowPlayingInfo() {
        val music = _queueState.value.currentMusic ?: return

        nowPlayingInfo.clear()

        nowPlayingInfo[MPMediaItemPropertyTitle] = music.title
        nowPlayingInfo[MPMediaItemPropertyArtist] = music.artist
        nowPlayingInfo[MPMediaItemPropertyAlbumTitle] = music.creator

        // Set initial playback rate
        nowPlayingInfo[MPNowPlayingInfoPropertyPlaybackRate] =
            if (player.isPlaying()) 1.0 else 0.0

        nowPlayingInfo[MPMediaItemPropertyPlaybackDuration] = player.getDuration()
        nowPlayingInfo[MPNowPlayingInfoPropertyElapsedPlaybackTime] = player.getCurrentPosition()

        MPNowPlayingInfoCenter.defaultCenter().nowPlayingInfo = nowPlayingInfo.toMap()


        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT.toLong(), 0u)) {
            try {
                val url = URLWithString(music.coverUrl)
                if (url != null) {
                    val imageData = NSData.dataWithContentsOfURL(url)
                    if (imageData != null) {
                        val image = UIImage(data = imageData)
                        dispatch_async(dispatch_get_main_queue()) {
                            val artwork = MPMediaItemArtwork(boundsSize = image.size) { _ -> image }
                            nowPlayingInfo[MPMediaItemPropertyArtwork] = artwork
                            nowPlayingInfoCenter.nowPlayingInfo = nowPlayingInfo.toMap()
                            println("Artwork updated successfully")
                        }
                    } else {
                        println("Failed to load NSData from URL")
                    }
                } else {
                    println("Invalid URL: ${music.coverUrl}")
                }
            } catch (e: Exception) {
                println("Error loading artwork: ${e.message}")
            }
        }

        println("Setting Now Playing Info: ${nowPlayingInfo}")
        println("Music: ${music.title}, ${music.artist}, ${music.coverUrl}")
    }

    private fun updatePlayBackMetadata(duration: Double, progress: Double) {
        if (_queueState.value.currentMusic == null) return
        println("$duration $progress")
        val updatedNowPlayingInfo = nowPlayingInfoCenter.nowPlayingInfo?.toMutableMap() ?: mutableMapOf()

        // Set or update basic metadata
        updatedNowPlayingInfo[MPMediaItemPropertyTitle] = _queueState.value.currentMusic?.title ?: "Unknown Title"
        updatedNowPlayingInfo[MPMediaItemPropertyArtist] = _queueState.value.currentMusic?.artist ?: "Unknown Artist"

        updatedNowPlayingInfo[MPMediaItemPropertyPlaybackDuration] = duration
        updatedNowPlayingInfo[MPNowPlayingInfoPropertyElapsedPlaybackTime] = progress

        // Update playback rate
        updatedNowPlayingInfo[MPNowPlayingInfoPropertyPlaybackRate] =
            if (player.isPlaying()) 1.0 else 0.0

        MPNowPlayingInfoCenter.defaultCenter().nowPlayingInfo = updatedNowPlayingInfo.toMap()
        println("Setting Now Playing Info: ${updatedNowPlayingInfo}")
    }

    // --- Remote Control ---
    private fun setupRemoteCommands() {
        // Play command
        remoteCommandCenter.playCommand.enabled = true
        remoteCommandCenter.playCommand.addTargetWithHandler { _ ->
            play()
            MPRemoteCommandHandlerStatusSuccess
        }

        // Pause command
        remoteCommandCenter.pauseCommand.enabled = true
        remoteCommandCenter.pauseCommand.addTargetWithHandler { _ ->
            pause()
            MPRemoteCommandHandlerStatusSuccess
        }

        // Toggle Play/Pause command
        remoteCommandCenter.togglePlayPauseCommand.enabled = true
        remoteCommandCenter.togglePlayPauseCommand.addTargetWithHandler { _ ->
            togglePlayPause()
            MPRemoteCommandHandlerStatusSuccess
        }

        // Next track command
        remoteCommandCenter.nextTrackCommand.enabled = true
        remoteCommandCenter.nextTrackCommand.addTargetWithHandler { _ ->
            onPlayerEvent(OMPlayerEvent.Next)
            MPRemoteCommandHandlerStatusSuccess
        }

        // Previous track command
        remoteCommandCenter.previousTrackCommand.enabled = true
        remoteCommandCenter.previousTrackCommand.addTargetWithHandler { _ ->
            onPlayerEvent(OMPlayerEvent.Previous)
            MPRemoteCommandHandlerStatusSuccess
        }
    }

    private fun deactivateAllRemoteCommands() {
        remoteCommandCenter.playCommand.removeTarget(null)
        remoteCommandCenter.pauseCommand.removeTarget(null)
        remoteCommandCenter.togglePlayPauseCommand.removeTarget(null)
        remoteCommandCenter.nextTrackCommand.removeTarget(null)
        remoteCommandCenter.previousTrackCommand.removeTarget(null)
    }

    private fun clearProgress() {
        _playbackState.update {
            it.copy(
                progress = OMPlayerPlaybackState.Progress(
                    timestamp = Clock.System.now().toEpochMilliseconds()
                ),
                duration = Duration.ZERO,
            )
        }
    }
}
