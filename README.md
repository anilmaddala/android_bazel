# NoteKeeper - Android App with Jetpack Compose & Hilt

A modern Android note-taking application built with Jetpack Compose and Hilt for dependency injection. This project is set up with Gradle and ready to be migrated to Bazel 7.6 for building.

## Features

- Create and delete notes
- Material Design 3 UI with Jetpack Compose
- MVVM architecture
- Dependency injection with Hilt (using KSP)
- In-memory note storage with Flow
- Navigation with Jetpack Navigation Compose
- Dark theme support

## Tech Stack

- **UI**: Jetpack Compose (Material 3)
- **Dependency Injection**: Hilt with KSP (Kotlin Symbol Processing)
- **Architecture**: MVVM (Model-View-ViewModel)
- **Navigation**: Jetpack Navigation Compose
- **Language**: Kotlin
- **Build System**: Gradle (ready for Bazel migration)

## Project Structure

```
android_bazel/
├── app/
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/example/notekeeper/
│       │   ├── NoteKeeperApplication.kt
│       │   ├── MainActivity.kt
│       │   ├── data/
│       │   │   ├── model/Note.kt
│       │   │   └── repository/
│       │   │       ├── NoteRepository.kt
│       │   │       └── NoteRepositoryImpl.kt
│       │   ├── di/AppModule.kt
│       │   ├── ui/
│       │   │   ├── NoteViewModel.kt
│       │   │   ├── navigation/NavGraph.kt
│       │   │   ├── screens/
│       │   │   │   ├── NoteListScreen.kt
│       │   │   │   └── AddNoteScreen.kt
│       │   │   └── theme/
│       │   │       ├── Theme.kt
│       │   │       └── Type.kt
│       │   └── res/
│       │       └── values/
│       │           ├── strings.xml
│       │           └── themes.xml
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
└── gradlew
```

## Requirements

- JDK 17 or higher (tested with Java 21)
- Android SDK with API level 34
- Gradle 8.4+ (included via wrapper)

## Building the Project

### With Gradle

Build the debug APK:
```bash
./gradlew assembleDebug
```

Build the release APK:
```bash
./gradlew assembleRelease
```

Install on connected device:
```bash
./gradlew installDebug
```

Run tests:
```bash
./gradlew test
```

### Output

The APK will be generated at:
```
app/build/outputs/apk/debug/app-debug.apk
```

## Gradle Configuration

- **Android Gradle Plugin**: 8.3.1
- **Kotlin**: 1.9.22
- **KSP**: 1.9.22-1.0.17
- **Hilt**: 2.50
- **Jetpack Compose BOM**: 2023.10.01
- **Compose Compiler**: 1.5.8

## Dependencies

### Core
- androidx.core:core-ktx:1.12.0
- androidx.lifecycle:lifecycle-runtime-ktx:2.6.2
- androidx.activity:activity-compose:1.8.1

### Jetpack Compose
- Compose BOM 2023.10.01
- Material 3
- UI Tooling
- Icons Extended

### Hilt (Dependency Injection)
- com.google.dagger:hilt-android:2.50
- com.google.dagger:hilt-android-compiler:2.50 (KSP)
- androidx.hilt:hilt-navigation-compose:1.1.0

### Navigation
- androidx.navigation:navigation-compose:2.7.5

### ViewModel
- androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2

## Architecture

The app follows MVVM architecture with clean separation of concerns:

- **UI Layer**: Jetpack Compose screens and composables
- **ViewModel Layer**: State management and business logic
- **Repository Layer**: Data access abstraction
- **Data Layer**: Data models and storage

## Next Steps: Bazel Migration

This project is designed to be migrated to Bazel 7.6 for building. The current Gradle setup provides:

1. A working Android app with all modern dependencies
2. Kotlin with KSP (better Bazel compatibility than KAPT)
3. Clear module structure ready for Bazel WORKSPACE/MODULE setup
4. Hilt dependency injection (Bazel-compatible)

To migrate to Bazel, you'll need to:

1. Create a WORKSPACE or MODULE.bazel file
2. Add Android SDK and Kotlin rules
3. Convert build.gradle.kts to BUILD files
4. Set up Hilt for Bazel
5. Configure KSP processor for Bazel

## Known Issues

- Java 21 compatibility required JVM arguments in gradle.properties
- Switched from KAPT to KSP for better Java 17+ compatibility
- Minor SDK version warning (can be ignored)

## License

This is a sample project for demonstrating Gradle to Bazel migration.
