//
//  AudioPlaybackManager.swift
//
//  Created by LM on 2022/6/20.
//

import UIKit
import MediaPlayer

@objc
open class AudioPlaybackManager: NSObject {
    
    /// An enumeration of possible playback status.
    @objc
    public enum PlayStatus: Int {
        case prepare, playing, paused, stop, playCompleted, error
    }
    
    /// The single instance of `AudioPlaybackManager`.
    @objc
    public static let shared = AudioPlaybackManager()
    
    // MARK: Public Properties
    
    /// If true, auto play when item status is `readyToPlay`,
    /// otherwise, you can call `play()` when received noti `readyToPlay`.
    @objc
    open var autoPlayWhenItemReady = false
    
    /// If the playback is forcibly interrupted during audio playback,
    /// whether to continue playing automatically after the interruption ends.
    ///
    /// Whether it can be resumed depends on the interrupted app is set ... when the audio session is not active
    /// ```
    /// session.setActive(false, options: .notifyOthersOnDeactivation)
    /// ```
    /// if set, you can pass
    /// ```
    /// AVAudioSession.InterruptionOptions
    /// ```
    /// get the `shouldResume` notification.
    ///
    /// Otherwise, can not resume automatically.
    ///
    /// - Note: Forced interruption includes incoming calls, alarm clocks, other players playing...,
    ///  the system app will generally send a corresponding notification when the audio session ends,
    /// and some third apps may not take the initiative to notify.
    @objc
    open var shouldResumeWhenInterruptEnded = true
    
    /// Update periodic time to `timeObserver`.
    ///
    /// Default is
    /// ```
    /// CMTime(seconds: 1.0 / 30, preferredTimescale: CMTimeScale(NSEC_PER_SEC))
    /// ```
    @objc
    open var observeTimeInterval = CMTime(seconds: 1.0 / 30, preferredTimescale: CMTimeScale(NSEC_PER_SEC)) {
        willSet {
            if let timeObserver = timeObserver {
                player.removeTimeObserver(timeObserver)
                self.timeObserver = nil
            }
        }
        didSet {
            timeObserver = player.addPeriodicTimeObserver(forInterval: observeTimeInterval, queue: .main) { [weak self] time in
                guard let self = self else { return }
                guard !self.isSeeking else { return }
                
                let playTime = CMTimeGetSeconds(time)
                var progress: Float {
                    let duration = self.duration
                    if duration == 0 {
                        return 0
                    }
                    return Float(playTime / duration)
                }
                self.playTime = playTime
                self.progress = progress
            }
        }
    }
    
    @objc
    open var isMuted: Bool {
        get { return player.isMuted }
        set { player.isMuted = newValue }
    }
    
    @objc
    open var volume: Float {
        get { return player.volume }
        set { player.volume = newValue }
    }
    
    /// The `playStatus` that the internal `AVPlayer` is in.
    ///
    /// This is marked as `dynamic` so that this property can be observed using KVO.
    @objc dynamic
    public private(set) var playStatus: PlayStatus = .prepare
    
    /// The current play time in seconds.
    ///
    /// This is marked as `dynamic` so that this property can be observed using KVO.
    @objc dynamic
    public private(set) var playTime: Float64 = 0
    
    /// The total duration in seconds.
    ///
    /// This is marked as `dynamic` so that this property can be observed using KVO.
    @objc dynamic
    public private(set) var duration: Float64 = 0
    
    /// The loaded time ranges in seconds.
    ///
    /// This is marked as `dynamic` so that this property can be observed using KVO.
    @objc dynamic
    public private(set) var loadedTime: Float64 = 0
    
    /// The progress for the playback.
    ///
    /// This is marked as `dynamic` so that this property can be observed using KVO.
    @objc dynamic
    public private(set) var progress: Float = 0
    
    /// The rate for the player.
    ///
    /// This is marked as `dynamic` so that this property can be observed using KVO.
    @objc dynamic
    public private(set) var rate: Float = 0
    
