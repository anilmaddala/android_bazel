# Bazel Migration Guide for NoteKeeper

## Current Status

### Gradle Build ✅
The app successfully builds with Gradle using:
```bash
./gradlew assembleDebug
```

Output APK: `app/build/outputs/apk/debug/app-debug.apk` (14MB)

### Bazel Build 🚧 (Work in Progress)
Bazel 7.6.1 is installed and configured, but there are dependency resolution conflicts to resolve.

## What's Been Set Up

### 1. Bazel Version
- ✅ Bazel 7.6.1 installed via Bazelisk
- ✅ `.bazelversion` file specifies version 7.6.1

### 2. Configuration Files Created
- ✅ `WORKSPACE` - Defines external dependencies and rules
- ✅ `.bazelrc` - Bazel build configuration
- ✅ `BUILD.bazel` - Root build file
- ✅ `app/BUILD.bazel` - App module build configuration

### 3. Build Rules
- ✅ Android rules (rules_android 0.1.1)
- ✅ Kotlin rules (rules_kotlin 1.9.6)
- ✅ Maven dependency management (rules_jvm_external 4.5)

### 4. Hilt/KSP Configuration
- ✅ Hilt annotation processor configured as java_plugin
- ✅ KSP used in Gradle (better Bazel compatibility than KAPT)

## Current Issue

**Dependency Version Conflicts**

The main blocker is conflicting dependency versions between AndroidX libraries, particularly:
- `androidx.lifecycle:lifecycle-runtime` (2.3.1 vs 2.6.1 vs 2.6.2)
- `androidx.compose.*` libraries (1.2.1 vs 1.4.1 vs 1.5.4)
- `androidx.navigation.*` (2.5.1 vs 2.7.5)

This is a common issue when migrating from Gradle (which has more lenient version resolution) to Bazel (which requires strict version alignment).

## Solutions to Try

### Option 1: Use Simpler Dependencies (Recommended for POC)

Simplify the app to use fewer dependencies and more aligned versions:

1. Remove Hilt temporarily (use manual DI)
2. Use a single Compose BOM version
3. Reduce navigation complexity

### Option 2: Manual Dependency Resolution

Create a comprehensive dependency tree with explicit versions for all transitive dependencies. This requires:

1. List all transitive dependencies
2. Force specific versions using `override_targets`
3. Test each dependency individually

### Option 3: Use rules_android_ndk (Advanced)

Switch to newer Bazel Android rules that have better Maven dependency support.

## Files Organization

The project maintains **both** build systems:

```
android_bazel/
├── WORKSPACE              # Bazel workspace (Bazel only)
├── .bazelrc               # Bazel config (Bazel only)
├── BUILD.bazel            # Root Bazel build (Bazel only)
├── build.gradle.kts       # Root Gradle build (Gradle only)
├── settings.gradle.kts    # Gradle settings (Gradle only)
├── gradle.properties      # Gradle properties (Gradle only)
├── gradlew                # Gradle wrapper (Gradle only)
└── app/
    ├── BUILD.bazel        # App Bazel build (Bazel only)
    ├── build.gradle.kts   # App Gradle build (Gradle only)
    └── src/               # Shared source code (both)
```

## Build Commands

### Gradle
```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Clean
./gradlew clean

# Install on device
./gradlew installDebug
```

### Bazel (when working)
```bash
# Build debug APK
./bazelisk build //app:notekeeper_debug

# Build release APK
./bazelisk build //app:notekeeper

# Clean
./bazelisk clean

# Query dependencies
./bazelisk query 'deps(//app:notekeeper_debug)'
```

## Next Steps to Complete Bazel Migration

1. **Simplify Dependencies**
   - Consider creating a minimal version first
   - Add dependencies incrementally
   - Test each addition

2. **Use Dependency Pinning**
   - Generate `maven_install.json` with pinned versions
   - Run: `./bazelisk run @maven//:pin`
   - Commit the generated file

3. **Test Incremental Builds**
   - Bazel excels at incremental builds
   - Compare build times with Gradle

4. **Set Up CI/CD**
   - Configure Bazel caching
   - Use remote execution if needed

## Advantages of Bazel (Once Working)

1. **Faster Incremental Builds**: Only rebuilds what changed
2. **Reproducible Builds**: Same inputs = same outputs
3. **Multi-Language Support**: Easy to add C++, Python, etc.
4. **Remote Caching**: Share build artifacts across team
5. **Precise Dependencies**: Better understanding of dependency graph

## Current Gradle vs Bazel Status

| Feature | Gradle | Bazel |
|---------|--------|-------|
| Build Success | ✅ Yes | ❌ Dependency conflicts |
| APK Output | ✅ 14MB | ❌ Not yet |
| Hilt Support | ✅ With KSP | ⚠️ Configured but not tested |
| Build Time (clean) | ~1m 44s | ❓ Not measured |
| Build Time (incremental) | ~10-30s | ❓ Expected faster |

## Debugging Bazel Issues

```bash
# See full error output
./bazelisk build //app:notekeeper_debug 2>&1 | less

# Check dependency tree
./bazelisk query 'deps(//app:notekeeper_debug)' --output=graph

# Clean everything
./bazelisk clean --expunge

# Verbose output
./bazelisk build //app:notekeeper_debug --verbose_failures -s
```

## Resources

- [Bazel Android Rules](https://github.com/bazelbuild/rules_android)
- [rules_jvm_external](https://github.com/bazelbuild/rules_jvm_external)
- [Bazel Android Tutorial](https://bazel.build/start/android-app)
- [rules_kotlin](https://github.com/bazelbuild/rules_kotlin)

## Conclusion

The foundation for Bazel is in place. The main work remaining is resolving the Maven dependency conflicts, which is a known challenge when migrating from Gradle to Bazel. The Gradle build continues to work perfectly, so development can proceed while the Bazel migration is completed.
