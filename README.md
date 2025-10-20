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

- **UI**: Jetpack Compose 1.4.3 (Material 3)
- **Dependency Injection**: Hilt with custom Dagger fork (KAPT)
- **Architecture**: MVVM (Model-View-ViewModel)
- **Navigation**: Jetpack Navigation Compose
- **Language**: Kotlin 1.8.21
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

- **rules_kotlin**: 1.8.1
- **rules_android**: 0.1.1
- **rules_java**: 6.5.0 (required for JDK 17 toolchain)
- **rules_jvm_external**: 4.5

### Language & Compiler

- **Kotlin**: 1.8.21
- **Kotlin Compiler**: kotlinc 1.8.21
- **JVM Target**: Java 11
- **JDK**: 17 (via rules_java toolchains)

### Jetpack Compose

- **Compose UI**: 1.4.3
- **Compose Compiler**: 1.4.7 (compatible with Kotlin 1.8.21)
- **Material3**: 1.1.1
- **Material Icons**: 1.4.3

### Dependency Injection

- **Custom Dagger Fork**: pswaminathan/dagger
  - Branch: `use-generated-class-instead-of-superclass`
  - Generates `Hilt_*` classes for Bazel compatibility
  - Requires special annotation syntax (see below)

## Dependencies

### Core AndroidX
- androidx.core:core-ktx:1.12.0
- androidx.lifecycle:lifecycle-runtime-ktx:2.6.1
- androidx.activity:activity-compose:1.8.0
- androidx.annotation:annotation:1.7.1

### Jetpack Compose
- androidx.compose.ui:ui:1.4.3
- androidx.compose.ui:ui-graphics:1.4.3
- androidx.compose.ui:ui-tooling-preview:1.4.3
- androidx.compose.material3:material3:1.1.1
- androidx.compose.material:material-icons-core:1.4.3
- androidx.compose.material:material-icons-extended:1.4.3
- androidx.compose.foundation:foundation:1.4.3
- androidx.compose.runtime:runtime:1.4.3
- androidx.compose.compiler:compiler:1.4.7

### Hilt (Dependency Injection)
- Custom Dagger fork (via http_archive)
- androidx.hilt:hilt-navigation-compose:1.0.0
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

1. **rules_java 6.5.0**: Sets up JDK 17 toolchain (critical for Dagger fork compatibility)
2. **rules_android 0.1.1**: Android build rules
3. **rules_kotlin 1.8.1**: Kotlin build rules with Kotlin 1.8.21
4. **Custom Dagger fork**: pswaminathan/dagger (use-generated-class-instead-of-superclass branch)
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

# Kotlin 1.8.21
RULES_KOTLIN_VERSION = "1.8.1"
kotlin_repositories(compiler_release = kotlinc_version(
    release = "1.8.21",
    sha256 = "6e43c5569ad067492d04d92c28cdf8095673699d81ce460bd7270443297e8fd7",
))

# Custom Dagger fork
http_archive(
    name = "dagger",
    sha256 = "a796141af307e2b3a48b64a81ee163d96ffbfb41a71f0ea9cf8d26f930c80ca6",
    strip_prefix = "dagger-use-generated-class-instead-of-superclass",
    urls = ["https://github.com/pswaminathan/dagger/archive/refs/heads/use-generated-class-instead-of-superclass.zip"],
)

# Compose 1.4.3 compatible with Kotlin 1.8.21
"androidx.compose.compiler:compiler:1.4.7"
"androidx.compose.ui:ui:1.4.3"
"androidx.compose.material3:material3:1.1.1"
```

#### BUILD.bazel (root)
Defines the Kotlin toolchain and Compose compiler plugin:
```python
load("@io_bazel_rules_kotlin//kotlin:core.bzl", "define_kt_toolchain", "kt_compiler_plugin")

# Kotlin toolchain
define_kt_toolchain(
    name = "kotlin_toolchain",
    api_version = "1.8",
    jvm_target = "11",
    language_version = "1.8",
)

# Compose Compiler Plugin for Kotlin 1.8.21
kt_compiler_plugin(
    name = "compose_plugin",
    id = "androidx.compose.compiler",
    target_embedded_compiler = True,
    visibility = ["//visibility:public"],
    deps = [
        "@maven//:androidx_compose_compiler_compiler",
    ],
)

# Setup Hilt Android rules from custom Dagger fork
load("@dagger//:workspace_defs.bzl", "hilt_android_rules")
hilt_android_rules()
```

#### app/BUILD.bazel
Defines the Android app target with:
```python
load("@io_bazel_rules_kotlin//kotlin:android.bzl", "kt_android_library")
load("@build_bazel_rules_android//android:rules.bzl", "android_binary")

