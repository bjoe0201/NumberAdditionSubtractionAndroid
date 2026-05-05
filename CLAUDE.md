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

## Architecture

- Single-module app (`app/`)
- Package: `com.example.numberadditionsubtractionandroid`
- Entry point: `MainActivity` — uses `ComponentActivity` with Compose `setContent`
- Theme defined in `ui/theme/` (Color.kt, Theme.kt, Type.kt)
- Dependencies managed via version catalog in `gradle/libs.versions.toml`
