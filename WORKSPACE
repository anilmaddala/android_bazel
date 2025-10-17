workspace(name = "notekeeper")

# Android rules
load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

# rules_android
http_archive(
    name = "build_bazel_rules_android",
    sha256 = "cd06d15dd8bb59926e4d65f9003bfc20f9da4b2519985c27e190cddc8b7a7806",
    strip_prefix = "rules_android-0.1.1",
    urls = ["https://github.com/bazelbuild/rules_android/archive/v0.1.1.zip"],
)

load("@build_bazel_rules_android//android:rules.bzl", "android_sdk_repository")

android_sdk_repository(
    name = "androidsdk",
    api_level = 35,
)

# Kotlin rules - Using v2.1.0 for Kotlin 2.1.x support
http_archive(
    name = "rules_kotlin",
    sha256 = "dd32f19e73c70f32ccb9a166c615c0ca4aed8e27e72c4a6330c3523eafa1aa55",
    urls = ["https://github.com/bazelbuild/rules_kotlin/releases/download/v2.1.0/rules_kotlin-v2.1.0.tar.gz"],
)

load("@rules_kotlin//kotlin:repositories.bzl", "kotlin_repositories")
kotlin_repositories()

load("@rules_kotlin//kotlin:core.bzl", "kt_register_toolchains")
kt_register_toolchains()

# Maven dependencies
http_archive(
    name = "rules_jvm_external",
    sha256 = "b17d7388feb9bfa7f2fa09031b32707df529f26c91ab9e5d909eb1676badd9a6",
    strip_prefix = "rules_jvm_external-4.5",
    url = "https://github.com/bazelbuild/rules_jvm_external/archive/4.5.zip",
)

load("@rules_jvm_external//:defs.bzl", "maven_install")