kt_android_library(
    name = "notekeeper_lib",
    srcs = glob(["src/main/java/**/*.kt"]),
    custom_package = "com.example.notekeeper",
    manifest = "src/main/AndroidManifest.xml",
    resource_files = glob(["src/main/res/**"]),
    plugins = [
        "//:compose_plugin",  # Compose compiler
    ],
    deps = [
        "//:hilt-android",  # From custom Dagger fork
        "@maven//:androidx_hilt_hilt_navigation_compose",
        "@maven//:androidx_compose_ui_ui",
        "@maven//:androidx_compose_material3_material3",
        # ... other dependencies
    ],
)

android_binary(
    name = "notekeeper",
    manifest = "src/main/AndroidManifest.xml",
    deps = [":notekeeper_lib"],
    debug_key = "debug.keystore",
)
```

### Custom Dagger Fork - Special Hilt Annotation Syntax

The custom Dagger fork requires a different annotation syntax than standard Hilt:

**MainActivity.kt:**
```kotlin
import dagger.hilt.android.AndroidEntryPoint
import androidx.activity.ComponentActivity

// Custom fork syntax: annotation requires base class parameter
@AndroidEntryPoint(ComponentActivity::class)
class MainActivity : Hilt_MainActivity() {  // Extends generated Hilt class
    // ...
}
```

**NoteKeeperApplication.kt:**
```kotlin
import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// Custom fork syntax: annotation requires base class parameter
@HiltAndroidApp(Application::class)
class NoteKeeperApplication : Hilt_NoteKeeperApplication() {  // Extends generated Hilt class
}
```

The fork generates `Hilt_MainActivity` and `Hilt_NoteKeeperApplication` classes that your classes extend, instead of the standard Hilt approach where annotations modify the superclass.

### Critical Insights

1. **JDK 17 Required**: rules_java must be configured first to set up JDK 17 toolchain. JDK 21 will NOT work with the Dagger fork
2. **Bzlmod Must Be Disabled**: Add `common --noenable_bzlmod` to .bazelrc
3. **rules_java Order**: rules_java must be loaded BEFORE rules_android and rules_kotlin in WORKSPACE
4. **Compose Compiler Version**: Must match Kotlin version (1.4.7 for Kotlin 1.8.21)
5. **Hilt Annotation Syntax**: Custom fork requires base class parameter and extending generated `Hilt_*` classes
6. **Version Compatibility**: Kotlin 1.8.21 + Compose 1.4.3 + rules_kotlin 1.8.1 must be used together

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

**Issue**: `Expected @AndroidEntryPoint to have a value`
- **Cause**: Using standard Hilt annotation syntax with custom Dagger fork
- **Solution**: Use `@AndroidEntryPoint(ComponentActivity::class)` and extend `Hilt_MainActivity()`

**Issue**: Bzlmod conflicts with WORKSPACE configuration
- **Cause**: Bazel 7.x enables Bzlmod by default
- **Solution**: Add `common --noenable_bzlmod` to .bazelrc

**Issue**: Bazel server crashes during build
- **Cause**: Resource exhaustion with many parallel actions
- **Solution**: Use `bazel build //app:notekeeper --jobs=4` to limit parallelism

**Issue**: `kt_android_library should be loaded from //kotlin:android.bzl`
- **Cause**: Using deprecated load path
- **Note**: This is a warning and can be ignored - build succeeds

## Known Issues

- **JDK Version**: Must use JDK 17, not JDK 21 (Dagger fork uses Java 7/8 targets which are unsupported in JDK 21)
- **Load Path Warnings**: `kt_android_library should be loaded from //kotlin:android.bzl` warnings are benign
- **Java 7 Warnings**: Build shows warnings about Java 7/8 being obsolete - these are expected and harmless

## Build Verification

The app successfully builds and runs with:
- Generated Hilt components: `Hilt_MainActivity`, `Hilt_NoteKeeperApplication`
- Full Jetpack Compose UI with Material 3
- Dependency injection working correctly
- Navigation between screens functional

Build output confirms:
```
entry point element: com.example.notekeeper.MainActivity actually extends: Hilt_MainActivity
entry point element: com.example.notekeeper.NoteKeeperApplication actually extends: Hilt_NoteKeeperApplication
INFO: Build completed successfully
```

## Additional Resources

- [Custom Dagger Fork](https://github.com/pswaminathan/dagger/tree/use-generated-class-instead-of-superclass) - Fork used for Bazel Hilt support
- [rules_kotlin 1.8.1](https://github.com/bazelbuild/rules_kotlin/releases/tag/v1.8.1) - Kotlin build rules
- [rules_android 0.1.1](https://github.com/bazelbuild/rules_android) - Android build rules
- [rules_java 6.5.0](https://github.com/bazelbuild/rules_java) - Java toolchain rules
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - Official Compose documentation

## License

This is a sample project demonstrating Bazel build system support for Android apps with Jetpack Compose and Hilt using a custom Dagger fork.
