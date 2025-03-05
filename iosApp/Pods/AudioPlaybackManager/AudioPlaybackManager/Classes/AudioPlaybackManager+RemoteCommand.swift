//
//  AudioPlaybackManager+RemoteCommand.swift
//
//  Created by LM on 2023/3/20.
//

import Foundation
import MediaPlayer

extension AudioPlaybackManager {
    
    private struct AssociatedKeys {
        static var remoteCommandCenterKey: Void?
        static var rewindRateKey: Void?
        static var fastForwardRateKey: Void?
    }
    
    // MARK: Public Properties
    
    /// Set remote control center rewind rate.
    ///
    /// Default rewind rate is `-2.0`.
    @objc
    open var remoteControlRewindRate: Float {
        get {
            return objc_getAssociatedObject(self, &AssociatedKeys.rewindRateKey) as? Float ?? -2.0
        }
        set {
            objc_setAssociatedObject(self, &AssociatedKeys.rewindRateKey, newValue, .OBJC_ASSOCIATION_RETAIN_NONATOMIC)
        }
    }
    
    /// Set remote control center fast forward rate.
    ///
    /// Default fast forword rate is `2.0`.
    @objc
    open var remoteControlFastForwardRate: Float {
        get {
            return objc_getAssociatedObject(self, &AssociatedKeys.fastForwardRateKey) as? Float ?? 2.0
        }
        set {
            objc_setAssociatedObject(self, &AssociatedKeys.fastForwardRateKey, newValue, .OBJC_ASSOCIATION_RETAIN_NONATOMIC)
        }
    }
    
    // MARK: Private Properties
    
    /// Reference of `MPRemoteCommandCenter` used to configure and
    /// setup remote control events in the application.
    private var remoteCommandCenter: MPRemoteCommandCenter {
        if let center = objc_getAssociatedObject(self, &AssociatedKeys.remoteCommandCenterKey) as? MPRemoteCommandCenter {
            return center
        }
        
        let center = MPRemoteCommandCenter.shared()
        objc_setAssociatedObject(self, &AssociatedKeys.remoteCommandCenterKey, center, .OBJC_ASSOCIATION_RETAIN_NONATOMIC)
        return center
    }
}

// MARK: - MPRemoteCommand Active/Deactive Methods

extension AudioPlaybackManager {
    
