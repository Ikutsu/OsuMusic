//
//  AudioPlaybackManager+Cache.swift
//
//  Created by 刘铭 on 2023/3/25.
//

import Foundation
import VIMediaCache

extension AudioPlaybackManager {
    
    private struct AssociatedKeys {
        static var resouceLoaderKey: Void?
        static var cacheEnabledKey: Void?
    }
    
    // MARK: Public Properties
    
    /// Allow cache online resources and load from the cache.
    ///
    /// Default is `false`.
    @objc
    open var cacheEnabled: Bool {
        get {
            return objc_getAssociatedObject(self, &AssociatedKeys.cacheEnabledKey) as? Bool ?? false
        }
        set {
            objc_setAssociatedObject(self, &AssociatedKeys.cacheEnabledKey, newValue, .OBJC_ASSOCIATION_RETAIN_NONATOMIC)
        }
    }
    
    // MARK: Private Properties
    
    /// The instance of `VIResourceLoaderManager`.
    private var resouceLoader: VIResourceLoaderManager {
        if let resouceLoader = objc_getAssociatedObject(self, &AssociatedKeys.resouceLoaderKey) as? VIResourceLoaderManager {
            return resouceLoader
        }
        
        let resouceLoader = VIResourceLoaderManager()
        objc_setAssociatedObject(self, &AssociatedKeys.resouceLoaderKey, resouceLoader, .OBJC_ASSOCIATION_RETAIN_NONATOMIC)
        return resouceLoader
    }
}

extension AudioPlaybackManager {
    
    internal func generateCachePlayerItem(withURL url: URL) -> AVPlayerItem {
        return resouceLoader.playerItem(with: url)
    }
}
