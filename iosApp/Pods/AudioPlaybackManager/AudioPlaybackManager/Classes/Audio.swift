//
//  Audio.swift
//
//  Created by LM on 2023/3/27.
//

import Foundation

@objcMembers
open class Audio: NSObject {
    
    /// Audio url.
    open var audioURL: URL
    
    public init(audioURL: URL) {
        self.audioURL = audioURL
    }
    
    /// -------------- `MPNowPlayingInfoCenter` --------------
    
    /// Set `nowPlayingInfo` using audio metadata.
    ///
    /// Default is `false`.
    open var useAudioMetadata: Bool = false
    
    // Note: If `useAudioMetadata` is set to false, then you can set it through the following properties.
    
    /// Audio name.
    open var title: String?
    /// Album name.
    open var albumName: String?
    /// Artist.
    open var artist: String?
    
    /// Artwork.
    open var artworkImage: UIImage?
    open var artworkURL: URL?
}