    /// The logger state.
    @objc
    open var logEnabled: Bool = true {
        didSet {
            logger.enabled = logEnabled
        }
    }
    
    /// The log severity level.
    @objc
    open var logLevel: LogLevel = .info {
        didSet {
            logger.minLevel = logLevel
        }
    }
    
    // MARK: Internal Properties
    
    internal let player = AVPlayer()
    
    internal private(set) var playerItem: AVPlayerItem? {
        willSet {
            guard let playerItem = playerItem else { return }
            
            playerItem.removeObserver(self, forKeyPath: #keyPath(AVPlayerItem.status), context: nil)
            playerItem.removeObserver(self, forKeyPath: #keyPath(AVPlayerItem.loadedTimeRanges), context: nil)
            
            NotificationCenter.default.removeObserver(self, name: .AVPlayerItemDidPlayToEndTime, object: playerItem)
        }
        didSet {
            guard let playerItem = playerItem else { return }
            
            // AVPlayerItem status.
            playerItem.addObserver(self, forKeyPath: #keyPath(AVPlayerItem.status), options: [.new], context: nil)
            // AVPlayerItem loadedTimeRanges.
            playerItem.addObserver(self, forKeyPath: #keyPath(AVPlayerItem.loadedTimeRanges), options: [.new], context: nil)
            
            // Did play to end time.
            NotificationCenter.default.addObserver(self, selector: #selector(playerItemDidPlayToEndTime(_:)), name: .AVPlayerItemDidPlayToEndTime, object: playerItem)
        }
    }
    
    /// Record `setupItem(_:beginTime:)` item.
    internal private(set) var audio: Audio?
    
    /// The logger.
    internal lazy var logger = AudioLogger()
    
    // MARK: Private Properties
    
    /// A periodic time observer to keep `playTime` and `progress` up to date.
    private var timeObserver: Any?
    
    /// Record `setupItem(_:beginTime:)` beginTime.
    private var beginTime: TimeInterval = 0
    
    /// Is skipping playing.
    ///
    /// `playTime` and `progress` will stop updating while the seek operation is running.
    private var isSeeking: Bool = false
    
// MARK: - Life Cycle
    
    public override init() {
        super.init()
        setupPlayer()
        addNotiObserver()
    }
    
    deinit {
        if let playerItem = playerItem {
            playerItem.removeObserver(self, forKeyPath: #keyPath(AVPlayerItem.status))
            playerItem.removeObserver(self, forKeyPath: #keyPath(AVPlayerItem.loadedTimeRanges))
            self.playerItem = nil
        }
        
        if let timeObserver = timeObserver {
            player.removeTimeObserver(timeObserver)
            self.timeObserver = nil
        }
        player.removeObserver(self, forKeyPath: #keyPath(AVPlayer.currentItem), context: nil)
        player.removeObserver(self, forKeyPath: #keyPath(AVPlayer.rate), context: nil)
        
        NotificationCenter.default.removeObserver(self)
    }
}

// MARK: - Playback Control Methods.

extension AudioPlaybackManager {
    
    @objc
    open func setupItem(_ audio: Audio, beginTime: TimeInterval = 0.0) {
        self.audio = audio
        self.beginTime = beginTime
        
        let url = audio.audioURL
        if !url.isFileURL, cacheEnabled {
            playerItem = generateCachePlayerItem(withURL: url)
        } else {
            playerItem = AVPlayerItem(asset: AVAsset(url: url))
        }
        player.replaceCurrentItem(with: playerItem)
        
        playStatus = .prepare
    }
    
    @objc
    open func play() {
        guard playerItem != nil else { return }
        
        player.play()
        playStatus = .playing
    }
    
    @objc
    open func pause() {
        guard playerItem != nil else { return }
        
        player.pause()
        playStatus = .paused
    }
    
    @objc
    open func togglePlayPause() {
        guard playerItem != nil else { return }
        
        if player.rate == 0 {
            play()
        } else {
            pause()
        }
    }
    
    @objc
    open func stop() {
        guard playerItem != nil else { return }
        
        player.pause()
        player.replaceCurrentItem(with: nil)
        
        playStatus = .stop
    }
    
    @objc
    open func switchNext() {
        guard playerItem != nil else { return }
        
        respondNextTrackCallback()
    }
    
    @objc
    open func switchPrevious() {
        guard playerItem != nil else { return }
        
        respondPreviousTrackCallback()
    }
    
    @objc
    open func skipForward(_ timeInterval: TimeInterval) {
        guard playerItem != nil else { return }
        
        let currentTime = player.currentTime()
        let offset = CMTime(seconds: timeInterval, preferredTimescale: 1)
        
        let newTime = CMTimeAdd(currentTime, offset)
        seek(to: newTime)
    }
    
    @objc
    open func skipBackward(_ timeInterval: TimeInterval) {
        guard playerItem != nil else { return }
        
        let currentTime = player.currentTime()
        let offset = CMTime(seconds: timeInterval, preferredTimescale: 1)
        
        let newTime = CMTimeSubtract(currentTime, offset)
        seek(to: newTime)
    }
    
    @objc
    open func seekToPositionTime(_ positionTime: TimeInterval) {
        guard playerItem != nil else { return }
        
        let newTime = CMTime(seconds: positionTime, preferredTimescale: 1)
        seek(to: newTime)
    }
    
    @objc
    open func seekToProgress(_ value: Float) {
        guard value >= 0, value <= 1 else { return }
        guard let item = playerItem else { return }

        let duration = CMTimeGetSeconds(item.duration)
        let positionTime = duration * Double(value)
        seekToPositionTime(positionTime)
    }
    
    @objc
    open func beginRewind(rate: Float = -2.0) {
        guard playerItem != nil else { return }
        
        player.rate = rate
    }
    
    @objc
    open func beginFastForward(rate: Float = 2.0) {
        guard playerItem != nil else { return }
        
        player.rate = rate
    }
    
    @objc
    open func endRewindFastForward() {
        guard playerItem != nil else { return }
        
        player.rate = 1.0
    }
}

// MARK: - Private Methods

extension AudioPlaybackManager {
    
    private func setupPlayer() {
        // Set default periodic time to observer.
        let observeTimeInterval = self.observeTimeInterval
        self.observeTimeInterval = observeTimeInterval
        
        player.addObserver(self, forKeyPath: #keyPath(AVPlayer.currentItem), options: [.new], context: nil)
        player.addObserver(self, forKeyPath: #keyPath(AVPlayer.rate), options: [.new], context: nil)
    }
    
    private func seekToBeginTimeWhenItemReady() {
        guard let item = playerItem else { return }
        
        let time = CMTime(seconds: beginTime, preferredTimescale: item.duration.timescale)
        player.seek(to: time, toleranceBefore: .zero, toleranceAfter: .zero) { [weak self] _ in
            guard let self = self else { return }
            self.respondReadyToPlayCallback()
            
            if self.autoPlayWhenItemReady {
                self.play()
            } else {
                // Wait for call `play()`.
            }
        }
    }
    
    private func seek(to time: CMTime) {
        guard player.currentItem != nil else { return }
        
        guard time.isValid, time.isNumeric else {
            logger.warning(" [DEBUG] Seek time: \(time) is invalid or infinity!!!")
            return
        }
        
        isSeeking = true
        player.seek(to: time, toleranceBefore: .zero, toleranceAfter: .zero) { [weak self] _ in
            guard let self = self else { return }
            
            self.isSeeking = false
            
            self.updatePlaybackMetadata()
        }
    }
}

// MARK: - Key-Value Observing Method

extension AudioPlaybackManager {
    
    open override func observeValue(forKeyPath keyPath: String?, of object: Any?, change: [NSKeyValueChangeKey : Any]?, context: UnsafeMutableRawPointer?) {
        if keyPath == #keyPath(AVPlayer.currentItem) {
            let currentItem = player.currentItem
            logger.info(" [DEBUG] Observe player item: \(String(describing: currentItem))")
            
            if currentItem == nil {
                playerItem = nil
            }
            
            setNowPlayingInfo()
        } else if keyPath == #keyPath(AVPlayer.rate) {
            let rate = player.rate
            logger.info(" [DEBUG] Observe player rate: \(rate)")
            
            self.rate = rate
            
            updatePlaybackMetadata()
        } else if keyPath == #keyPath(AVPlayerItem.status) {
            guard let playerItem = object as? AVPlayerItem else { return }
            
            let status = playerItem.status
            let duration = CMTimeGetSeconds(playerItem.duration)
            logger.info(" [DEBUG] Observe player item status: \(status), duration: \(duration)")
            
            switch status {
            case .unknown: break
            case .readyToPlay:
                self.duration = duration
                
                seekToBeginTimeWhenItemReady()
            case .failed:
                playStatus = .error
            @unknown default:
                fatalError()
            }
        } else if keyPath == #keyPath(AVPlayerItem.loadedTimeRanges) {
            guard let playerItem = object as? AVPlayerItem else { return }
            
            let loadedTime = calculateLoadedTime(for: playerItem)
            logger.info(" [DEBUG] Observe player item loaded time: \(loadedTime)")
            self.loadedTime = loadedTime
            updatePlaybackMetadata()
        } else {
            super.observeValue(forKeyPath: keyPath, of: object, change: change, context: context)
        }
    }
    
    private func calculateLoadedTime(for playerItem: AVPlayerItem) -> Float64 {
        let loadedTimeRanges = playerItem.loadedTimeRanges
        guard let timeRangeValue = loadedTimeRanges.first?.timeRangeValue else {
            return 0
        }
        
        let start = CMTimeGetSeconds(timeRangeValue.start)
        let duration = CMTimeGetSeconds(timeRangeValue.duration)
        let result = start + duration
        return result
    }
}

// MARK: - Notification Observing Methods

extension AudioPlaybackManager {
    
    private func addNotiObserver() {
        // interruption
        NotificationCenter.default.addObserver(self, selector: #selector(sessionInterruption(_:)), name: AVAudioSession.interruptionNotification, object: nil)
        // Route changed.
        NotificationCenter.default.addObserver(self, selector: #selector(routeChange(_:)), name: AVAudioSession.routeChangeNotification, object: nil)
    }
    
    @objc private func playerItemDidPlayToEndTime(_ noti: NSNotification) {
        guard let playerItem = noti.object as? AVPlayerItem else { return }
        logger.info(" [DEBUG] Player item did play to end time noti: \(noti)")
        
        let duration = CMTimeGetSeconds(playerItem.duration)
        // Sync play time to end time.
        playTime = duration
        
        playStatus = .playCompleted
    }
    
    @objc private func sessionInterruption(_ noti: NSNotification) {
        guard let userInfo = noti.userInfo else { return }
        logger.info(" [DEBUG] Audio interruption noti: \(noti)")
        
        if let typeValue = userInfo[AVAudioSessionInterruptionTypeKey] as? NSNumber,
           let type = AVAudioSession.InterruptionType(rawValue: typeValue.uintValue) {
            switch type {
            case .began:
                pause()
            case .ended:
                if shouldResumeWhenInterruptEnded,
                   let optionValue = userInfo[AVAudioSessionInterruptionOptionKey] as? NSNumber {
                    let options = AVAudioSession.InterruptionOptions(rawValue: optionValue.uintValue)
                    if options.contains(.shouldResume) {
                        play()
                    }
                }
            @unknown default:
                fatalError()
            }
        }
    }
    
    @objc private func routeChange(_ noti: NSNotification) {
        guard let userInfo = noti.userInfo else { return }
        logger.info(" [DEBUG] Route change noti: \(noti)")
        
        guard let value = userInfo[AVAudioSessionRouteChangeReasonKey] as? NSNumber,
              let reason = AVAudioSession.RouteChangeReason(rawValue: value.uintValue)
        else { return }
        
        switch reason {
        case .newDeviceAvailable:
            play()
        case .oldDeviceUnavailable:
            pause()
        default: break
        }
    }
}
