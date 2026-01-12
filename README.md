# Pont - Etymology Word Puzzle Game

Pont is a cross-platform word puzzle game that challenges users to discover etymological connections between words in different languages. Built with Kotlin Multiplatform, it works on Android and iOS.

##  Features

* Guess English words from words in other languages
* Able to clear progress
* Able to pick a puzzle at random
* Learn about etymology

## Tech Stack

- **Kotlin Multiplatform** (2.2.0) - Cross-platform development
- **Jetpack Compose / Compose Multiplatform** (1.8.2) - Modern declarative UI
- **MVVM Architecture** - Clean separation of concerns
- **Koin** (4.1.0) - Dependency injection
- **Room Database** (2.7.0-alpha12) - Local data persistence with KMP support
- **Kotlin Coroutines & Flow** - Asynchronous programming and reactive state management
- **kotlinx.serialization** - JSON parsing for puzzle data
- **Material Design 3** - Modern UI components and theming

## 📋 Prerequisites

### For All Platforms:
- JDK
- Android Studio
- Kotlin Multiplatform Mobile Plugin
 

### For Android:
- **Android SDK**: API Level 24-35
- **Minimum Android Version**: Android 7.0 (Nougat)
- **Target Android Version**: Android 15 (API 35)

### For iOS (Mac only):
- macOS
- Xcode
  ```bash
  xcode-select --install
  ```

## Installation & Setup

### 1. Clone the Repository
```bash
git clone https://github.com/Khanner1/pont.git
cd pont
```

### 2. Open in Android Studio
1. Launch Android Studio
2. Select **File > Open**
3. Navigate to the cloned `pont` directory
4. Click **OK**
5. Wait for Gradle sync to complete (this may take several minutes on first run)

### 3. Configure the Project

#### Verify Gradle JDK:
1. Go to `Settings/Preferences > Build, Execution, Deployment > Build Tools > Gradle`
2. Set **Gradle JDK** to JDK 11 or higher
3. Ensure **Gradle version** is 8.9 or compatible

#### Initial Build:
```bash
# Clean and build the project
./gradlew clean build

# This will download dependencies and generate necessary code
```

## Running the App

### Android

#### Via Android Studio (Recommended)
1. Connect an Android device via USB with USB debugging enabled, OR
2. Start an Android emulator (API 24 or higher)
3. Access the `MainActivity.kt` file in the `composeApp/androidMain/` folder 

### iOS (Mac only)

#### Via Xcode
1. Navigate to the `iosApp` directory
2. Open `iosApp.xcodeproj` in Xcode
3. Select a simulator (e.g., iPhone 15) or connected iOS device from the scheme menu
4. Click the **Run** button (▶) or press `Cmd + R`
5. Wait for the build to complete and the app to launch

##  Project Structure

```
pont/
├── composeApp/                    # Main application module
│   ├── src/
│   │   ├── commonMain/           # Shared code across all platforms
│   │   │   ├── kotlin/
│   │   │   │   ├── data/         # Data layer (models, repository, database)
│   │   │   │   └── ui/           # UI layer (screens, ViewModels)
│   │   │   │       ├── home/     # Home screen with puzzle carousel
│   │   │   │       ├── library/  # Puzzle library screen
│   │   │   │       ├── puzzle/   # Main puzzle gameplay screen
│   │   │   │       └── theme/    # Material Design theme
│   │   │   └── composeResources/ # Shared resources
│   │   │       └── files/        # JSON data files
│   │   ├── androidMain/          # Android-specific code
│   │   └── iosMain/              # iOS-specific code
│   ├── build.gradle.kts          # Module build configuration
│   └── schemas/                  # Room database schemas
├── iosApp/                       # iOS application wrapper
│   ├── iosApp/
│   │   ├── ContentView.swift     # iOS app entry point
│   │   └── iOSApp.swift         
│   └── iosApp.xcodeproj          # Xcode project
├── gradle/                       # Gradle wrapper and dependencies
│   ├── libs.versions.toml        # Centralized dependency versions
│   └── wrapper/
├── build.gradle.kts              # Root build configuration
├── settings.gradle.kts           # Project settings
└── README.md                     # This file
```

This project is licensed under the Apache License 2.0
