# Bazel Build Status

## Current State

### Gradle Build ✅
The Gradle build is **fully functional** with the latest dependencies:
- Compose BOM: 2025.05.01
- Compose UI: 1.8.1
- Material3: 1.3.2
- Kotlin: 2.1.0
- Android Gradle Plugin: 8.8.0
- Gradle: 8.10.2
- compileSdk/targetSdk: 35

Build command: `./gradlew assembleDebug`

### Bazel Build ✅
The Bazel build is **functional** with Compose BOM 2024.10.00:
- rules_kotlin: v2.0.0
- Compose BOM: 2024.10.00
- Compose UI: 1.7.6
- Material3: 1.3.1
- Compose Compiler Plugin: 2.0.0
- compileSdk/targetSdk: 35

Build command: `./bazelisk build //app:notekeeper_debug`

**Note**: Cannot use Compose BOM 2025.05.01 (Compose 1.8.1) due to Compose compiler plugin version incompatibilities.

## The Problem

### Initial Attempt (rules_kotlin 2.0.0, 2.1.0)
When attempting to build with rules_kotlin v2.0.0 or v2.1.0, the compilation failed with:

```
couldn't find inline method Landroidx/compose/runtime/ComposablesKt;.remember(Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;
```

This indicated the Compose compiler plugin wasn't being invoked properly.

