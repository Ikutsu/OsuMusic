//
//  AudioLogger.swift
//  AudioPlaybackManager
//
//  Created by LM on 2024/3/6.
//

import Foundation

@objc
public enum LogLevel: Int {
    case info, warning, error

    var description: String {
        return String(describing: self).uppercased()
    }
}

extension LogLevel: Comparable {
    
    public static func < (lhs: LogLevel, rhs: LogLevel) -> Bool {
        return lhs.rawValue < rhs.rawValue
    }
}

internal class AudioLogger {
    var enabled: Bool = true
    var minLevel: LogLevel = .info
    
    private let queue = DispatchQueue(label: "com.AudioPlaybackManager.log")
    
    func info(_ items: Any..., separator: String = " ", terminator: String = "\n") {
        log(.info, items, separator, terminator)
    }
    
    func warning(_ items: Any..., separator: String = " ", terminator: String = "\n") {
        log(.warning, items, separator, terminator)
    }
    
    func error(_ items: Any..., separator: String = " ", terminator: String = "\n") {
        log(.error, items, separator, terminator)
    }
    
    private func log(_ level: LogLevel, _ items: [Any], _ separator: String, _ terminator: String) {
        guard level >= minLevel else { return }
        
        #if DEBUG
        queue.async {
            Swift.print(items, separator: separator, terminator: terminator)
        }
        #endif
    }
}
