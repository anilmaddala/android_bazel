# NoteKeeper - Android App with Jetpack Compose & Hilt

A modern Android note-taking application built with Jetpack Compose and Hilt for dependency injection. This project supports **both Gradle and Bazel** build systems with the same Compose BOM 2025.05.01.

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
- **Dependency Injection**: Hilt with KAPT (Gradle) / KAPT (Bazel)
- **Architecture**: MVVM (Model-View-ViewModel)
- **Navigation**: Jetpack Navigation Compose
- **Language**: Kotlin
- **Build Systems**: Gradle & Bazel (both supported)

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
- Android SDK with API level 35
- **For Gradle**: Gradle 8.10.2+ (included via wrapper)
- **For Bazel**: Bazelisk (recommended) or Bazel 7.0+

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

The APK will be generated at:
```
app/build/outputs/apk/debug/app-debug.apk
```

### With Bazel

Build the debug APK:
```bash
./bazelisk build //app:notekeeper_debug
```

Build the release APK:
```bash
./bazelisk build //app:notekeeper
```

Clean build:
```bash
./bazelisk clean
```

The APK will be generated at:
```
bazel-bin/app/notekeeper_debug.apk
bazel-bin/app/notekeeper_debug_unsigned.apk
bazel-bin/app/notekeeper_debug_deploy.jar
```

## Build System Configuration

### Gradle

- **Android Gradle Plugin**: 8.8.0
- **Kotlin**: 2.1.0
- **Hilt**: 2.54
- **Gradle**: 8.10.2
- **Jetpack Compose BOM**: 2025.05.01
- **Compose UI**: 1.8.1
- **Material3**: 1.3.2

### Bazel

- **rules_kotlin**: v2.1.0
- **rules_android**: 0.1.1
- **Kotlin**: 2.1.0
- **Compose Compiler Plugin**: 2.1.0
- **Jetpack Compose BOM**: 2025.05.01
- **Compose UI**: 1.8.1
- **Material3**: 1.3.2
- **Material Icons**: 1.7.8

## Dependencies

### Core
- androidx.core:core-ktx:1.15.0
- androidx.lifecycle:lifecycle-runtime-ktx:2.8.7
- androidx.activity:activity-compose:1.9.3

### Jetpack Compose
- Compose BOM 2025.05.01
- Compose UI: 1.8.1
- Material 3: 1.3.2
- Material Icons Extended: 1.7.8
- UI Tooling Preview

### Hilt (Dependency Injection)
- com.google.dagger:hilt-android:2.54
- com.google.dagger:hilt-android-compiler:2.54 (KAPT)
- androidx.hilt:hilt-navigation-compose:1.2.0

### Navigation
- androidx.navigation:navigation-compose:2.8.5

### ViewModel
- androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7

### Coroutines
- org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0

## Architecture

The app follows MVVM architecture with clean separation of concerns:

- **UI Layer**: Jetpack Compose screens and composables
- **ViewModel Layer**: State management and business logic
- **Repository Layer**: Data access abstraction
- **Data Layer**: Data models and storage

## Bazel Setup

This project demonstrates a working Bazel build configuration for an Android app with Jetpack Compose and Hilt. Both build systems (Gradle and Bazel) use the **same Compose BOM 2025.05.01**.

### Bazel Project Structure

```
android_bazel/
├── WORKSPACE                 # Bazel workspace configuration
├── BUILD.bazel              # Root BUILD file with Compose plugin
├── stability.conf           # Compose compiler stability config
├── app/
│   ├── BUILD.bazel         # App module BUILD file
│   ├── debug.keystore      # Debug signing key
│   └── src/main/...
└── bazelisk                # Bazelisk wrapper script
```

### Key Bazel Configuration Files

#### WORKSPACE
Defines external dependencies and build rules:
- **rules_kotlin v2.1.0**: Kotlin build rules with Kotlin 2.1.x support
- **rules_android 0.1.1**: Android build rules
- **rules_jvm_external**: Maven dependency management
- **Android SDK**: API level 35

Key configuration:
```python
# Kotlin 2.1.0 for Compose BOM 2025.05.01
"org.jetbrains.kotlin:kotlin-stdlib:2.1.0"

# Compose compiler MUST match Kotlin version
"org.jetbrains.kotlin:kotlin-compose-compiler-plugin-embeddable:2.1.0"

# Compose dependencies from BOM 2025.05.01
"androidx.compose.ui:ui:1.8.1"
"androidx.compose.ui:ui-android:1.8.1"  # Multiplatform artifacts
"androidx.compose.material3:material3:1.3.2"
```

#### BUILD.bazel (root)
Defines the Compose compiler plugin:
```python
kt_compiler_plugin(
    name = "compose_plugin",
    id = "androidx.compose.compiler.plugins.kotlin",
    options = {"sourceInformation": "true"},
    target_embedded_compiler = True,
    deps = ["@maven//:org_jetbrains_kotlin_kotlin_compose_compiler_plugin_embeddable"],
)
```

#### app/BUILD.bazel
Defines the Android app target with:
- **kt_android_library**: Kotlin Android library with Compose and Hilt
- **android_binary**: APK generation with signing
- **Plugins**: Hilt annotation processor + Compose compiler plugin

### Critical Insights

1. **Compose Compiler Version**: Must match Kotlin compiler version exactly (2.1.0 in both)
2. **Multiplatform Artifacts**: Use `-android` suffixed artifacts (e.g., `ui-android:1.8.1`)
3. **Material Icons**: Version 1.7.8 for BOM 2025.05.01 (not 1.8.1!)
4. **Plugin Configuration**: Use `androidx.compose.compiler.plugins.kotlin` ID with `kotlin-compose-compiler-plugin-embeddable` artifact
5. **KAPT Warning**: "Falling back to 1.9" warning is benign - Hilt works correctly

### Installing Bazelisk

**macOS/Linux**:
```bash
# Using Homebrew
brew install bazelisk

# Or download directly
chmod +x bazelisk
```

**Windows**:
```powershell
# Using Chocolatey
choco install bazelisk

# Or download bazelisk.exe
```

### Troubleshooting

**Issue**: Build fails with "couldn't find inline method"
- **Solution**: Ensure Compose compiler plugin version matches Kotlin version exactly

**Issue**: Material Icons dependency not found
- **Solution**: Check the BOM mapping - Material Icons may have different version than Compose UI

**Issue**: NoSuchMethodError during build
- **Solution**: Verify rules_kotlin version supports your Kotlin compiler version

See [BAZEL_STATUS.md](BAZEL_STATUS.md) for detailed troubleshooting and version compatibility matrix.

## Known Issues

- **Gradle**: Java 21 compatibility requires JVM arguments in gradle.properties
- **Bazel**: KAPT warning "Falling back to 1.9" can be ignored - build succeeds
- **Both**: Icon deprecation warnings for ArrowBack (use AutoMirrored version)

## License

This is a sample project demonstrating dual build system support (Gradle + Bazel) for Android apps with Jetpack Compose and Hilt.

## Additional Resources

- [BAZEL_STATUS.md](BAZEL_STATUS.md) - Detailed Bazel build status, troubleshooting, and version compatibility
- [rules_kotlin](https://github.com/bazelbuild/rules_kotlin) - Official Bazel rules for Kotlin
- [rules_android](https://github.com/bazelbuild/rules_android) - Official Bazel rules for Android
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - Official Compose documentation
