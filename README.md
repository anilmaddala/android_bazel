# NoteKeeper - Android App with Jetpack Compose & Hilt (Bazel)

A modern Android note-taking application built with Jetpack Compose and Hilt for dependency injection, built with **Bazel**. This project demonstrates a working Bazel build configuration for Android with Jetpack Compose and Hilt using a custom Dagger fork.

## Features

- Create and delete notes
- Material Design 3 UI with Jetpack Compose
- MVVM architecture
- Dependency injection with Hilt (using KSP)
- In-memory note storage with Flow
- Navigation with Jetpack Navigation Compose
- Dark theme support

## Tech Stack

- **UI**: Jetpack Compose 1.7.5 (Material 3)
- **Dependency Injection**: Hilt with official Dagger 2.57 (KSP)
- **Architecture**: MVVM (Model-View-ViewModel)
- **Navigation**: Jetpack Navigation Compose
- **Language**: Kotlin 2.1.0
- **Build System**: Bazel 7.6.1

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

- **JDK 17** (required - JDK 21 will not work with the Dagger fork)
- Android SDK with API level 34
- **Bazel 7.6.1** (recommended) or Bazelisk
- Android device or emulator for testing

## Building the Project

### Build the APK

```bash
bazel build //app:notekeeper
```

The APK will be generated at:
```
bazel-bin/app/notekeeper.apk
bazel-bin/app/notekeeper_unsigned.apk
bazel-bin/app/notekeeper_deploy.jar
```

### Install on Device

```bash
adb install bazel-bin/app/notekeeper.apk
```

Or uninstall first if updating:
```bash
adb uninstall com.example.notekeeper
adb install bazel-bin/app/notekeeper.apk
```

### Clean Build

```bash
bazel clean
# Or full clean:
bazel clean --expunge
```

## Build System Configuration

### Bazel Version

- **Bazel**: 7.6.1
- **.bazelversion file**: Locks Bazel version to 7.6.1

### Build Rules

- **rules_kotlin**: 2.1.9
- **rules_android**: 0.1.1
- **rules_java**: 6.5.0 (required for JDK 17 toolchain)
- **rules_jvm_external**: 4.5

### Language & Compiler

- **Kotlin**: 2.1.0
- **Kotlin Compiler**: kotlinc 2.1.0
- **KSP**: 2.1.0-1.0.31
- **JVM Target**: Java 11
- **JDK**: 17 (via rules_java toolchains)

### Jetpack Compose

- **Compose UI**: 1.7.5
- **Compose Compiler**: Built into Kotlin 2.1.0
- **Material3**: 1.3.1
- **Material Icons**: 1.7.5

### Dependency Injection

- **Dagger**: Official version 2.57 with KSP support
- **Hilt**: Official version 2.57 with KSP support
- **Annotation Processing**: KSP (replacing KAPT)

## Dependencies

### Core AndroidX
- androidx.core:core-ktx:1.12.0
- androidx.lifecycle:lifecycle-runtime-ktx:2.6.1
- androidx.activity:activity-compose:1.8.0
- androidx.annotation:annotation:1.7.1

### Jetpack Compose
- androidx.compose.ui:ui:1.7.5
- androidx.compose.ui:ui-graphics:1.7.5
- androidx.compose.ui:ui-tooling-preview:1.7.5
- androidx.compose.material3:material3:1.3.1
- androidx.compose.material:material-icons-core:1.7.5
- androidx.compose.material:material-icons-extended:1.7.5
- androidx.compose.foundation:foundation:1.7.5
- androidx.compose.runtime:runtime:1.7.5

### Hilt (Dependency Injection)
- com.google.dagger:dagger:2.57
- com.google.dagger:dagger-compiler:2.57
- com.google.dagger:hilt-android:2.57
- com.google.dagger:hilt-compiler:2.57
- com.google.dagger:hilt-android-compiler:2.57
- androidx.hilt:hilt-navigation-compose:1.2.0
- javax.inject:javax.inject:1
- javax.annotation:javax.annotation-api:1.3.2

### Navigation
- androidx.navigation:navigation-compose:2.6.0
- androidx.navigation:navigation-runtime-ktx:2.6.0
- androidx.navigation:navigation-common-ktx:2.6.0

### ViewModel
- androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1
- androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1

### Coroutines
- org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3
- org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.7.3

## Architecture

The app follows MVVM architecture with clean separation of concerns:

- **UI Layer**: Jetpack Compose screens and composables
- **ViewModel Layer**: State management and business logic
- **Repository Layer**: Data access abstraction
- **Data Layer**: Data models and storage

## Bazel Setup

This project demonstrates a working Bazel build configuration for an Android app with Jetpack Compose and Hilt using a custom Dagger fork that generates Hilt component classes for Bazel compatibility.

### Bazel Project Structure