### Official Example Configuration (rules_kotlin 1.9.5)
After finding the [official Jetpack Compose example](https://github.com/bazelbuild/rules_kotlin/tree/master/examples/jetpack_compose), I discovered the correct configuration:

1. **Plugin ID**: Use `androidx.compose.compiler.plugins.kotlin` (not `org.jetbrains.kotlin.plugin.compose`)
2. **Artifact**: Use `org.jetbrains.kotlin:kotlin-compose-compiler-plugin-embeddable` (not the gradle plugin)
3. **rules_kotlin**: Use v1.9.5 (stable version with confirmed Compose support)

Applied configuration:
```python
# In root BUILD.bazel
kt_compiler_plugin(
    name = "compose_plugin",
    id = "androidx.compose.compiler.plugins.kotlin",
    options = {
        "sourceInformation": "true",
    },
    target_embedded_compiler = True,
    visibility = ["//visibility:public"],
    deps = [
        "@maven//:org_jetbrains_kotlin_kotlin_compose_compiler_plugin_embeddable",
    ],
)
```

### New Error with Correct Configuration
With the proper configuration, the build now fails with:

```
exception: java.lang.NoSuchMethodError: 'org.jetbrains.kotlin.cli.common.messages.MessageCollector
org.jetbrains.kotlin.config.CommonConfigurationKeysKt.getMessageCollector(org.jetbrains.kotlin.config.CompilerConfiguration)'
	at androidx.compose.compiler.plugins.kotlin.ComposePluginRegistrar$Companion.checkCompilerConfiguration
```

### Root Cause

**Kotlin Compiler Version Mismatch**: rules_kotlin 1.9.5 ships with an embedded Kotlin 1.9.x compiler, but Compose 1.8.1 requires Kotlin 2.0+ and the Compose compiler plugins (versions 2.0.20, 2.1.20) are compiled against Kotlin 2.x APIs.

The `NoSuchMethodError` indicates that the Kotlin 2.x Compose compiler plugin is trying to call methods (`getMessageCollector`) that don't exist in the Kotlin 1.9.x compiler that rules_kotlin 1.9.5 uses.

**Why the official example might work**: The official Jetpack Compose example likely uses Compose 1.7.6 with older Compose compiler plugin versions that are compatible with Kotlin 1.9.x, or they use simpler Compose code that doesn't trigger these incompatibilities.

## Attempted Solutions

All of the following were attempted without success for Compose 1.8.1:

1. ✅ Tried rules_kotlin v2.1.0 - Plugin not properly invoked
2. ✅ Tried rules_kotlin v2.0.0 with correct config - `NoSuchMethodError: getMessageCollector`
3. ✅ Tried rules_kotlin v2.1.9 (latest) - WORKSPACE compatibility error (`@@compatibility_proxy` cycle)
4. ✅ Switched to rules_kotlin v1.9.5 (official example version) - Kotlin 1.9.x incompatible with Kotlin 2.x plugins
5. ✅ Used correct plugin ID: `androidx.compose.compiler.plugins.kotlin`
6. ✅ Used correct artifact: `kotlin-compose-compiler-plugin-embeddable`
7. ✅ Tried Compose compiler plugin 2.0.20 - Kotlin API version mismatch
8. ✅ Tried Compose compiler plugin 2.1.20 - Kotlin API version mismatch
9. ✅ Configured kt_compiler_plugin with target_embedded_compiler = True
10. ✅ Updated all Compose dependencies to 1.8.1 with `-android` multiplatform artifacts
11. ✅ Updated Android SDK API level to 35
12. ✅ Updated all AndroidX dependencies to match Gradle versions

**Result**:
- rules_kotlin v1.9.5: Kotlin 1.9.x compiler incompatible with Kotlin 2.x Compose plugins
- rules_kotlin v2.0.0 + Compose plugin 2.1.20: `NoSuchMethodError: getMessageCollector`
- rules_kotlin v2.0.0 + Compose plugin 2.0.0: ✅ **Works!**
- rules_kotlin v2.1.0: Plugin not properly invoked
- rules_kotlin v2.1.9: WORKSPACE cycle error, appears to require Bzlmod

**Key Finding**: The Compose compiler plugin version MUST match the Kotlin compiler version in rules_kotlin. Using Compose compiler plugin 2.0.0 with rules_kotlin v2.0.0 successfully builds with Compose 1.7.6 (BOM 2024.10.00).

## Current Solution (Implemented)

### Dual Build Systems with Version Divergence ✅
**Status**: Both build systems are functional with different Compose versions

**Gradle** (Latest):
- Compose BOM: 2025.05.01
- Compose UI: 1.8.1
- Material3: 1.3.2
- Kotlin: 2.1.0

**Bazel** (Stable):
- Compose BOM: 2024.10.00
- Compose UI: 1.7.6
- Material3: 1.3.1
- rules_kotlin: v2.0.0
- Compose Compiler Plugin: 2.0.0

**Trade-offs:**
- ✅ Both build systems work
- ✅ Can use Bazel for builds/CI
- ✅ Can use Gradle for development with latest features
- ⚠️ Must maintain compatibility with Compose 1.7.6 (minor version difference)
- ⚠️ Cannot use Compose 1.8.x exclusive features in shared code

## Alternative Options

### Option 1: Use Gradle Only
Remove Bazel configuration entirely and standardize on Gradle with Compose BOM 2025.05.01.

### Option 2: Wait for rules_kotlin Updates
Monitor [rules_kotlin releases](https://github.com/bazelbuild/rules_kotlin/releases) for better Kotlin 2.1.x/Compose 1.8.x support and upgrade both systems together.

## Dependencies Configuration Summary

### Gradle (Working)
See `build.gradle.kts` and `app/build.gradle.kts` for full configuration.

### Bazel (Working)
See `WORKSPACE` and `app/BUILD.bazel` for full configuration.

Key Bazel dependencies:
- rules_kotlin: v2.0.0
- rules_android: 0.1.1
- Kotlin stdlib: 2.0.0
- Compose Compiler Plugin: 2.0.0 (embeddable)
- Compose: 1.7.6 (all libraries with `-android` variants)
- Material3: 1.3.1
- Material Icons: 1.7.6

## Key Learnings

### What Was Discovered
1. **Correct Plugin Configuration**: The official rules_kotlin example uses:
   - Plugin ID: `androidx.compose.compiler.plugins.kotlin`
   - Artifact: `kotlin-compose-compiler-plugin-embeddable` (not gradle plugin)
   - Plugin defined in root BUILD.bazel (not app BUILD.bazel)

2. **Version Compatibility Matrix**:
   - rules_kotlin 1.9.5: Uses Kotlin 1.9.x compiler → Compatible with old Compose (<1.7.6)
   - rules_kotlin 2.0.0/2.1.0: Plugin invocation issues with Kotlin 2.x
   - Compose 1.8.1: Requires Kotlin 2.0+ → Not compatible with any current rules_kotlin

3. **The Fundamental Block**: No current released version of rules_kotlin properly supports Kotlin 2.x with Compose 1.7.6+

### Related Issues
- [rules_kotlin #1364](https://github.com/bazelbuild/rules_kotlin/issues/1364) - Compiler plugin support on Android
- rules_kotlin has ongoing compatibility challenges with Kotlin 2.x
- The embedded Compose compiler plugin in Kotlin 2.x requires special compiler API support

## Final Status

**Dual Build System Successfully Configured** ✅

Both Gradle and Bazel builds are now functional:

- **Gradle**: Latest Compose BOM 2025.05.01 for development
- **Bazel**: Stable Compose BOM 2024.10.00 for production builds

The configuration demonstrates the correct way to set up Compose with rules_kotlin, with the key insight that **Compose compiler plugin version must match rules_kotlin's Kotlin version** (2.0.0 in this case).

Future upgrades to Compose BOM 2025.05.01 in Bazel will require either:
1. Waiting for rules_kotlin to support Kotlin 2.1.x with compatible Compose compiler plugins
2. Using Bzlmod with rules_kotlin v2.1.9+ (requires migration from WORKSPACE)
