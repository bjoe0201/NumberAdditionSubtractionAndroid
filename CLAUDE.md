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

This project is **not currently configured like `MultiplicationZoo_ANDROID`**. In `MultiplicationZoo_ANDROID`, `assembleRelease` can produce a signed/installable `app-release.apk`. In this project, `app/build.gradle.kts` has no release `signingConfig`, so `assembleRelease` produces an unsigned release APK that must not be published.

- Do not upload `app-release-unsigned.apk`.
- `assembleRelease` output is unsigned in this project and is not installable.
- Do not copy the `MultiplicationZoo_ANDROID` release APK upload command until this project has a real release keystore/signing workflow.
- GitHub Release assets should be named `NumberAdditionSubtractionAndroid-vX.Y.Z-debug-signed.apk` while this temporary debug-signed publishing process is in use.

### Current Release Status Checked

Checked on 2026-05-05:

- Repository: `https://github.com/bjoe0201/NumberAdditionSubtractionAndroid.git`
- Existing release: `v1.0.0`
- Release asset: `NumberAdditionSubtractionAndroid-v1.0.0-debug-signed.apk`
- Asset SHA-256: `ee2e26c1e3ec1e200b520a47a30a44e07e5e10ffbb10e539096384ca3b567a6d`
- Local `app/build/outputs/apk/debug/app-debug.apk` SHA-256 matched the GitHub Release asset.
- APK signature verification passed with `apksigner`; signer certificate is Android Debug (`CN=Android Debug`).

Conclusion: the current `v1.0.0` GitHub Release APK is correct for the current temporary policy because it is the debug-signed APK, not `app-release-unsigned.apk`.

### 0) Pre-release checks

Windows PowerShell:

```powershell
git branch --show-current
git remote -v
gh auth status
gh release list --limit 100
```

Before publishing, confirm:

- Current branch is the intended release branch, normally `main`.
- Remote is `https://github.com/bjoe0201/NumberAdditionSubtractionAndroid.git`.
- GitHub CLI is logged in to the correct account.
- Release tag uses format `vX.Y.Z`.
- `app/build.gradle.kts` `versionCode` is incremented and `versionName` matches the release tag without the `v` prefix.

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

Do **not** use this file for public release in this project:

- `app/build/outputs/apk/release/app-release-unsigned.apk`

Optional file and hash check:

```powershell
$apk = ".\app\build\outputs\apk\debug\app-debug.apk"
Test-Path $apk
Get-Item $apk
Get-FileHash $apk -Algorithm SHA256
```

Optional signature verification:

```powershell
$sdk = if (Test-Path .\local.properties) {
  Get-Content .\local.properties |
	Where-Object { $_ -like 'sdk.dir=*' } |
	Select-Object -First 1 |
	ForEach-Object { ($_ -replace '^sdk.dir=', '') -replace '\\:', ':' -replace '\\\\', '\' }
}
if (-not $sdk) { $sdk = $env:ANDROID_HOME }
if (-not $sdk) { $sdk = $env:ANDROID_SDK_ROOT }
if (-not $sdk) { throw 'Android SDK path not found. Set sdk.dir in local.properties or ANDROID_HOME/ANDROID_SDK_ROOT.' }
$apksigner = Get-ChildItem -Path (Join-Path $sdk 'build-tools') -Recurse -Filter 'apksigner.bat' |
  Sort-Object FullName -Descending |
  Select-Object -First 1
if (-not $apksigner) { throw 'apksigner.bat not found under Android SDK build-tools.' }
& $apksigner.FullName verify --verbose --print-certs .\app\build\outputs\apk\debug\app-debug.apk
```

Expected verification includes:

```text
Verifies
Verified using v2 scheme (APK Signature Scheme v2): true
Number of signers: 1
Signer #1 certificate DN: C=US, O=Android, CN=Android Debug
```

### 2) Publish to GitHub Release

Use tag format `vX.Y.Z` and upload debug APK as release asset.

```powershell
gh release upload vX.Y.Z ".\app\build\outputs\apk\debug\app-debug.apk#NumberAdditionSubtractionAndroid-vX.Y.Z-debug-signed.apk" --clobber
```

If wrong asset was uploaded before, delete it first:

```powershell
gh release delete-asset vX.Y.Z app-release-unsigned.apk --yes
```

If a wrongly named APK was uploaded, delete that exact asset name too:

```powershell
gh release delete-asset vX.Y.Z wrong-asset-name.apk --yes
```

### 3) Verify GitHub Release after upload

Windows PowerShell:

```powershell
gh release view vX.Y.Z --json tagName,name,url,publishedAt,assets
gh release list --limit 100
```

The release asset must contain exactly the debug-signed APK for that version, for example:

```text
NumberAdditionSubtractionAndroid-v1.0.0-debug-signed.apk
```

Verify the uploaded asset hash if needed:

```powershell
$localHash = (Get-FileHash ".\app\build\outputs\apk\debug\app-debug.apk" -Algorithm SHA256).Hash.ToLowerInvariant()
$release = gh release view vX.Y.Z --json assets | ConvertFrom-Json
$asset = $release.assets | Where-Object { $_.name -eq "NumberAdditionSubtractionAndroid-vX.Y.Z-debug-signed.apk" }
$remoteHash = $asset.digest -replace '^sha256:', ''
[pscustomobject]@{ LocalHash = $localHash; RemoteHash = $remoteHash; Match = ($localHash -eq $remoteHash) }
```

### 4) Installation notes

- A debug-signed APK is installable, but it is not a long-term public release signing strategy.
- Devices that already installed the same package name with a different signing key may reject updates. Uninstall the old app first if installation fails due to signature mismatch.
- Android rejects downgrade installs when the new APK `versionCode` is less than or equal to the installed app's `versionCode`; increment `versionCode` for every release.
- If this project later adds a private release keystore, switch the process to publish a signed `app-release.apk` and stop publishing debug APKs.
- Never upload keystore files, `keystore.properties`, passwords, mapping files, or unsigned APKs to GitHub Releases.

## Architecture

- Single-module app (`app/`)
- Package: `com.example.numberadditionsubtractionandroid`
- Entry point: `MainActivity` — uses `ComponentActivity` with Compose `setContent`
- Theme defined in `ui/theme/` (Color.kt, Theme.kt, Type.kt)
- Dependencies managed via version catalog in `gradle/libs.versions.toml`
