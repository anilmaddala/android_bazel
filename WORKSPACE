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

# Kotlin rules - Using v1.8.1 for compatibility with Dagger fork
RULES_KOTLIN_VERSION = "1.8.1"

http_archive(
    name = "io_bazel_rules_kotlin",
    sha256 = "a630cda9fdb4f56cf2dc20a4bf873765c41cf00e9379e8d59cd07b24730f4fde",
    urls = ["https://github.com/bazelbuild/rules_kotlin/releases/download/v%s/rules_kotlin_release.tgz" % RULES_KOTLIN_VERSION],
)

load(
    "@io_bazel_rules_kotlin//kotlin:repositories.bzl",
    "kotlin_repositories",
    "kotlinc_version",
)

kotlin_repositories(compiler_release = kotlinc_version(
    release = "1.8.21",
    sha256 = "6e43c5569ad067492d04d92c28cdf8095673699d81ce460bd7270443297e8fd7",
))

register_toolchains("//:kotlin_toolchain")

# Dagger and Hilt - Using custom fork for Bazel component generation
DAGGER_SHA = "a796141af307e2b3a48b64a81ee163d96ffbfb41a71f0ea9cf8d26f930c80ca6"

http_archive(
    name = "dagger",
    sha256 = DAGGER_SHA,
    strip_prefix = "dagger-use-generated-class-instead-of-superclass",
    urls = ["https://github.com/pswaminathan/dagger/archive/refs/heads/use-generated-class-instead-of-superclass.zip"],
)

load("@dagger//:repositories.bzl", "dagger_repositories")
dagger_repositories()

load("@dagger//:workspace_defs.bzl", "dagger_workspace")
dagger_workspace()

load("@dagger//:workspace_defs_2.bzl", "dagger_workspace_2")
dagger_workspace_2()

load(
    "@dagger//:workspace_defs.bzl",
    "HILT_ANDROID_ARTIFACTS",
    "HILT_ANDROID_REPOSITORIES",
)

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
        # Kotlin stdlib - 1.8.21 (matching rules_kotlin 1.8.1)
        "org.jetbrains.kotlin:kotlin-stdlib:1.8.21",
        "org.jetbrains.kotlin:kotlin-stdlib-common:1.8.21",

        # Compose Compiler - 1.4.7 compatible with Kotlin 1.8.21
        "androidx.compose.compiler:compiler:1.4.7",

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

        # Jetpack Compose - 1.4.3 compatible with Kotlin 1.8.21
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

        # Material 3 - 1.1.1
        "androidx.compose.material3:material3:1.1.1",
        # Material Icons
        "androidx.compose.material:material-icons-core:1.4.3",
        "androidx.compose.material:material-icons-extended:1.4.3",

        # Compose ViewModel integration - 2.6.1
        "androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1",
        "androidx.lifecycle:lifecycle-runtime-compose:2.6.1",

        # Navigation - 2.6.0
        "androidx.navigation:navigation-common:2.6.0",
        "androidx.navigation:navigation-common-ktx:2.6.0",
        "androidx.navigation:navigation-runtime:2.6.0",
        "androidx.navigation:navigation-runtime-ktx:2.6.0",
        "androidx.navigation:navigation-compose:2.6.0",

        # Hilt Navigation for Compose - 1.0.0
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
        "androidx.interpolator:interpolator:1.0.0",
        "androidx.emoji2:emoji2:1.4.0",
        "androidx.emoji2:emoji2-views-helper:1.4.0",
        "androidx.arch.core:core-common:2.2.0",
        "androidx.arch.core:core-runtime:2.2.0",
    ] + HILT_ANDROID_ARTIFACTS,
    fail_on_missing_checksum = False,
    repositories = [
        "https://maven.google.com",
        "https://repo1.maven.org/maven2",
    ] + HILT_ANDROID_REPOSITORIES,
    strict_visibility = False,
    version_conflict_policy = "pinned",
)