```
android_bazel_airin/
├── WORKSPACE                # Bazel workspace configuration
├── .bazelrc                 # Bazel configuration (disables Bzlmod)
├── .bazelversion           # Locks Bazel version to 7.6.1
├── BUILD.bazel             # Root BUILD file with Kotlin toolchain & Compose plugin
├── MODULE.bazel            # (Not used - Bzlmod disabled)
├── app/
│   ├── BUILD.bazel        # App module BUILD file
│   ├── debug.keystore     # Debug signing key
│   └── src/main/
│       ├── AndroidManifest.xml
│       └── java/com/example/notekeeper/
└── third_party/
    └── maven_dependencies.bzl  # (Optional, not currently used)
```

### Key Bazel Configuration Files

#### .bazelrc
Disables Bzlmod to use WORKSPACE for dependency management:
```
# Disable Bzlmod - use WORKSPACE instead
common --noenable_bzlmod

build --disk_cache=bazel-cache
build --verbose_failures

# Enable d8 merger and dexer
build --define=android_dexmerger_tool=d8_dexmerger
build --define=android_incremental_dexing_tool=d8_dexbuilder
build --define=android_standalone_dexing_tool=d8_compat_dx
```

#### .bazelversion
Locks the Bazel version:
```
7.6.1
```

#### WORKSPACE
Defines external dependencies and build rules in this order:

1. **rules_java 6.5.0**: Sets up JDK 17 toolchain
2. **rules_android 0.1.1**: Android build rules
3. **rules_kotlin 2.1.9**: Kotlin build rules with Kotlin 2.1.0 and KSP 2.1.0-1.0.31
4. **Official Dagger 2.57**: With KSP annotation processing support
5. **rules_jvm_external 4.5**: Maven dependency management
6. **Android SDK**: API level 34

Key configuration:
```python
# Java toolchain - MUST come first
RULES_JAVA_VERSION = "6.5.0"
http_archive(
    name = "rules_java",
    sha256 = "160d1ebf33763124766fb35316329d907ca67f733238aa47624a8e3ff3cf2ef4",
    urls = ["https://github.com/bazelbuild/rules_java/releases/download/{v}/rules_java-{v}.tar.gz".format(v = RULES_JAVA_VERSION)],
)
load("@rules_java//java:repositories.bzl", "rules_java_dependencies", "rules_java_toolchains")
rules_java_dependencies()
rules_java_toolchains()  # Sets up JDK 17

# Kotlin 2.1.0 with KSP support
RULES_KOTLIN_VERSION = "2.1.9"
KOTLIN_VERSION = "2.1.0"
KSP_VERSION = "2.1.0-1.0.31"

http_archive(
    name = "rules_kotlin",
    sha256 = "21b2b350f4856000bd7e3eb55befe37219b237fb37cc3ba272588c7eee4b4cea",
    urls = ["https://github.com/bazelbuild/rules_kotlin/releases/download/v%s/rules_kotlin-v%s.tar.gz" % (RULES_KOTLIN_VERSION, RULES_KOTLIN_VERSION)],
)

load("@rules_kotlin//kotlin:repositories.bzl", "kotlin_repositories")
kotlin_repositories()

# Official Dagger 2.57 with KSP support
DAGGER_VERSION = "2.57"

# Compose 1.7.5 compatible with Kotlin 2.1.0
"androidx.compose.ui:ui:1.7.5"
"androidx.compose.material3:material3:1.3.1"
"com.google.dagger:dagger:2.57"
"com.google.dagger:hilt-android:2.57"
"com.google.dagger:hilt-compiler:2.57"
"com.google.devtools.ksp:symbol-processing-api:2.1.0-1.0.31"
```

#### BUILD.bazel (root)
Defines the Compose compiler plugin and KSP plugins for Hilt/Dagger:
```python
load("@rules_kotlin//kotlin:core.bzl", "kt_compiler_plugin", "kt_ksp_plugin")
load("@rules_jvm_external//:defs.bzl", "artifact")

# Compose Compiler Plugin for Kotlin 2.1.0
kt_compiler_plugin(
    name = "compose_plugin",
    id = "org.jetbrains.kotlin.plugin.compose",
    visibility = ["//visibility:public"],
)

# KSP Plugin for Hilt/Dagger
kt_ksp_plugin(
    name = "hilt_android_ksp",
    processor_class = "dagger.hilt.processor.internal.root.RootProcessor",
    generates_java = True,
    visibility = ["//visibility:public"],
    deps = [
        artifact("com.google.dagger:hilt-compiler"),
        artifact("com.google.dagger:hilt-android-compiler"),
    ],
)

# KSP Plugin for Dagger
kt_ksp_plugin(
    name = "dagger_ksp",
    processor_class = "dagger.internal.codegen.KspComponentProcessor",
    generates_java = True,
    visibility = ["//visibility:public"],
    deps = [
        artifact("com.google.dagger:dagger-compiler"),
    ],
)
```

