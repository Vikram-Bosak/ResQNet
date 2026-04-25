# ResQNet Project Structure

## Directory Overview

```
ResQNet/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/resqnet/
│   │       │   ├── data/
│   │       │   │   ├── models/
│   │       │   │   │   ├── Message.kt
│   │       │   │   │   └── Device.kt
│   │       │   │   ├── database/
│   │       │   │   │   ├── ResQNetDatabase.kt
│   │       │   │   │   ├── MessageDao.kt
│   │       │   │   │   ├── DeviceDao.kt
│   │       │   │   │   └── converters/
│   │       │   │   │       └── TypeConverters.kt
│   │       │   │   └── repository/
│   │       │   │       ├── MessageRepository.kt
│   │       │   │       └── DeviceRepository.kt
│   │       │   ├── domain/
│   │       │   │   └── usecases/
│   │       │   │       ├── MessageUseCases.kt
│   │       │   │       └── DeviceUseCases.kt
│   │       │   ├── network/
│   │       │   │   ├── bluetooth/
│   │       │   │   │   └── BluetoothManager.kt
│   │       │   │   └── wifidirect/
│   │       │   │       └── WiFiDirectManager.kt
│   │       │   ├── ui/
│   │       │   │   ├── activities/
│   │       │   │   │   ├── MainActivity.kt
│   │       │   │   │   ├── MainViewModel.kt
│   │       │   │   │   ├── ChatActivity.kt
│   │       │   │   │   ├── ChatViewModel.kt
│   │       │   │   │   ├── NearbyDevicesActivity.kt
│   │       │   │   │   ├── NearbyDevicesViewModel.kt
│   │       │   │   │   ├── BroadcastActivity.kt
│   │       │   │   │   ├── BroadcastViewModel.kt
│   │       │   │   │   ├── MapActivity.kt
│   │       │   │   │   ├── MapViewModel.kt
│   │       │   │   │   ├── EmergencyModeActivity.kt
│   │       │   │   │   ├── EmergencyModeViewModel.kt
│   │       │   │   │   ├── SettingsActivity.kt
│   │       │   │   │   └── SettingsViewModel.kt
│   │       │   │   └── adapters/
│   │       │   │       ├── MessageAdapter.kt
│   │       │   │       └── DeviceAdapter.kt
│   │       │   ├── utils/
│   │       │   │   ├── constants/
│   │       │   │   │   └── AppConstants.kt
│   │       │   │   └── helpers/
│   │       │   │       └── HelperFunctions.kt
│   │       │   ├── di/
│   │       │   │   ├── DatabaseModule.kt
│   │       │   │   └── NetworkModule.kt
│   │       │   └── ResQNetApplication.kt
│   │       ├── res/
│   │       │   ├── layout/
│   │       │   │   ├── activity_main.xml
│   │       │   │   ├── activity_chat.xml
│   │       │   │   ├── activity_nearby_devices.xml
│   │       │   │   ├── activity_broadcast.xml
│   │       │   │   ├── activity_map.xml
│   │       │   │   ├── activity_emergency_mode.xml
│   │       │   │   ├── activity_settings.xml
│   │       │   │   ├── item_message.xml
│   │       │   │   └── item_device.xml
│   │       │   ├── drawable/
│   │       │   │   ├── sos_button.xml
│   │       │   │   ├── sos_glow.xml
│   │       │   │   ├── action_button.xml
│   │       │   │   ├── emergency_toggle.xml
│   │       │   │   ├── nav_item.xml
│   │       │   │   ├── nav_item_active.xml
│   │       │   │   ├── input_field.xml
│   │       │   │   ├── message_bubble.xml
│   │       │   │   └── device_item.xml
│   │       │   ├── values/
│   │       │   │   ├── strings.xml
│   │       │   │   ├── colors.xml
│   │       │   │   └── themes.xml
│   │       │   └── mipmap-*/
│   │       └── AndroidManifest.xml
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── .gitignore
├── README.md
└── BUILD.md
```

## Architecture Layers

### 1. Data Layer
- **Models**: Data structures (Message, Device)
- **Database**: Room database with DAOs
- **Repository**: Data access and caching

### 2. Domain Layer
- **Use Cases**: Business logic and operations
- Clean separation from data and UI layers

### 3. Network Layer
- **BluetoothManager**: Bluetooth LE scanning and connection
- **WiFiDirectManager**: WiFi P2P discovery and connection

### 4. UI Layer
- **Activities**: Screen implementations
- **ViewModels**: UI state management
- **Adapters**: RecyclerView adapters

### 5. Utils Layer
- **Constants**: App-wide constants
- **Helpers**: Utility functions

### 6. DI Layer
- **DatabaseModule**: Database dependencies
- **NetworkModule**: Network dependencies

## Key Components

### Core Features
1. **SOS System** - MainActivity + MainViewModel
2. **Messaging** - ChatActivity + ChatViewModel
3. **Device Discovery** - NearbyDevicesActivity + NearbyDevicesViewModel
4. **Broadcast** - BroadcastActivity + BroadcastViewModel
5. **Emergency Mode** - EmergencyModeActivity + EmergencyModeViewModel
6. **Settings** - SettingsActivity + SettingsViewModel

### Data Flow
1. User Action → Activity
2. Activity → ViewModel
3. ViewModel → Use Case
4. Use Case → Repository
5. Repository → Database/Network
6. Result flows back through LiveData/Flow

### Network Flow
1. BluetoothManager/WiFiDirectManager scans for devices
2. Devices discovered and stored in database
3. Messages sent via Bluetooth/WiFi Direct
4. Messages relayed across mesh network
5. Results updated in UI

## File Count Summary

- **Kotlin Files**: 30+
- **XML Layouts**: 10+
- **Drawable Resources**: 10+
- **Value Resources**: 3
- **Total Files**: 60+

## Dependencies

### Core
- AndroidX Core KTX
- Material Components
- ConstraintLayout

### Architecture
- Lifecycle (ViewModel, LiveData)
- Room Database
- Hilt (Dependency Injection)
- Navigation

### Network
- Play Services Location

### Testing
- JUnit
- Espresso
- AndroidX Test

## Build Configuration

- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **Compile SDK**: 34
- **Kotlin Version**: 1.9.20
- **Gradle Version**: 8.2

## Permissions Required

- BLUETOOTH
- BLUETOOTH_ADMIN
- BLUETOOTH_SCAN
- BLUETOOTH_CONNECT
- ACCESS_FINE_LOCATION
- ACCESS_COARSE_LOCATION
- ACCESS_WIFI_STATE
- CHANGE_WIFI_STATE
- INTERNET
- ACCESS_NETWORK_STATE
- RECORD_AUDIO
- POST_NOTIFICATIONS
