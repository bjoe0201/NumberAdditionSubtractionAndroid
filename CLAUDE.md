# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Android app for number addition and subtraction, built with Kotlin and Jetpack Compose. Currently in early stages (scaffolded from Android Studio template).

## Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose with Material 3
- **Build:** Gradle 9.3.1 with Kotlin DSL and version catalogs (`gradle/libs.versions.toml`)
- **AGP:** 9.1.1, Kotlin 2.2.10, Compose BOM 2026.02.01
- **Min SDK:** 31, Target/Compile SDK: 36

## Build Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run unit tests
./gradlew test

# Run a single unit test class
./gradlew testDebugUnitTest --tests "com.example.numberadditionsubtractionandroid.ExampleUnitTest"

# Run instrumented tests (requires device/emulator)
./gradlew connectedAndroidTest

# Clean build
./gradlew clean
```

## Fixed APK Release Process (Use This Every Time)

Public GitHub releases must use the **debug-signed APK** for now.

- Do not upload `app-release-unsigned.apk`.
- `assembleRelease` output is unsigned in this project and is not installable.

### 1) Build installable APK

```bash
./gradlew assembleDebug
```

Windows PowerShell:

```powershell
.\gradlew.bat assembleDebug
```

Output file:

- `app/build/outputs/apk/debug/app-debug.apk`

### 2) Publish to GitHub Release

Use tag format `vX.Y.Z` and upload debug APK as release asset.

```powershell
gh release upload vX.Y.Z "app/build/outputs/apk/debug/app-debug.apk#NumberAdditionSubtractionAndroid-vX.Y.Z-debug-signed.apk" --clobber
```

If wrong asset was uploaded before, delete it first:

```powershell
gh release delete-asset vX.Y.Z app-release-unsigned.apk --yes
```

## Architecture

- Single-module app (`app/`)
- Package: `com.example.numberadditionsubtractionandroid`
- Entry point: `MainActivity` — uses `ComponentActivity` with Compose `setContent`
- Theme defined in `ui/theme/` (Color.kt, Theme.kt, Type.kt)
- Dependencies managed via version catalog in `gradle/libs.versions.toml`
