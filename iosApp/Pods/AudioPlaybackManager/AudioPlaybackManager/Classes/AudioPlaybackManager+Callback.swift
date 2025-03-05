//
//  AudioPlaybackManager+Callback.swift
//
//  Created by LM on 2023/3/25.
//

import Foundation

// MARK: - Notification

extension AudioPlaybackManager {
    
    /// `setupItem(_:beginTime:)` seek to assigned time finished,
    /// call `play()` after receiving notification when `autoPlayWhenItemReady = false`.
    public static let readyToPlayNotification = Notification.Name(rawValue: "kReadyToPlayNotification")
    
    /// Remote control previous track.
    public static let previousTrackNotification = Notification.Name(rawValue: "kPreviousTrackNotification")
    
    /// Remote control next track.
    public static let nextTrackNotification = Notification.Name(rawValue: "kNextTrackNotification")
}

// MARK: - Delegate

@objc
public protocol AudioPlaybackManagerDelegate {
    
    /// `setupItem(_:beginTime:)` seek to assigned time finished,
    /// call `play()` when `autoPlayWhenItemReady = false`.
    @objc
    optional func audioPlaybackManagerRreadyToPlay(_ manager: AudioPlaybackManager)
    
    /// Remote control previous track.
    @objc
    optional func audioPlaybackManagerPreviousTrack(_ manager: AudioPlaybackManager)
    
    /// Remote control next track.
    @objc
    optional func audioPlaybackManagerNextTrack(_ manager: AudioPlaybackManager)
}

extension AudioPlaybackManager {
    
    private struct AssociatedKeys {
        static var delegatesKey: Void?
    }
    
    // MARK: Private Properties
    
    private var delegates: NSHashTable<AnyObject> {
        if let delegates = objc_getAssociatedObject(self, &AssociatedKeys.delegatesKey) as? NSHashTable<AnyObject> {
            return delegates
        }
        
        let delegates = NSHashTable<AnyObject>(options: .weakMemory)
        objc_setAssociatedObject(self, &AssociatedKeys.delegatesKey, delegates, .OBJC_ASSOCIATION_RETAIN_NONATOMIC)
        return delegates
    }
    
    // MARK: Public Methods
    
    @objc
    open func addDelegate(_ delegate: AudioPlaybackManagerDelegate) {
        if !delegates.contains(delegate) {
            delegates.add(delegate)
        }
    }
    
    @objc
    open func removeDelegate(_ delegate: AudioPlaybackManagerDelegate) {
        if delegates.contains(delegate) {
            delegates.remove(delegate)
        }
    }
    
    @objc
    open func allDelegates() -> [AnyObject] {
        return delegates.allObjects
    }
    
    @objc
    open func removeAllDelegates() {
        if delegates.count > 0 {
            delegates.removeAllObjects()
        }
    }
}

// MARK: - Respond Callback Methods

extension AudioPlaybackManager {
    
    internal func respondReadyToPlayCallback() {
        let noti = Notification(
            name: AudioPlaybackManager.readyToPlayNotification,
            object: self,
            userInfo: nil
        )
        NotificationCenter.default.post(noti)
        
        allDelegates().forEach { delegate in
            if delegate.responds(to: #selector(delegate.audioPlaybackManagerRreadyToPlay(_:))) {
                delegate.audioPlaybackManagerRreadyToPlay(self)
            }
        }
    }
    
    internal func respondPreviousTrackCallback() {
        let noti = Notification(
            name: AudioPlaybackManager.previousTrackNotification,
            object: self,
            userInfo: nil
        )
        NotificationCenter.default.post(noti)
        
        allDelegates().forEach { delegate in
            if delegate.responds(to: #selector(delegate.audioPlaybackManagerPreviousTrack(_:))) {
                delegate.audioPlaybackManagerPreviousTrack(self)
            }
        }
    }
    
    internal func respondNextTrackCallback() {
        let noti = Notification(
            name: AudioPlaybackManager.nextTrackNotification,
            object: self,
            userInfo: nil
        )
        NotificationCenter.default.post(noti)
        
        allDelegates().forEach { delegate in
            if delegate.responds(to: #selector(delegate.audioPlaybackManagerNextTrack(_:))) {
                delegate.audioPlaybackManagerNextTrack(self)
            }
        }
    }
}