maven_install(
    artifacts = [
        # Kotlin stdlib - 2.1.0 (matching rules_kotlin 2.1.0)
        "org.jetbrains.kotlin:kotlin-stdlib:2.1.0",
        "org.jetbrains.kotlin:kotlin-stdlib-common:2.1.0",

        # Compose Compiler Embeddable Plugin - 2.1.0 to match Kotlin 2.1.0
        "org.jetbrains.kotlin:kotlin-compose-compiler-plugin-embeddable:2.1.0",

        # Kotlinx Coroutines
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0",
        "org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.9.0",
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0",

        # AndroidX Core
        "androidx.annotation:annotation:1.9.1",
        "androidx.core:core:1.15.0",
        "androidx.core:core-ktx:1.15.0",

        # AndroidX Lifecycle - 2.8.7
        "androidx.lifecycle:lifecycle-common:2.8.7",
        "androidx.lifecycle:lifecycle-common-jvm:2.8.7",
        "androidx.lifecycle:lifecycle-runtime:2.8.7",
        "androidx.lifecycle:lifecycle-runtime-android:2.8.7",
        "androidx.lifecycle:lifecycle-runtime-ktx:2.8.7",
        "androidx.lifecycle:lifecycle-runtime-ktx-android:2.8.7",
        "androidx.lifecycle:lifecycle-viewmodel:2.8.7",
        "androidx.lifecycle:lifecycle-viewmodel-android:2.8.7",
        "androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7",
        "androidx.lifecycle:lifecycle-viewmodel-savedstate:2.8.7",
        "androidx.lifecycle:lifecycle-livedata-core:2.8.7",

        # AndroidX Activity - 1.9.3
        "androidx.activity:activity:1.9.3",
        "androidx.activity:activity-ktx:1.9.3",
        "androidx.activity:activity-compose:1.9.3",

        # AndroidX SavedState
        "androidx.savedstate:savedstate:1.2.1",
        "androidx.savedstate:savedstate-ktx:1.2.1",

        # Jetpack Compose - Using 1.8.1 (from BOM 2025.05.01) with explicit -android artifacts
        # Testing compatibility with rules_kotlin v2.1.0 + Compose compiler 2.1.0
        "androidx.compose.runtime:runtime:1.8.1",
        "androidx.compose.runtime:runtime-android:1.8.1",
        "androidx.compose.runtime:runtime-saveable:1.8.1",
        "androidx.compose.runtime:runtime-saveable-android:1.8.1",
        "androidx.compose.ui:ui:1.8.1",
        "androidx.compose.ui:ui-android:1.8.1",
        "androidx.compose.ui:ui-geometry:1.8.1",
        "androidx.compose.ui:ui-geometry-android:1.8.1",
        "androidx.compose.ui:ui-graphics:1.8.1",
        "androidx.compose.ui:ui-graphics-android:1.8.1",
        "androidx.compose.ui:ui-text:1.8.1",
        "androidx.compose.ui:ui-text-android:1.8.1",
        "androidx.compose.ui:ui-unit:1.8.1",
        "androidx.compose.ui:ui-unit-android:1.8.1",
        "androidx.compose.ui:ui-util:1.8.1",
        "androidx.compose.ui:ui-util-android:1.8.1",
        "androidx.compose.ui:ui-tooling-preview:1.8.1",
        "androidx.compose.ui:ui-tooling-preview-android:1.8.1",
        "androidx.compose.foundation:foundation:1.8.1",
        "androidx.compose.foundation:foundation-android:1.8.1",
        "androidx.compose.foundation:foundation-layout:1.8.1",
        "androidx.compose.foundation:foundation-layout-android:1.8.1",
        "androidx.compose.animation:animation:1.8.1",
        "androidx.compose.animation:animation-android:1.8.1",
        "androidx.compose.animation:animation-core:1.8.1",
        "androidx.compose.animation:animation-core-android:1.8.1",

        # Material 3 and Icons - from BOM 2025.05.01 with explicit -android artifacts
        "androidx.compose.material3:material3:1.3.2",
        "androidx.compose.material3:material3-android:1.3.2",
        # Material Icons are at 1.7.8 for BOM 2025.05.01
        "androidx.compose.material:material-icons-core:1.7.8",
        "androidx.compose.material:material-icons-core-android:1.7.8",
        "androidx.compose.material:material-icons-extended:1.7.8",
        "androidx.compose.material:material-icons-extended-android:1.7.8",
        "androidx.compose.material:material-ripple:1.7.8",
        "androidx.compose.material:material-ripple-android:1.7.8",

        # Compose ViewModel integration - 2.8.7
        "androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7",
        "androidx.lifecycle:lifecycle-viewmodel-compose-android:2.8.7",
        "androidx.lifecycle:lifecycle-runtime-compose:2.8.7",
        "androidx.lifecycle:lifecycle-runtime-compose-android:2.8.7",

        # Navigation - 2.8.5
        "androidx.navigation:navigation-common:2.8.5",
        "androidx.navigation:navigation-common-ktx:2.8.5",
        "androidx.navigation:navigation-runtime:2.8.5",
        "androidx.navigation:navigation-runtime-ktx:2.8.5",
        "androidx.navigation:navigation-compose:2.8.5",

        # Dagger and Hilt - 2.54
        "com.google.dagger:dagger:2.54",
        "com.google.dagger:dagger-compiler:2.54",
        "com.google.dagger:hilt-core:2.54",
        "com.google.dagger:hilt-android:2.54",
        "com.google.dagger:hilt-android-compiler:2.54",

        # Hilt Navigation for Compose - 1.2.0
        "androidx.hilt:hilt-navigation-compose:1.2.0",

        # Javax Inject (required by Hilt)
        "javax.inject:javax.inject:1",
        "javax.annotation:javax.annotation-api:1.3.2",

        # Additional AndroidX dependencies
        "androidx.collection:collection:1.4.4",
        "androidx.collection:collection-jvm:1.4.4",
        "androidx.collection:collection-ktx:1.4.4",
        "androidx.profileinstaller:profileinstaller:1.4.1",
        "androidx.startup:startup-runtime:1.2.0",
        "androidx.tracing:tracing:1.3.0",
        "androidx.versionedparcelable:versionedparcelable:1.2.1",
        "androidx.customview:customview:1.2.0-alpha02",
        "androidx.customview:customview-poolingcontainer:1.0.0",
        "androidx.interpolator:interpolator:1.0.0",
        "androidx.emoji2:emoji2:1.5.0",
        "androidx.emoji2:emoji2-views-helper:1.5.0",
        "androidx.arch.core:core-common:2.2.0",
        "androidx.arch.core:core-runtime:2.2.0",
    ],
    fail_on_missing_checksum = False,
    repositories = [
        "https://maven.google.com",
        "https://repo1.maven.org/maven2",
    ],
    strict_visibility = False,
    version_conflict_policy = "pinned",
)
