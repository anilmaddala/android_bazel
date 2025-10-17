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
    api_level = 34,
)

# Kotlin rules
http_archive(
    name = "rules_kotlin",
    sha256 = "3b772976fec7bdcda1d84b9d39b176589424c047eb2175bed09aac630e50af43",
    urls = ["https://github.com/bazelbuild/rules_kotlin/releases/download/v1.9.6/rules_kotlin-v1.9.6.tar.gz"],
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
        # Kotlin stdlib
        "org.jetbrains.kotlin:kotlin-stdlib:1.9.22",
        "org.jetbrains.kotlin:kotlin-stdlib-common:1.9.22",

        # Kotlinx Coroutines
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3",
        "org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.7.3",
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3",

        # AndroidX Core
        "androidx.annotation:annotation:1.7.0",
        "androidx.core:core:1.12.0",
        "androidx.core:core-ktx:1.12.0",

        # AndroidX Lifecycle - 2.6.1 for Compose 1.4.3 compatibility
        "androidx.lifecycle:lifecycle-common:2.6.1",
        "androidx.lifecycle:lifecycle-runtime:2.6.1",
        "androidx.lifecycle:lifecycle-runtime-ktx:2.6.1",
        "androidx.lifecycle:lifecycle-viewmodel:2.6.1",
        "androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1",
        "androidx.lifecycle:lifecycle-viewmodel-savedstate:2.6.1",
        "androidx.lifecycle:lifecycle-livedata-core:2.6.1",

        # AndroidX Activity - 1.7.2 for Compose 1.4.3
        "androidx.activity:activity:1.7.2",
        "androidx.activity:activity-ktx:1.7.2",
        "androidx.activity:activity-compose:1.7.2",

        # AndroidX SavedState
        "androidx.savedstate:savedstate:1.2.1",
        "androidx.savedstate:savedstate-ktx:1.2.1",

        # Jetpack Compose - 1.4.3 (pre-multiplatform, available artifacts)
        # Compose Compiler 1.5.13 for Kotlin 1.9.23 compatibility
        "androidx.compose.compiler:compiler:1.5.13",
        "androidx.compose.runtime:runtime:1.4.3",
        "androidx.compose.runtime:runtime-saveable:1.4.3",
        "androidx.compose.ui:ui:1.4.3",
        "androidx.compose.ui:ui-geometry:1.4.3",
        "androidx.compose.ui:ui-graphics:1.4.3",
        "androidx.compose.ui:ui-text:1.4.3",
        "androidx.compose.ui:ui-unit:1.4.3",
        "androidx.compose.ui:ui-util:1.4.3",
        "androidx.compose.ui:ui-tooling-preview:1.4.3",
        "androidx.compose.foundation:foundation:1.4.3",
        "androidx.compose.foundation:foundation-layout:1.4.3",
        "androidx.compose.animation:animation:1.4.3",
        "androidx.compose.animation:animation-core:1.4.3",

        # Material 3 and Icons - 1.0.1 compatible with Compose 1.4.3
        "androidx.compose.material3:material3:1.0.1",
        "androidx.compose.material:material-icons-core:1.4.3",
        "androidx.compose.material:material-icons-extended:1.4.3",
        "androidx.compose.material:material-ripple:1.4.3",

        # Compose ViewModel integration - 2.6.1
        "androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1",
        "androidx.lifecycle:lifecycle-runtime-compose:2.6.1",

        # Navigation - 2.5.3 for Compose 1.4.3 compatibility
        "androidx.navigation:navigation-common:2.5.3",
        "androidx.navigation:navigation-common-ktx:2.5.3",
        "androidx.navigation:navigation-runtime:2.5.3",
        "androidx.navigation:navigation-runtime-ktx:2.5.3",
        "androidx.navigation:navigation-compose:2.5.3",

        # Dagger and Hilt - 2.50 matching Gradle
        "com.google.dagger:dagger:2.50",
        "com.google.dagger:dagger-compiler:2.50",
        "com.google.dagger:hilt-core:2.50",
        "com.google.dagger:hilt-android:2.50",
        "com.google.dagger:hilt-android-compiler:2.50",

        # Hilt Navigation for Compose - 1.0.0 for Compose 1.4.3
        "androidx.hilt:hilt-navigation-compose:1.0.0",

        # Javax Inject (required by Hilt)
        "javax.inject:javax.inject:1",
        "javax.annotation:javax.annotation-api:1.3.2",

        # Additional AndroidX dependencies
        "androidx.collection:collection:1.3.0",
        "androidx.collection:collection-ktx:1.3.0",
        "androidx.profileinstaller:profileinstaller:1.3.1",
        "androidx.startup:startup-runtime:1.1.1",
        "androidx.tracing:tracing:1.2.0",
        "androidx.versionedparcelable:versionedparcelable:1.1.1",
        "androidx.customview:customview:1.1.0",
        "androidx.customview:customview-poolingcontainer:1.0.0",
    ],
    fail_on_missing_checksum = False,
    repositories = [
        "https://maven.google.com",
        "https://repo1.maven.org/maven2",
    ],
    strict_visibility = False,
    version_conflict_policy = "pinned",
)
