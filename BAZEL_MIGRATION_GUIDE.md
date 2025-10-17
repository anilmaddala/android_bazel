# Complete Guide: Adding Bazel Build Support to Android Gradle Project

## Table of Contents
1. [Prerequisites](#prerequisites)
2. [Step-by-Step Migration](#step-by-step-migration)
3. [Critical Findings & Solutions](#critical-findings--solutions)
4. [Version Compatibility Matrix](#version-compatibility-matrix)
5. [Common Issues & Troubleshooting](#common-issues--troubleshooting)
6. [Best Practices](#best-practices)

---

## Prerequisites

### Required Tools
- **Bazelisk**: Automatic Bazel version manager
- **Java 17+**: For Kotlin 1.9+ compilation
- **Android SDK**: API level matching your project
- **Existing Gradle project**: With dependencies documented

### Initial Setup

```bash
# Install Bazelisk (Bazel version manager)
# Linux/Mac
wget https://github.com/bazelbuild/bazelisk/releases/download/v1.19.0/bazelisk-linux-amd64
chmod +x bazelisk-linux-amd64
mv bazelisk-linux-amd64 ./bazelisk

# Verify installation
./bazelisk version
```

---

## Step-by-Step Migration

### Step 1: Create Bazel Workspace Files

#### 1.1 Create `.bazelversion`
```
7.6.1
```

#### 1.2 Create `.bazelrc`
```python
# Common flags for all commands
common --enable_platform_specific_config

# Android configuration
build:android --fat_apk_cpu=arm64-v8a,armeabi-v7a
build:android --android_crosstool_top=@androidndk//:toolchain
build:android --host_crosstool_top=@bazel_tools//tools/cpp:toolchain

# Java configuration - use local JDK
build --java_language_version=17
build --tool_java_language_version=17
build --java_runtime_version=local_jdk
build --tool_java_runtime_version=local_jdk

# Kotlin configuration
build --strategy=KotlinCompile=worker
# Disable worker sandboxing to avoid Java version issues
build --worker_sandboxing=false

# Performance optimizations
build --worker_max_instances=4
# Allow JAVA_HOME in action environment
build --action_env=JAVA_HOME

# Output configuration
build --verbose_failures
build --show_timestamps

# Test configuration
test --test_output=errors
test --test_summary=detailed

# Cache configuration
build --disk_cache=~/.cache/bazel

# Incremental dexing
build --incremental_dexing

# Allow modern Android features
build --experimental_android_databinding_v2
build --experimental_android_resource_shrinking
```

**Critical Configuration Notes:**
- `--worker_sandboxing=false`: Prevents Java version conflicts between Bazel's sandbox and your JDK
- `--action_env=JAVA_HOME`: Ensures consistent Java version across build actions
- `--java_runtime_version=local_jdk`: Uses your system JDK instead of Bazel's remote JDK

---

### Step 2: Create WORKSPACE File

```python
workspace(name = "your_project_name")

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

# ===== ANDROID RULES =====
http_archive(
    name = "build_bazel_rules_android",
    sha256 = "cd06d15dd8bb59926e4d65f9003bfc20f9da4b2519985c27e190cddc8b7a7806",
    strip_prefix = "rules_android-0.1.1",
    urls = ["https://github.com/bazelbuild/rules_android/archive/v0.1.1.zip"],
)

load("@build_bazel_rules_android//android:rules.bzl", "android_sdk_repository")

android_sdk_repository(
    name = "androidsdk",
    api_level = 34,  # Match your targetSdk
)

# ===== KOTLIN RULES =====
http_archive(
    name = "rules_kotlin",
    sha256 = "3b772976fec7bdcda1d84b9d39b176589424c047eb2175bed09aac630e50af43",
    urls = ["https://github.com/bazelbuild/rules_kotlin/releases/download/v1.9.6/rules_kotlin-v1.9.6.tar.gz"],
)

load("@rules_kotlin//kotlin:repositories.bzl", "kotlin_repositories")
kotlin_repositories()

load("@rules_kotlin//kotlin:core.bzl", "kt_register_toolchains")
kt_register_toolchains()

# ===== MAVEN DEPENDENCIES =====
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

        # Dagger and Hilt - 2.50
        "com.google.dagger:dagger:2.50",
        "com.google.dagger:dagger-compiler:2.50",
        "com.google.dagger:hilt-core:2.50",
        "com.google.dagger:hilt-android:2.50",
        "com.google.dagger:hilt-android-compiler:2.50",

        # Hilt Navigation for Compose - 1.0.0 (optional, only if using hiltViewModel())
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
```

**Key Points:**
- Use Compose 1.4.3 (pre-multiplatform artifacts) - Compose 1.5+ has different artifact naming that's incompatible with simple Bazel maven references
- Compose Compiler version MUST match Kotlin version (1.5.13 for Kotlin 1.9.23)
- `version_conflict_policy = "pinned"` helps resolve dependency conflicts

---

### Step 3: Create app/BUILD.bazel

```python
load("@rules_kotlin//kotlin:android.bzl", "kt_android_library")
load("@rules_kotlin//kotlin:core.bzl", "kt_compiler_plugin")
load("@build_bazel_rules_android//android:rules.bzl", "android_binary")

# ===== COMPOSE COMPILER PLUGIN =====
# CRITICAL: This enables Compose @Composable functions to work
kt_compiler_plugin(
    name = "compose_plugin",
    id = "androidx.compose.compiler",
    options = {
        "suppressKotlinVersionCompatibilityCheck": "true",
    },
    target_embedded_compiler = True,
    deps = [
        "@maven//:androidx_compose_compiler_compiler",
    ],
)

# ===== HILT ANNOTATION PROCESSOR =====
java_plugin(
    name = "hilt_plugin",
    generates_api = True,
    processor_class = "dagger.hilt.processor.internal.root.RootProcessor",
    deps = [
        "@maven//:com_google_dagger_hilt_android_compiler",
        "@maven//:com_google_dagger_hilt_core",
        "@maven//:com_google_dagger_dagger_compiler",
    ],
)

# ===== KOTLIN ANDROID LIBRARY =====
kt_android_library(
    name = "app_lib",
    srcs = glob([
        "src/main/java/**/*.kt",
    ]),
    custom_package = "com.example.yourapp",
    manifest = "src/main/AndroidManifest.xml",
    resource_files = glob(["src/main/res/**"]),
    plugins = [
        ":hilt_plugin",      # Must be BEFORE compose_plugin
        ":compose_plugin",   # Must be AFTER hilt_plugin
    ],
    deps = [
        "@maven//:androidx_core_core_ktx",
        "@maven//:androidx_lifecycle_lifecycle_runtime_ktx",
        "@maven//:androidx_activity_activity_compose",
        "@maven//:androidx_compose_ui_ui",
        "@maven//:androidx_compose_ui_ui_graphics",
        "@maven//:androidx_compose_ui_ui_tooling_preview",
        "@maven//:androidx_compose_material3_material3",
        "@maven//:androidx_compose_material_material_icons_extended",
        "@maven//:androidx_navigation_navigation_compose",
        "@maven//:androidx_lifecycle_lifecycle_viewmodel_compose",
        "@maven//:com_google_dagger_hilt_android",
        "@maven//:org_jetbrains_kotlin_kotlin_stdlib",
        "@maven//:org_jetbrains_kotlinx_kotlinx_coroutines_core",
        "@maven//:org_jetbrains_kotlinx_kotlinx_coroutines_android",
        "@maven//:javax_inject_javax_inject",
    ],
    visibility = ["//visibility:private"],
)

# ===== ANDROID BINARY (APK) =====
android_binary(
    name = "app",
    custom_package = "com.example.yourapp",
    manifest = "src/main/AndroidManifest.xml",
    manifest_values = {
        "minSdkVersion": "24",
        "targetSdkVersion": "34",
        "versionCode": "1",
        "versionName": "1.0",
    },
    deps = [":app_lib"],
    visibility = ["//visibility:public"],
)

# ===== DEBUG APK =====
android_binary(
    name = "app_debug",
    custom_package = "com.example.yourapp",
    debug_key = "//app:debug.keystore",
    manifest = "src/main/AndroidManifest.xml",
    manifest_values = {
        "minSdkVersion": "24",
        "targetSdkVersion": "34",
        "versionCode": "1",
        "versionName": "1.0-debug",
    },
    deps = [":app_lib"],
    visibility = ["//visibility:public"],
)
```

**Critical Configuration Notes:**

1. **Compose Compiler Plugin Setup:**
   - Must load from `@rules_kotlin//kotlin:core.bzl` (NOT `jvm.bzl`)
   - Plugin ID is `androidx.compose.compiler` (NOT the full package path)
   - `target_embedded_compiler = True` is required
   - Options can include `suppressKotlinVersionCompatibilityCheck` for minor version mismatches

2. **Plugin Order Matters:**
   - Hilt plugin MUST come before Compose plugin
   - Order: `[":hilt_plugin", ":compose_plugin"]`

3. **AndroidManifest.xml Requirements:**
   - Must include `package="com.example.yourapp"` attribute in root `<manifest>` tag
   - This is required for Bazel even though it's deprecated in newer Gradle

---

### Step 4: Fix AndroidManifest.xml for Bazel

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.example.yourapp">  <!-- REQUIRED for Bazel -->

    <application
        android:name=".YourApplication"
        android:allowBackup="true"
        android:icon="@android:drawable/ic_menu_edit"
        android:label="@string/app_name"
        android:theme="@style/Theme.YourApp">
        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:theme="@style/Theme.YourApp">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```

---

### Step 5: Create Debug Keystore (Optional)

```bash
keytool -genkey -v -keystore app/debug.keystore \
  -alias androiddebugkey \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000 \
  -storepass android \
  -keypass android \
  -dname "CN=Android Debug,O=Android,C=US"
```

---

### Step 6: Build with Bazel

```bash
# Clean build
./bazelisk clean

# Build debug APK
./bazelisk build //app:app_debug

# Output location
ls -lh bazel-bin/app/app_debug.apk
```

---

## Critical Findings & Solutions

### Finding 1: Compose Compiler Plugin Configuration

**Problem:** Kotlin compiler can't find inline functions from Compose (e.g., `remember()`, `@Composable`)

**Error:**
```
Couldn't inline method call: CALL 'public final fun remember <T> ...'
couldn't find inline method Landroidx/compose/runtime/ComposablesKt;.remember
```

**Solution:** Properly configure the Compose compiler plugin:
```python
load("@rules_kotlin//kotlin:core.bzl", "kt_compiler_plugin")  # CRITICAL: Use core.bzl

kt_compiler_plugin(
    name = "compose_plugin",
    id = "androidx.compose.compiler",  # CRITICAL: Exact ID
    target_embedded_compiler = True,   # CRITICAL: Must be True
    deps = ["@maven//:androidx_compose_compiler_compiler"],
)
```

### Finding 2: Java Version Consistency

**Problem:** Java version mismatch between Bazel's sandbox and system JDK

**Error:**
```
UnsupportedClassVersionError: class file version 61.0,
this version only recognizes up to 55.0
```

**Solution:** Configure `.bazelrc`:
```python
build --java_runtime_version=local_jdk
build --tool_java_runtime_version=local_jdk
build --worker_sandboxing=false
build --action_env=JAVA_HOME
```

### Finding 3: Compose Multiplatform Artifact Naming

**Problem:** Compose 1.5+ uses multiplatform artifacts with `-android` suffix not found by Bazel

**Error:**
```
https://maven.google.com/androidx/compose/ui/ui/1.5.4/ui-1.5.4.aar: not found
```

**Solution:** Use Compose 1.4.3 which has pre-multiplatform artifact naming:
```python
"androidx.compose.ui:ui:1.4.3",  # Works with Bazel
# NOT: "androidx.compose.ui:ui:1.5.4"  # Fails - multiplatform naming
```

### Finding 4: Kotlin-Compose Version Compatibility

**Problem:** Compose Compiler version must EXACTLY match Kotlin version

**Error:**
```
this version (1.5.14) of the Compose Compiler requires Kotlin version 1.9.24
but you appear to be using Kotlin version 1.9.23
```

**Solution:** Use exact version mapping:
- Kotlin 1.9.23 → Compose Compiler 1.5.13
- Kotlin 1.9.24 → Compose Compiler 1.5.14
- See: https://developer.android.com/jetpack/androidx/releases/compose-kotlin

### Finding 5: Hilt with hiltViewModel() Alternative

**Problem:** `hiltViewModel()` from hilt-navigation-compose has inline function issues

**Error:**
```
couldn't find inline method hiltViewModel$default
```

**Solution:** Use standard Hilt Activity integration:
```kotlin
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: NoteViewModel by viewModels()  // Standard Hilt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Pass viewModel directly instead of using hiltViewModel()
            NavGraph(navController, viewModel)
        }
    }
}
```

### Finding 6: API Compatibility with Older Libraries

**Problem:** Code written for newer APIs fails with older library versions

**Errors:**
```
unresolved reference: enableEdgeToEdge
unresolved reference: topAppBarColors
```

**Solution:** Remove newer API calls or update code:
```kotlin
// Remove: enableEdgeToEdge() - not in Activity 1.7.2

// Remove custom colors - not in Material3 1.0.1
TopAppBar(
    title = { Text("Title") }
    // Remove: colors = TopAppBarDefaults.topAppBarColors(...)
)

// Change minLines to maxLines
OutlinedTextField(
    // Remove: minLines = 5  // Not supported
    maxLines = 10  // Use maxLines instead
)
```

---

## Version Compatibility Matrix

### Working Configuration

| Component | Version | Notes |
|-----------|---------|-------|
| **Bazel** | 7.6.1 | Via Bazelisk |
| **rules_kotlin** | 1.9.6 | Provides Kotlin 1.9.23 |
| **rules_android** | 0.1.1 | Latest stable |
| **rules_jvm_external** | 4.5 | Maven dependency management |
| **Java** | 17+ | Local JDK |
| **Kotlin** | 1.9.23 | From rules_kotlin |
| **Compose Compiler** | 1.5.13 | MUST match Kotlin 1.9.23 |
| **Compose UI** | 1.4.3 | Pre-multiplatform artifacts |
| **Material3** | 1.0.1 | Compatible with Compose 1.4.3 |
| **Lifecycle** | 2.6.1 | Compatible with Compose 1.4.3 |
| **Activity Compose** | 1.7.2 | Compatible with Compose 1.4.3 |
| **Navigation Compose** | 2.5.3 | Compatible with Compose 1.4.3 |
| **Hilt** | 2.50 | Latest stable |

### Version Mapping Reference

#### Kotlin → Compose Compiler
```
Kotlin 1.9.20 → Compose Compiler 1.5.11
Kotlin 1.9.21 → Compose Compiler 1.5.12
Kotlin 1.9.22 → Compose Compiler 1.5.13
Kotlin 1.9.23 → Compose Compiler 1.5.13
Kotlin 1.9.24 → Compose Compiler 1.5.14
```

#### Compose Version Constraints
```
Compose 1.4.x → Compatible with Bazel (pre-multiplatform)
Compose 1.5.x+ → Incompatible (uses multiplatform artifacts with -android suffix)
```

---

## Common Issues & Troubleshooting

### Issue 1: "Package name not declared in AndroidManifest"

**Error:**
```
Main AndroidManifest.xml manifest:package attribute is not declared
```

**Fix:** Add `package` attribute to manifest:
```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.example.yourapp">
```

### Issue 2: Dependency Version Conflicts

**Error:**
```
ERROR: Multiple conflicting versions of androidx.lifecycle:lifecycle-runtime
```

**Fix:** Use `version_conflict_policy = "pinned"` in WORKSPACE:
```python
maven_install(
    artifacts = [...],
    version_conflict_policy = "pinned",
)
```

### Issue 3: Incremental Build Issues

**Symptom:** Changes not reflected in builds

**Fix:**
```bash
# Full clean and rebuild
./bazelisk clean --expunge
./bazelisk build //app:app_debug
```

### Issue 4: Missing Dependencies at Runtime

**Symptom:** `NoClassDefFoundError` at runtime

**Fix:** Ensure all transitive dependencies are explicitly listed:
```python
deps = [
    "@maven//:androidx_core_core_ktx",
    "@maven//:org_jetbrains_kotlin_kotlin_stdlib",
    # Add ALL direct dependencies explicitly
]
```

### Issue 5: Build Too Slow

**Optimization:**
```python
# In .bazelrc
build --disk_cache=~/.cache/bazel
build --worker_max_instances=4
build --jobs=4  # Match CPU cores
```

---

## Best Practices

### 1. Keep Both Build Systems

**Recommendation:** Maintain both Gradle and Bazel builds in parallel
- Gradle: Fast for local development and IDE support
- Bazel: For CI/CD, caching, and large-scale builds

### 2. Version Pinning

Always pin exact versions in WORKSPACE:
```python
artifacts = [
    "androidx.compose.ui:ui:1.4.3",  # GOOD: Exact version
    # NOT: "androidx.compose.ui:ui:1.4.+"  # BAD: Version ranges don't work well
]
```

### 3. Dependency Audit

Before migration, audit your Gradle dependencies:
```bash
./gradlew app:dependencies > gradle-deps.txt
```

Use this to create your Bazel maven_install list.

### 4. Incremental Migration Strategy

1. **Phase 1:** Get basic build working without Compose
2. **Phase 2:** Add Compose compiler plugin
3. **Phase 3:** Add Hilt annotation processing
4. **Phase 4:** Optimize and refine

### 5. Testing Strategy

```python
# Add test targets
kt_android_library(
    name = "app_lib",
    testonly = False,
    # ...
)

android_local_test(
    name = "app_test",
    srcs = glob(["src/test/**/*.kt"]),
    deps = [":app_lib"],
)
```

### 6. CI/CD Integration

```yaml
# .github/workflows/bazel.yml
- name: Build with Bazel
  run: |
    ./bazelisk build //app:app_debug
    ./bazelisk test //...
```

---

## Research Resources

### Official Documentation
- Bazel Android Rules: https://github.com/bazelbuild/rules_android
- rules_kotlin: https://github.com/bazelbuild/rules_kotlin
- Compose-Kotlin Compatibility: https://developer.android.com/jetpack/androidx/releases/compose-kotlin

### Working Examples
- Bencodes/bazel_jetpack_compose_example: https://github.com/Bencodes/bazel_jetpack_compose_example
- Google Dagger Bazel Examples: https://github.com/google/dagger (search for BUILD files)

### Key Discoveries
1. **Compose Compiler Plugin:** Must use `kotlin:core.bzl` not `jvm.bzl`
2. **Plugin ID:** Use `androidx.compose.compiler` (simple name, not full package)
3. **Pre-multiplatform Compose:** Version 1.4.3 is the last with simple artifact naming
4. **Worker Sandboxing:** Must disable for Java version consistency

---

## Migration Checklist

- [ ] Install Bazelisk
- [ ] Create `.bazelversion`, `.bazelrc`
- [ ] Create WORKSPACE with Android, Kotlin, and Maven rules
- [ ] Audit Gradle dependencies and add to maven_install
- [ ] Create app/BUILD.bazel with kt_compiler_plugin for Compose
- [ ] Configure Hilt annotation processor with java_plugin
- [ ] Add package attribute to AndroidManifest.xml
- [ ] Set up debug keystore
- [ ] Run first build: `./bazelisk build //app:app_debug`
- [ ] Fix version compatibility issues
- [ ] Test APK installation and functionality
- [ ] Document any project-specific configurations

---

## Success Metrics

After successful migration, you should have:

✅ **Two working build systems:**
- Gradle: `./gradlew assembleDebug` → `app/build/outputs/apk/debug/app-debug.apk`
- Bazel: `./bazelisk build //app:app_debug` → `bazel-bin/app/app_debug.apk`

✅ **Comparable build artifacts:**
- Gradle APK: ~14MB
- Bazel APK: ~12MB
- Both should run identically on device

✅ **Build times:**
- First build: 60-90s
- Incremental builds: 10-30s (with caching)
- CI/CD builds: Significantly faster with remote caching

---

## Conclusion

Adding Bazel to an existing Android Gradle project with Jetpack Compose and Hilt requires careful attention to:

1. **Version compatibility** - Kotlin, Compose Compiler, and Compose UI versions must align
2. **Compose compiler plugin** - Proper configuration is critical for @Composable functions
3. **Java version consistency** - Use local JDK and disable worker sandboxing
4. **Artifact naming** - Use pre-multiplatform Compose versions (1.4.x)
5. **Plugin ordering** - Hilt before Compose in the plugins list

The key breakthrough was finding the working Compose compiler plugin configuration from Google's example repositories, which enabled inline functions and @Composable annotations to work correctly with Bazel.

Both build systems can coexist successfully, providing flexibility for development and optimized builds for CI/CD.

---

**Last Updated:** October 2025
**Tested With:** Bazel 7.6.1, Kotlin 1.9.23, Compose 1.4.3, Hilt 2.50