    @objc
    open func activatePlaybackCommands(_ enabled: Bool) {
        if enabled {
            remoteCommandCenter.playCommand.addTarget(self, action: #selector(handlePlayCommandEvent(_:)))
            remoteCommandCenter.pauseCommand.addTarget(self, action: #selector(handlePauseCommandEvent(_:)))
            remoteCommandCenter.stopCommand.addTarget(self, action: #selector(handleStopCommandEvent(_:)))
            remoteCommandCenter.togglePlayPauseCommand.addTarget(self, action: #selector(handleTogglePlayPauseCommandEvent(_:)))
        } else {
            remoteCommandCenter.playCommand.removeTarget(self, action: #selector(handlePlayCommandEvent(_:)))
            remoteCommandCenter.pauseCommand.removeTarget(self, action: #selector(handlePauseCommandEvent(_:)))
            remoteCommandCenter.stopCommand.removeTarget(self, action: #selector(handleStopCommandEvent(_:)))
            remoteCommandCenter.togglePlayPauseCommand.removeTarget(self, action: #selector(handleTogglePlayPauseCommandEvent(_:)))
        }
        
        remoteCommandCenter.playCommand.isEnabled = enabled
        remoteCommandCenter.pauseCommand.isEnabled = enabled
        remoteCommandCenter.stopCommand.isEnabled = enabled
        remoteCommandCenter.togglePlayPauseCommand.isEnabled = enabled
    }
    
    @objc
    open func activateNextTrackCommand(_ enabled: Bool) {
        if enabled {
            remoteCommandCenter.nextTrackCommand.addTarget(self, action: #selector(handleNextTrackCommandEvent(_:)))
        } else {
            remoteCommandCenter.nextTrackCommand.removeTarget(self, action: #selector(handleNextTrackCommandEvent(_:)))
        }
        
        remoteCommandCenter.nextTrackCommand.isEnabled = enabled
    }
    
    @objc
    open func activatePreviousTrackCommand(_ enabled: Bool) {
        if enabled {
            remoteCommandCenter.previousTrackCommand.addTarget(self, action: #selector(handlePreviousTrackCommandEvent(event:)))
        } else {
            remoteCommandCenter.previousTrackCommand.removeTarget(self, action: #selector(handlePreviousTrackCommandEvent(event:)))
        }
        
        remoteCommandCenter.previousTrackCommand.isEnabled = enabled
    }
    
    @objc
    open func activateSkipForwardCommand(_ enabled: Bool, interval: Int = 0) {
        if enabled {
            remoteCommandCenter.skipForwardCommand.preferredIntervals = [NSNumber(value: interval)]
            remoteCommandCenter.skipForwardCommand.addTarget(self, action: #selector(handleSkipForwardCommandEvent(event:)))
        } else {
            remoteCommandCenter.skipForwardCommand.removeTarget(self, action: #selector(handleSkipForwardCommandEvent(event:)))
        }
        
        remoteCommandCenter.skipForwardCommand.isEnabled = enabled
    }
    
    @objc
    open func activateSkipBackwardCommand(_ enabled: Bool, interval: Int = 0) {
        if enabled {
            remoteCommandCenter.skipBackwardCommand.preferredIntervals = [NSNumber(value: interval)]
            remoteCommandCenter.skipBackwardCommand.addTarget(self, action: #selector(handleSkipBackwardCommandEvent(event:)))
        } else {
            remoteCommandCenter.skipBackwardCommand.removeTarget(self, action: #selector(handleSkipBackwardCommandEvent(event:)))
        }
        
        remoteCommandCenter.skipBackwardCommand.isEnabled = enabled
    }
    
    @objc
    open func activateSeekForwardCommand(_ enabled: Bool) {
        if enabled {
            remoteCommandCenter.seekForwardCommand.addTarget(self, action: #selector(handleSeekForwardCommandEvent(event:)))
        } else {
            remoteCommandCenter.seekForwardCommand.removeTarget(self, action: #selector(handleSeekForwardCommandEvent(event:)))
        }
        
        remoteCommandCenter.seekForwardCommand.isEnabled = enabled
    }
    
    @objc
    open func activateSeekBackwardCommand(_ enabled: Bool) {
        if enabled {
            remoteCommandCenter.seekBackwardCommand.addTarget(self, action: #selector(handleSeekBackwardCommandEvent(event:)))
        } else {
            remoteCommandCenter.seekBackwardCommand.removeTarget(self, action: #selector(handleSeekBackwardCommandEvent(event:)))
        }
        
        remoteCommandCenter.seekBackwardCommand.isEnabled = enabled
    }
    
    @objc
    open func activateChangePlaybackPositionCommand(_ enabled: Bool) {
        if enabled {
            remoteCommandCenter.changePlaybackPositionCommand.addTarget(self, action: #selector(handleChangePlaybackPositionCommandEvent(event:)))
        } else {
            remoteCommandCenter.changePlaybackPositionCommand.removeTarget(self, action: #selector(handleChangePlaybackPositionCommandEvent(event:)))
        }
        
        remoteCommandCenter.changePlaybackPositionCommand.isEnabled = enabled
    }
    
    @objc
    open func deactivateAllRemoteCommands() {
        activatePlaybackCommands(false)
        activateNextTrackCommand(false)
        activatePreviousTrackCommand(false)
        activateSkipForwardCommand(false)
        activateSkipBackwardCommand(false)
        activateSeekForwardCommand(false)
        activateSeekBackwardCommand(false)
        activateChangePlaybackPositionCommand(false)
    }
}

// MARK: - MPRemoteCommand handler methods.

extension AudioPlaybackManager {
    
    // MARK: Playback Command Handlers
    
    @objc private func handlePauseCommandEvent(_ event: MPRemoteCommandEvent) -> MPRemoteCommandHandlerStatus {
        pause()
        
        return .success
    }
    
    @objc private func handlePlayCommandEvent(_ event: MPRemoteCommandEvent) -> MPRemoteCommandHandlerStatus {
        play()
        
        return .success
    }
    
    @objc private func handleStopCommandEvent(_ event: MPRemoteCommandEvent) -> MPRemoteCommandHandlerStatus {
        stop()
        
        return .success
    }
    
    @objc private func handleTogglePlayPauseCommandEvent(_ event: MPRemoteCommandEvent) -> MPRemoteCommandHandlerStatus {
        togglePlayPause()
        
        return .success
    }
    
    // MARK: Track Changing Command Handlers
    
    @objc private func handleNextTrackCommandEvent(_ event: MPRemoteCommandEvent) -> MPRemoteCommandHandlerStatus {
        if playerItem != nil {
            switchNext()

            return .success
        } else {
            return .noSuchContent
        }
    }
    
    @objc private func handlePreviousTrackCommandEvent(event: MPRemoteCommandEvent) -> MPRemoteCommandHandlerStatus {
        if playerItem != nil {
            switchPrevious()

            return .success
        } else {
            return .noSuchContent
        }
    }
    
    // MARK: Skip Interval Command Handlers
    
    @objc private func handleSkipForwardCommandEvent(event: MPSkipIntervalCommandEvent) -> MPRemoteCommandHandlerStatus {
        skipForward(event.interval)
        
        return .success
    }
    
    @objc private func handleSkipBackwardCommandEvent(event: MPSkipIntervalCommandEvent) -> MPRemoteCommandHandlerStatus {
        skipBackward(event.interval)
        
        return .success
    }
    
    // MARK: Seek Command Handlers
    
    @objc private func handleSeekForwardCommandEvent(event: MPSeekCommandEvent) -> MPRemoteCommandHandlerStatus {
        switch event.type {
        case .beginSeeking:
            beginFastForward(rate: remoteControlFastForwardRate)
        case .endSeeking:
            endRewindFastForward()
        @unknown default:
            fatalError()
        }
        return .success
    }
    
    @objc private func handleSeekBackwardCommandEvent(event: MPSeekCommandEvent) -> MPRemoteCommandHandlerStatus {
        switch event.type {
        case .beginSeeking:
            beginRewind(rate: remoteControlRewindRate)
        case .endSeeking:
            endRewindFastForward()
        @unknown default:
            fatalError()
        }
        return .success
    }
    
    @objc private func handleChangePlaybackPositionCommandEvent(event: MPChangePlaybackPositionCommandEvent) -> MPRemoteCommandHandlerStatus {
        seekToPositionTime(event.positionTime)
        
        return .success
    }
}
