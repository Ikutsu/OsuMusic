//
//  AudioPlaybackManager+NowPlayingInfo.swift
//
//  Created by 刘铭 on 2023/3/25.
//

import Foundation
import MediaPlayer

extension AudioPlaybackManager {
    
    private struct AssociatedKeys {
        static var nowPlayingInfoCenterKey: Void?
        static var allowSetNowPlayingInfoKey: Void?
    }
    
    // MARK: Public Properties
    
    /// Allows to set background playback display information.
    ///
    /// Default is `false`.
    @objc
    open var allowSetNowPlayingInfo: Bool {
        get {
            return objc_getAssociatedObject(self, &AssociatedKeys.allowSetNowPlayingInfoKey) as? Bool ?? false
        }
        set {
            objc_setAssociatedObject(self, &AssociatedKeys.allowSetNowPlayingInfoKey, newValue, .OBJC_ASSOCIATION_RETAIN_NONATOMIC)
        }
    }
    
    // MARK: Private Properties
    
    /// The instance of `MPNowPlayingInfoCenter`.
    private var nowPlayingInfoCenter: MPNowPlayingInfoCenter {
        if let center = objc_getAssociatedObject(self, &AssociatedKeys.nowPlayingInfoCenterKey) as? MPNowPlayingInfoCenter {
            return center
        }
        
        let center = MPNowPlayingInfoCenter.default()
        objc_setAssociatedObject(self, &AssociatedKeys.nowPlayingInfoCenterKey, center, .OBJC_ASSOCIATION_RETAIN_NONATOMIC)
        return center
    }
}

extension AudioPlaybackManager {
    
    internal func setNowPlayingInfo() {
        guard allowSetNowPlayingInfo,
              let audio = audio,
              let item = player.currentItem
        else {
            nowPlayingInfoCenter.nowPlayingInfo = nil
            return
        }
        
        var nowPlayingInfo = nowPlayingInfoCenter.nowPlayingInfo ?? [String: Any]()
        
        if audio.useAudioMetadata {
            let asset = item.asset
            let metadataItems = asset.commonMetadata
            
            let titleKey = AVMetadataKey.commonKeyTitle
            let albumNameKey = AVMetadataKey.commonKeyAlbumName
            let artistKey = AVMetadataKey.commonKeyArtist
            
            let title = self.acquireMetadataItem(from: metadataItems, withKey: titleKey) as? String
            let album = self.acquireMetadataItem(from: metadataItems, withKey: albumNameKey) as? String
            let artist = self.acquireMetadataItem(from: metadataItems, withKey: artistKey) as? String
            
            nowPlayingInfo[MPMediaItemPropertyTitle] = title
            nowPlayingInfo[MPMediaItemPropertyAlbumTitle] = album
            nowPlayingInfo[MPMediaItemPropertyArtist] = artist
            
            nowPlayingInfoCenter.nowPlayingInfo = nowPlayingInfo
        } else {
            nowPlayingInfo[MPMediaItemPropertyAlbumTitle] = audio.albumName
            nowPlayingInfo[MPMediaItemPropertyArtist] = audio.artist
            nowPlayingInfo[MPMediaItemPropertyTitle] = audio.title
            
            nowPlayingInfoCenter.nowPlayingInfo = nowPlayingInfo
        }
        
        appendArtwork()
    }
    
    private func appendArtwork() {
        var nowPlayingInfo = self.nowPlayingInfoCenter.nowPlayingInfo ?? [String: Any]()
        
        guard let audio = audio,
              let item = player.currentItem
        else {
            nowPlayingInfo[MPMediaItemPropertyArtwork] = nil
            nowPlayingInfoCenter.nowPlayingInfo = nowPlayingInfo
            return
        }
        
        if audio.useAudioMetadata {
            let asset = item.asset
            let metadataItems = asset.commonMetadata
            
            let artworkKey = AVMetadataKey.commonKeyArtwork
            
            DispatchQueue.global().async {
                var artwork: MPMediaItemArtwork? {
                    if let artworkData = self.acquireMetadataItem(from: metadataItems, withKey: artworkKey) as? Data,
                       let image = UIImage(data: artworkData) {
                        return MPMediaItemArtwork(boundsSize: image.size) { _ in
                            return image
                        }
                    } else {
                        return nil
                    }
                }
                nowPlayingInfo[MPMediaItemPropertyArtwork] = artwork
                
                DispatchQueue.main.async {
                    self.nowPlayingInfoCenter.nowPlayingInfo = nowPlayingInfo
                }
            }
        } else {
            if let image = audio.artworkImage {
                let artwork = MPMediaItemArtwork(boundsSize: image.size) { _ in
                    return image
                }
                nowPlayingInfo[MPMediaItemPropertyArtwork] = artwork
                
                nowPlayingInfoCenter.nowPlayingInfo = nowPlayingInfo
            } else if let url = audio.artworkURL {
                if url.isFileURL {
                    var path: String {
                        if #available(iOS 16.0, *) {
                            return url.path()
                        } else {
                            return url.path
                        }
                    }
                    var artwork: MPMediaItemArtwork? {
                        if let image = UIImage(contentsOfFile: path) {
                            return MPMediaItemArtwork(boundsSize: image.size) { _ in
                                return image
                            }
                        } else {
                            return nil
                        }
                    }
                    nowPlayingInfo[MPMediaItemPropertyArtwork] = artwork
                    
                    nowPlayingInfoCenter.nowPlayingInfo = nowPlayingInfo
                } else {
                    DispatchQueue.global().async {
                        var artwork: MPMediaItemArtwork? {
                            if let data = try? Data(contentsOf: url, options: .mappedIfSafe),
                               let image = UIImage(data: data) {
                                return MPMediaItemArtwork(boundsSize: image.size) { _ in
                                    return image
                                }
                            } else {
                                return nil
                            }
                        }
                        nowPlayingInfo[MPMediaItemPropertyArtwork] = artwork
                        
                        DispatchQueue.main.async {
                            self.nowPlayingInfoCenter.nowPlayingInfo = nowPlayingInfo
                        }
                    }
                }
            } else {
                nowPlayingInfo[MPMediaItemPropertyArtwork] = nil
                
                nowPlayingInfoCenter.nowPlayingInfo = nowPlayingInfo
            }
        }
    }
    
    private func acquireMetadataItem(from metadataItems: [AVMetadataItem], withKey key: Any?, keySpace: AVMetadataKeySpace? = .common) -> (NSCopying & NSObjectProtocol)? {
        let value = AVMetadataItem.metadataItems(from: metadataItems, withKey: key, keySpace: keySpace).first?.value
        return value
    }
    
    internal func updatePlaybackMetadata() {
        guard allowSetNowPlayingInfo,
              let item = player.currentItem
        else {
            nowPlayingInfoCenter.nowPlayingInfo = nil
            return
        }
        
        var nowPlayingInfo = nowPlayingInfoCenter.nowPlayingInfo ?? [String: Any]()
        
        nowPlayingInfo[MPMediaItemPropertyPlaybackDuration] = CMTimeGetSeconds(item.duration)
        nowPlayingInfo[MPNowPlayingInfoPropertyElapsedPlaybackTime] = CMTimeGetSeconds(item.currentTime())
        nowPlayingInfo[MPNowPlayingInfoPropertyPlaybackRate] = rate
        nowPlayingInfo[MPNowPlayingInfoPropertyDefaultPlaybackRate] = rate
        
        nowPlayingInfoCenter.nowPlayingInfo = nowPlayingInfo
    }
}
