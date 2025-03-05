//
//  Created by Ikutsu on 2025-02-25.
//  Copyright © 2025 OsuMusic. All rights reserved.
//

import ComposeApp
import AudioPlaybackManager

class IOSPlayerBridge: NativePlayerBridge{
    private var stateChangeListener: ((OMPlayerState) -> Void)?
    private var onFinnishListener: (() -> Void)?
    private var onMusicChangeListener: ((KotlinBoolean) -> Void)?
    private var onErrorListener: ((String) -> Void)?
    
    private var stateObserver: NSKeyValueObservation?
    
    init() {
        AudioPlaybackManager.shared.setActiveSession(true, activeOptions: [.notifyOthersOnDeactivation])
        AudioPlaybackManager.shared.cacheEnabled = true
        AudioPlaybackManager.shared.allowSetNowPlayingInfo = true
        
        AudioPlaybackManager.shared.activatePlaybackCommands(true)
        AudioPlaybackManager.shared.activatePreviousTrackCommand(true)
        AudioPlaybackManager.shared.activateNextTrackCommand(true)
        AudioPlaybackManager.shared.activateChangePlaybackPositionCommand(true)
        addNotificationObserver()
        
        AudioPlaybackManager.shared.logEnabled = true
        AudioPlaybackManager.shared.logLevel = .info
        
        self.stateObserver = AudioPlaybackManager.shared.observe(
            \.playStatus,
             options: [.old, .new]
        ) { object, change in
            self.stateChangeListener?(self.convertState(state: object.playStatus))
            switch object.playStatus {
            case .playCompleted:
                self.onFinnishListener?()
            case .error:
                self.onErrorListener?("Player Error")
            default:
                break
            }
        }
    }
        
    func setSource(
        music: Music
    ) {
        let audio = Audio(audioURL: URL(string: music.source)!)
        audio.artist = music.artist
        audio.title = music.title
        audio.artworkURL = URL(string: music.backgroundUrl)
        
        AudioPlaybackManager.shared.setupItem(audio, beginTime: 0.0)
    }
    
    func isPlaying() -> Bool {
        AudioPlaybackManager.shared.playStatus == AudioPlaybackManager.PlayStatus.playing
    }
    
    func play() {
        AudioPlaybackManager.shared.play()
    }
    
    func pause() {
        AudioPlaybackManager.shared.pause()
    }
    
    func seekTo(position: Double) {
        AudioPlaybackManager.shared.seekToPositionTime(position)
    }
    
    func getCurrentPosition() -> Double {
        AudioPlaybackManager.shared.playTime
    }
    
    func getDuration() -> Double {
        AudioPlaybackManager.shared.duration
    }
    
    func setOnStateChangeListener(listener: @escaping (OMPlayerState) -> Void) {
        stateChangeListener = listener
    }
    
    func setOnFinishListener(listener: @escaping () -> Void) {
        onFinnishListener = listener
    }
    
    func setOnMusicChangeListener(listener: @escaping (KotlinBoolean) -> Void) {
        onMusicChangeListener = listener
    }
    
    func setOnErrorListener(listener: @escaping (String) -> Void) {
        onErrorListener = listener
    }
    
    func convertState(state: AudioPlaybackManager.PlayStatus) -> OMPlayerState {
        switch state {
        case .prepare:
            OMPlayerState.Idle()
        case .playing:
            OMPlayerState.Playing()
        case .paused:
            OMPlayerState.Paused()
        case .stop:
            OMPlayerState.Stopped()
        case .playCompleted:
            OMPlayerState.Stopped()
        case .error:
            OMPlayerState.Error()
        }
    }
    
    func release() {
        stateObserver?.invalidate()
        AudioPlaybackManager.shared.deactivateAllRemoteCommands()
        AudioPlaybackManager.shared.setActiveSession(false, activeOptions: [.notifyOthersOnDeactivation])
    }
}

extension IOSPlayerBridge {
    private func addNotificationObserver() {
        NotificationCenter.default.addObserver(forName: AudioPlaybackManager.readyToPlayNotification, object: nil, queue: .main) { [weak self] noti in
            guard let self = self else { return }
            
            AudioPlaybackManager.shared.play()
        }
        
        NotificationCenter.default.addObserver(forName: AudioPlaybackManager.previousTrackNotification, object: nil, queue: .main) { [weak self] noti in
            guard let self = self else { return }
            
            self.onMusicChangeListener?(false)
        }
                
        NotificationCenter.default.addObserver(forName: AudioPlaybackManager.nextTrackNotification, object: nil, queue: .main) { [weak self] noti in
            guard let self = self else { return }
            
            self.onMusicChangeListener?(true)
        }
    }
}
