import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    init() {
        KoinInitHelper().doInitKoin(
            config: nil,
            applicationComponent: IosApplicationComponent(
                nativePlayerBridge: IOSPlayerBridge()
            )
        )
    }
    
	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}