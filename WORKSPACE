workspace(name = "notekeeper")

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

# Java
RULES_JAVA_VERSION = "6.5.0"

RULES_JAVA_SHA = "160d1ebf33763124766fb35316329d907ca67f733238aa47624a8e3ff3cf2ef4"

http_archive(
    name = "rules_java",
    sha256 = RULES_JAVA_SHA,
    urls = ["https://github.com/bazelbuild/rules_java/releases/download/{v}/rules_java-{v}.tar.gz".format(v = RULES_JAVA_VERSION)],
)

load(
    "@rules_java//java:repositories.bzl",
    "rules_java_dependencies",
    "rules_java_toolchains",
)

rules_java_dependencies()

rules_java_toolchains()

# Android rules

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
    build_tools_version = "34.0.0",
)

# Kotlin rules - Using v2.1.9 with Kotlin 2.1.0 and KSP support
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

load("@rules_kotlin//kotlin:core.bzl", "kt_register_toolchains")

kt_register_toolchains()

# Dagger version - Official release with KSP support
DAGGER_VERSION = "2.57"

# Maven dependencies
http_archive(
    name = "rules_jvm_external",
    sha256 = "b17d7388feb9bfa7f2fa09031b32707df529f26c91ab9e5d909eb1676badd9a6",
    strip_prefix = "rules_jvm_external-4.5",
    url = "https://github.com/bazelbuild/rules_jvm_external/archive/4.5.zip",
)

load("@rules_jvm_external//:repositories.bzl", "rules_jvm_external_deps")
rules_jvm_external_deps()

load("@rules_jvm_external//:setup.bzl", "rules_jvm_external_setup")
rules_jvm_external_setup()

load("@rules_jvm_external//:defs.bzl", "maven_install")

maven_install(
    artifacts = [
        # Kotlin stdlib - 2.1.0 (matching rules_kotlin 2.1.9)
        "org.jetbrains.kotlin:kotlin-stdlib:2.1.0",
        "org.jetbrains.kotlin:kotlin-stdlib-common:2.1.0",

        # Compose Compiler - Built into Kotlin 2.0+, using Compose BOM for libraries
        # Note: Compose Compiler is now part of the Kotlin compiler in 2.0+

        # Kotlinx Coroutines
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3",
        "org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.7.3",
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3",

        # AndroidX Core
        "androidx.annotation:annotation:1.7.1",
        "androidx.core:core:1.12.0",
        "androidx.core:core-ktx:1.12.0",

        # AndroidX Lifecycle - 2.6.1
        "androidx.lifecycle:lifecycle-common:2.6.1",
        "androidx.lifecycle:lifecycle-runtime:2.6.1",
        "androidx.lifecycle:lifecycle-runtime-ktx:2.6.1",
        "androidx.lifecycle:lifecycle-viewmodel:2.6.1",
        "androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1",
        "androidx.lifecycle:lifecycle-viewmodel-savedstate:2.6.1",
        "androidx.lifecycle:lifecycle-livedata-core:2.6.1",

        # AndroidX Activity - 1.8.0
        "androidx.activity:activity:1.8.0",
        "androidx.activity:activity-ktx:1.7.0",
        "androidx.activity:activity-compose:1.8.0",

        # AndroidX SavedState
        "androidx.savedstate:savedstate:1.2.1",
        "androidx.savedstate:savedstate-ktx:1.2.1",

        # Jetpack Compose - 1.7.5 compatible with Kotlin 2.1.0
        "androidx.compose.runtime:runtime:1.7.5",
        "androidx.compose.runtime:runtime-saveable:1.7.5",
        "androidx.compose.ui:ui:1.7.5",
        "androidx.compose.ui:ui-geometry:1.7.5",
        "androidx.compose.ui:ui-graphics:1.7.5",
        "androidx.compose.ui:ui-text:1.7.5",
        "androidx.compose.ui:ui-unit:1.7.5",
        "androidx.compose.ui:ui-util:1.7.5",
        "androidx.compose.ui:ui-tooling-preview:1.7.5",
        "androidx.compose.foundation:foundation:1.7.5",
        "androidx.compose.foundation:foundation-layout:1.7.5",
        "androidx.compose.animation:animation:1.7.5",
        "androidx.compose.animation:animation-core:1.7.5",

        # Material 3 - 1.3.1
        "androidx.compose.material3:material3:1.3.1",
        # Material Icons
        "androidx.compose.material:material-icons-core:1.7.5",
        "androidx.compose.material:material-icons-extended:1.7.5",

        # Compose ViewModel integration - 2.6.1
        "androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1",
        "androidx.lifecycle:lifecycle-runtime-compose:2.6.1",

        # Navigation - 2.6.0
        "androidx.navigation:navigation-common:2.6.0",
        "androidx.navigation:navigation-common-ktx:2.6.0",
        "androidx.navigation:navigation-runtime:2.6.0",
        "androidx.navigation:navigation-runtime-ktx:2.6.0",
        "androidx.navigation:navigation-compose:2.6.0",

        # Hilt Navigation for Compose - 1.2.0
        "androidx.hilt:hilt-navigation-compose:1.2.0",

        # Dagger and Hilt - Official version with KSP support
        "com.google.dagger:dagger:2.57",
        "com.google.dagger:dagger-compiler:2.57",
        "com.google.dagger:hilt-android:2.57",
        "com.google.dagger:hilt-compiler:2.57",
        "com.google.dagger:hilt-android-compiler:2.57",

        # KSP for Kotlin 2.1.0
        "com.google.devtools.ksp:symbol-processing-api:2.1.0-1.0.31",

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
        "androidx.interpolator:interpolator:1.0.0",
        "androidx.emoji2:emoji2:1.4.0",
        "androidx.emoji2:emoji2-views-helper:1.4.0",
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
