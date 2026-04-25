# Build Instructions for ResQNet

## Prerequisites

- Android Studio Arctic Fox or later
- Android SDK 24 (Android 7.0) or higher
- JDK 17 or higher
- Gradle 8.2 or higher

## Building the Project

### Using Android Studio

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/ResQNet.git
   cd ResQNet
   ```

2. **Open in Android Studio**
   - File → Open
   - Select the ResQNet folder
   - Wait for Gradle sync to complete

3. **Configure SDK**
   - File → Project Structure → SDK Location
   - Set Android SDK location if not already configured

4. **Build the project**
   - Build → Make Project (Ctrl+F9)
   - Or Build → Rebuild Project

5. **Run on device/emulator**
   - Connect an Android device or start an emulator
   - Click the Run button (Shift+F10)
   - Or Run → Run 'app'

### Using Command Line

1. **Navigate to project directory**
   ```bash
   cd ResQNet
   ```

2. **Clean build**
   ```bash
   ./gradlew clean
   ```

3. **Build debug APK**
   ```bash
   ./gradlew assembleDebug
   ```

4. **Build release APK**
   ```bash
   ./gradlew assembleRelease
   ```

5. **Install on connected device**
   ```bash
   ./gradlew installDebug
   ```

## Build Variants

### Debug Build
- Not minified
- Includes debug symbols
- Faster build time
- Larger APK size

```bash
./gradlew assembleDebug
```

Output: `app/build/outputs/apk/debug/app-debug.apk`

### Release Build
- Minified with ProGuard
- Optimized
- Smaller APK size
- Requires signing

```bash
./gradlew assembleRelease
```

Output: `app/build/outputs/apk/release/app-release.apk`

## Signing the Release APK

### 1. Generate a keystore
```bash
keytool -genkey -v -keystore resqnet-release.keystore -alias resqnet -keyalg RSA -keysize 2048 -validity 10000
```

### 2. Configure signing in `app/build.gradle.kts`
```kotlin
android {
    signingConfigs {
        create("release") {
            storeFile = file("path/to/resqnet-release.keystore")
            storePassword = "your-store-password"
            keyAlias = "resqnet"
            keyPassword = "your-key-password"
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}
```

### 3. Build signed release APK
```bash
./gradlew assembleRelease
```

## Testing

### Run Unit Tests
```bash
./gradlew test
```

### Run Instrumented Tests
```bash
./gradlew connectedAndroidTest
```

### Run Lint Checks
```bash
./gradlew lint
```

## Troubleshooting

### Gradle Sync Issues
- File → Invalidate Caches → Invalidate and Restart
- Delete `.gradle` and `.idea` folders
- Reopen project

### SDK Issues
- SDK Manager → Install required SDK versions
- Check `local.properties` for correct SDK path

### Build Errors
- Clean build: `./gradlew clean`
- Check for missing dependencies
- Update Gradle and Android Gradle Plugin

### Permission Issues
- Ensure all permissions are declared in `AndroidManifest.xml`
- Test on physical device (emulator may have limitations)

## Performance Optimization

### Enable Gradle Build Cache
Add to `gradle.properties`:
```properties
org.gradle.caching=true
org.gradle.configuration-cache=true
```

### Enable Parallel Build
Add to `gradle.properties`:
```properties
org.gradle.parallel=true
org.gradle.workers.max=4
```

### Increase JVM Memory
Add to `gradle.properties`:
```properties
org.gradle.jvmargs=-Xmx4096m -Dfile.encoding=UTF-8
```

## Continuous Integration

### GitHub Actions Example
```yaml
name: Build

on: [push, pull_request]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Set up JDK 17
        uses: actions/setup-java@v2
        with:
          java-version: '17'
          distribution: 'temurin'
      - name: Grant execute permission for gradlew
        run: chmod +x gradlew
      - name: Build with Gradle
        run: ./gradlew assembleDebug
```

## Deployment

### Google Play Store
1. Build signed release APK or AAB
2. Create app in Google Play Console
3. Upload APK/AAB
4. Fill in store listing
5. Submit for review

### Firebase App Distribution
```bash
./gradlew assembleDebug
firebase appdistribution:distribute app/build/outputs/apk/debug/app-debug.apk
```

### Direct Distribution
Share the APK file directly:
- Email
- Cloud storage (Google Drive, Dropbox)
- Website download

## Additional Resources

- [Android Build Documentation](https://developer.android.com/studio/build)
- [Gradle Documentation](https://docs.gradle.org/)
- [ProGuard Documentation](https://www.guardsquare.com/manual/home)
