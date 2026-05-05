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

Public GitHub releases must use the **signed release APK** produced by `assembleRelease`.

This project now supports release signing through a private, ignored `keystore.properties` file or `NUMBER_MATH_RELEASE_*` environment variables. Do not publish debug APKs, unsigned APKs, or APKs signed with the Android Debug certificate.

- Use `app/build/outputs/apk/release/app-release.apk` for GitHub Releases.
- Do not upload `app-debug.apk` for public releases.
- Do not upload `app-release-unsigned.apk`.
- GitHub Release assets should be named `NumberAdditionSubtractionAndroid-vX.Y.Z-release.apk`.
- Never commit or upload keystore files, `keystore.properties`, passwords, mapping files, or unsigned APKs.

### Release signing setup

Create a private release keystore outside Git, then copy `keystore.properties.example` to `keystore.properties` and fill in the real values. Both `keystore.properties` and `*.jks` are ignored by Git.

Required `keystore.properties` keys:

```properties
storeFile=release/number-addition-subtraction-upload-key.jks
storePassword=private-store-password
keyAlias=number-addition-subtraction
keyPassword=private-key-password
```

Equivalent environment variables are also supported:

```powershell
$env:NUMBER_MATH_RELEASE_STORE_FILE = 'release/number-addition-subtraction-upload-key.jks'
$env:NUMBER_MATH_RELEASE_STORE_PASSWORD = 'private-store-password'
$env:NUMBER_MATH_RELEASE_KEY_ALIAS = 'number-addition-subtraction'
$env:NUMBER_MATH_RELEASE_KEY_PASSWORD = 'private-key-password'
```

If signing inputs are missing, `assembleRelease` intentionally fails so an unsigned APK is not accidentally published.

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

### 1) Build installable release APK

```bash
./gradlew assembleRelease
```

Windows PowerShell:

```powershell
.\gradlew.bat assembleRelease
```

Output file:

- `app/build/outputs/apk/release/app-release.apk`

Do **not** use this file for public release in this project:

- `app/build/outputs/apk/release/app-release-unsigned.apk`
- `app/build/outputs/apk/debug/app-debug.apk`

Optional file and hash check:

```powershell
$apk = ".\app\build\outputs\apk\release\app-release.apk"
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
& $apksigner.FullName verify --verbose --print-certs .\app\build\outputs\apk\release\app-release.apk
```

Expected verification includes:

```text
Verifies
Verified using v2 scheme (APK Signature Scheme v2): true
Number of signers: 1
```

The signer certificate must be the private release key, not `CN=Android Debug`.

### 2) Publish to GitHub Release

Use tag format `vX.Y.Z` and upload the signed release APK as release asset.

```powershell
Copy-Item ".\app\build\outputs\apk\release\app-release.apk" ".\app\build\outputs\apk\release\NumberAdditionSubtractionAndroid-vX.Y.Z-release.apk" -Force
gh release upload vX.Y.Z ".\app\build\outputs\apk\release\NumberAdditionSubtractionAndroid-vX.Y.Z-release.apk" --clobber
```

If wrong asset was uploaded before, delete it first:

```powershell
gh release delete-asset vX.Y.Z app-release-unsigned.apk --yes
gh release delete-asset vX.Y.Z app-debug.apk --yes
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

The release asset must contain exactly the signed release APK for that version, for example:

```text
NumberAdditionSubtractionAndroid-v1.0.2-release.apk
```

Verify the uploaded asset hash if needed:

```powershell
$localHash = (Get-FileHash ".\app\build\outputs\apk\release\app-release.apk" -Algorithm SHA256).Hash.ToLowerInvariant()
$release = gh release view vX.Y.Z --json assets | ConvertFrom-Json
$asset = $release.assets | Where-Object { $_.name -eq "NumberAdditionSubtractionAndroid-vX.Y.Z-release.apk" }
$remoteHash = $asset.digest -replace '^sha256:', ''
[pscustomobject]@{ LocalHash = $localHash; RemoteHash = $remoteHash; Match = ($localHash -eq $remoteHash) }
```

### 4) Installation notes

- Devices that already installed a debug-signed APK may reject the release-signed APK because the signing key changed. Uninstall the old app first if installation fails due to signature mismatch.
- Android rejects downgrade installs when the new APK `versionCode` is less than or equal to the installed app's `versionCode`; increment `versionCode` for every release.
- Keep the same private release keystore for all future public releases. Losing or changing it prevents normal updates over existing release-signed installs.
- Never upload keystore files, `keystore.properties`, passwords, mapping files, or unsigned APKs to GitHub Releases.

## Architecture

- Single-module app (`app/`)
- Package: `com.example.numberadditionsubtractionandroid`
- Entry point: `MainActivity` — uses `ComponentActivity` with Compose `setContent`
- Theme defined in `ui/theme/` (Color.kt, Theme.kt, Type.kt)
- Dependencies managed via version catalog in `gradle/libs.versions.toml`
