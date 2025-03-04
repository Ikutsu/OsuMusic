//
//  Created by Ikutsu on 2025-02-25.
//  Copyright © 2025 OsuMusic. All rights reserved.
//

import ComposeApp
import AudioStreaming

class IOSPlayerBridge: NativePlayerBridge{
    var player: AudioPlayer
    private var stateChangeListener: ((OMPlayerState) -> Void)?
    private var onFinnishListener: (() -> Void)?
    private var onErrorListener: ((String) -> Void)?
    
    init() {
        player = AudioPlayer()
        player.delegate = self
    }
        
    func setSource(
        url: String
    ) {
        player.play(url: URL(string: url)!)
    }
    
    func isPlaying() -> Bool {
        player.state == AudioPlayerState.playing
    }
    
    func play() {
        player.resume()
    }
    
    func pause() {
        player.pause()
    }
    
    func seekTo(position: Double) {
        player.seek(to: position)
    }
    
    func getCurrentPosition() -> Double {
        player.progress
    }
    
    func getDuration() -> Double {
        player.duration
    }
    
    func getPlaybackState() -> OMPlayerState {
        convertPlayerState(state: player.state)
    }
    
    private func convertPlayerState(state: AudioPlayerState) -> OMPlayerState {
        switch state {
        case .playing:
            OMPlayerState.Playing()
        case .bufferring:
            OMPlayerState.Buffering()
        case .paused:
            OMPlayerState.Paused()
        case .stopped:
            OMPlayerState.Stopped()
        case .error:
            OMPlayerState.Error()
        default:
            OMPlayerState.Idle()
        }
    }
    
    func setOnStateChangeListener(listener: @escaping (OMPlayerState) -> Void) {
        stateChangeListener = listener
    }
    
    func setOnFinishListener(listener: @escaping () -> Void) {
        onFinnishListener = listener
    }
    
    func setOnErrorListener(listener: @escaping (String) -> Void) {
        onErrorListener = listener
    }
    
    func release() {
        player.stop(clearQueue: true)
    }
}

extension IOSPlayerBridge: AudioPlayerDelegate {
    func audioPlayerDidStartPlaying(player: AudioStreaming.AudioPlayer, with entryId: AudioStreaming.AudioEntryId) {
    }
    
    func audioPlayerDidFinishBuffering(player: AudioStreaming.AudioPlayer, with entryId: AudioStreaming.AudioEntryId) {
    }
    
    func audioPlayerStateChanged(player: AudioStreaming.AudioPlayer, with newState: AudioStreaming.AudioPlayerState, previous: AudioStreaming.AudioPlayerState) {
        stateChangeListener?(convertPlayerState(state: newState))
    }
    
    func audioPlayerDidFinishPlaying(player: AudioStreaming.AudioPlayer, entryId: AudioStreaming.AudioEntryId, stopReason: AudioStreaming.AudioPlayerStopReason, progress: Double, duration: Double) {
        onFinnishListener?()
    }
    
    func audioPlayerUnexpectedError(player: AudioStreaming.AudioPlayer, error: AudioStreaming.AudioPlayerError) {
        onErrorListener?(error.errorDescription ?? "Player error")
    }
    
    func audioPlayerDidCancel(player: AudioStreaming.AudioPlayer, queuedItems: [AudioStreaming.AudioEntryId]) {
    }
    
    func audioPlayerDidReadMetadata(player: AudioStreaming.AudioPlayer, metadata: [String : String]) {
    }
    
}
