import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    init() {
        MainViewControllerKt.startKoin()
    }

	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}