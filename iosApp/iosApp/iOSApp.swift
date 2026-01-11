import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    init() {
        print("🟢 Swift: Starting Koin initialization")
        KoinInitializerKt.doInitKoinIos()
        print("🟢 Swift: Koin initialized")
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