#### app/BUILD.bazel
Defines the Android app target with KSP plugins:
```python
load("@rules_kotlin//kotlin:android.bzl", "kt_android_library")
load("@build_bazel_rules_android//android:rules.bzl", "android_binary")

kt_android_library(
    name = "notekeeper_lib",
    srcs = glob(["src/main/java/**/*.kt"]),
    custom_package = "com.example.notekeeper",
    manifest = "src/main/AndroidManifest.xml",
    resource_files = glob(["src/main/res/**"]),
    plugins = [
        "//:compose_plugin",      # Compose compiler
        "//:hilt_android_ksp",    # Hilt KSP processor
        "//:dagger_ksp",          # Dagger KSP processor
    ],
    deps = [
        "@maven//:com_google_dagger_hilt_android",
        "@maven//:com_google_dagger_dagger",
        "@maven//:androidx_hilt_hilt_navigation_compose",
        "@maven//:androidx_compose_ui_ui",
        "@maven//:androidx_compose_material3_material3",
        # ... other dependencies
    ],
)

android_binary(
    name = "notekeeper",
    custom_package = "com.example.notekeeper",
    manifest = "src/main/AndroidManifest.xml",
    deps = [":notekeeper_lib"],
)
```

### Standard Hilt Annotations with KSP

This project uses the standard official Hilt annotations with KSP annotation processing:

**MainActivity.kt:**
```kotlin
import dagger.hilt.android.AndroidEntryPoint
import androidx.activity.ComponentActivity

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    // ...
}
```

**NoteKeeperApplication.kt:**
```kotlin
import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NoteKeeperApplication : Application()
```

**AppModule.kt:**
```kotlin
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    // ...
}
```

KSP (Kotlin Symbol Processing) generates the necessary Hilt component classes automatically during the build process.

### Critical Insights

1. **KSP Instead of KAPT**: This project uses KSP (Kotlin Symbol Processing) for faster and more efficient annotation processing
2. **Official Dagger/Hilt**: Uses official Dagger 2.57 with native KSP support (no custom fork needed)
3. **Bzlmod Must Be Disabled**: Add `common --noenable_bzlmod` to .bazelrc and `build --experimental_worker_max_multiplex_instances=KotlinKsp=1`
4. **rules_java Order**: rules_java must be loaded BEFORE rules_android and rules_kotlin in WORKSPACE
5. **Kotlin 2.1.0 Compose Integration**: Compose compiler is built into Kotlin 2.0+, configured via kt_compiler_plugin
6. **Version Compatibility**: Kotlin 2.1.0 + Compose 1.7.5 + rules_kotlin 2.1.9 + KSP 2.1.0-1.0.31 work together
7. **KSP Thread Safety**: KSP requires worker limiting configuration in .bazelrc for stable builds

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

**Issue**: `error: Source option 7 is no longer supported`
- **Cause**: JDK 21 being used instead of JDK 17
- **Solution**: Ensure rules_java is loaded FIRST in WORKSPACE and calls `rules_java_toolchains()`

**Issue**: KSP annotation processing failures
- **Cause**: KSP worker thread safety issues
- **Solution**: Add `build --experimental_worker_max_multiplex_instances=KotlinKsp=1` to .bazelrc

**Issue**: Bzlmod conflicts with WORKSPACE configuration
- **Cause**: Bazel 7.x enables Bzlmod by default
- **Solution**: Add `common --noenable_bzlmod` to .bazelrc

**Issue**: Bazel server crashes during build
- **Cause**: Resource exhaustion with many parallel actions
- **Solution**: Use `bazel build //app:notekeeper --jobs=4` to limit parallelism

**Issue**: `kt_android_library should be loaded from //kotlin:android.bzl`
- **Cause**: Using legacy load path (rules_kotlin 2.1.9 uses new path)
- **Note**: This is a deprecation warning and can be ignored - build succeeds

## Known Issues

- **JDK Version**: Must use JDK 17, not JDK 21 for compatibility with Bazel's Java toolchain
- **Load Path Warnings**: `kt_android_library should be loaded from //kotlin:android.bzl` warnings are benign deprecation notices
- **KSP Worker Limits**: KSP requires single-threaded worker configuration for stable builds (configured in .bazelrc)

## Build Verification

The app successfully builds and runs with:
- KSP-generated Hilt components using official Dagger 2.57
- Full Jetpack Compose UI with Material 3
- Dependency injection working correctly with standard Hilt annotations
- Navigation between screens functional
- Kotlin 2.1.0 with Compose 1.7.5 fully operational

Build output confirms:
```
INFO: Build completed successfully
Target //app:notekeeper up-to-date:
  bazel-bin/app/notekeeper.apk
```

## Additional Resources

- [Dagger 2.57 Release](https://github.com/google/dagger/releases/tag/dagger-2.57) - Official Dagger release with KSP support
- [KSP Documentation](https://kotlinlang.org/docs/ksp-overview.html) - Kotlin Symbol Processing guide
- [rules_kotlin 2.1.9](https://github.com/bazelbuild/rules_kotlin/releases/tag/v2.1.9) - Kotlin build rules with KSP support
- [rules_android 0.1.1](https://github.com/bazelbuild/rules_android) - Android build rules
- [rules_java 6.5.0](https://github.com/bazelbuild/rules_java) - Java toolchain rules
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - Official Compose documentation
- [Hilt Android](https://developer.android.com/training/dependency-injection/hilt-android) - Official Hilt documentation

## License

This is a sample project demonstrating Bazel build system support for Android apps with Jetpack Compose and Hilt using official Dagger 2.57 with KSP annotation processing.
