# ResQNet – Offline Emergency Network

A fully functional Android application that enables offline communication using Bluetooth and WiFi Direct mesh networking for emergency situations.

## 🎯 Features

### Core Features
- **SOS System**: One-tap SOS button that broadcasts emergency messages to all nearby devices with GPS location
- **Offline Messaging**: Peer-to-peer messaging that works without internet
- **Device Discovery**: Discover nearby devices via Bluetooth and WiFi Direct
- **Broadcast System**: Send messages to all connected devices with predefined emergency templates

### Advanced Features
- **Auto Relay System**: Messages automatically forwarded to other devices with multi-hop communication
- **Location Sharing**: GPS integration with location attachment to messages
- **Emergency Mode**: Battery saver mode that limits background processes
- **Priority Message Queue**: SOS messages processed first with priority-based queuing
- **Message Routing System**: Track message path and prevent infinite loops
- **Offline Storage**: Store messages locally using Room Database

## 🧱 Tech Stack

- **Language**: Kotlin
- **UI**: XML (Material Design)
- **Architecture**: MVVM (Model-View-ViewModel)
- **Local DB**: Room Database
- **Dependency Injection**: Hilt
- **Connectivity**:
  - Bluetooth API
  - WiFi Direct API
- **Location**: Google Play Services Location API

## 📂 Project Structure

```
app/src/main/java/com/resqnet/
├── data/
│   ├── models/          # Data models (Message, Device)
│   ├── database/       # Room database and DAOs
│   └── repository/     # Repository layer
├── domain/
│   └── usecases/       # Business logic use cases
├── network/
│   ├── bluetooth/      # Bluetooth manager
│   └── wifidirect/     # WiFi Direct manager
├── ui/
│   ├── activities/     # Activities
│   ├── fragments/      # Fragments
│   └── adapters/       # RecyclerView adapters
└── utils/
    ├── constants/      # App constants
    └── helpers/        # Helper functions
```

## 🚀 Getting Started

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK 24 or higher
- Kotlin 1.9.20 or higher

### Installation

1. Clone the repository:
```bash
git clone https://github.com/yourusername/ResQNet.git
cd ResQNet
```

2. Open the project in Android Studio

3. Sync Gradle files

4. Build and run on an Android device or emulator

### Permissions

The app requires the following permissions:
- `BLUETOOTH` and `BLUETOOTH_ADMIN` - For Bluetooth communication
- `BLUETOOTH_SCAN` and `BLUETOOTH_CONNECT` - For Bluetooth LE scanning
- `ACCESS_FINE_LOCATION` and `ACCESS_COARSE_LOCATION` - For device discovery and GPS
- `ACCESS_WIFI_STATE` and `CHANGE_WIFI_STATE` - For WiFi Direct
- `RECORD_AUDIO` - For voice SOS feature (optional)
- `POST_NOTIFICATIONS` - For emergency notifications

## 📱 Usage

### SOS Feature
1. Tap the large SOS button on the home screen
2. The app will broadcast an emergency message with your location
3. All nearby devices will receive the alert

### Messaging
1. Tap "Send Message" or select a device from "Nearby Devices"
2. Type your message and send
3. Messages are relayed across the mesh network

### Broadcast
1. Tap "Broadcast Alert"
2. Select a template or type a custom message
3. Send to all connected devices

### Emergency Mode
1. Go to Settings or tap the Emergency Mode toggle
2. Enable Emergency Mode to save battery
3. Only essential services remain active

## 🎨 Design System

The app uses a dark theme with high contrast colors for emergency situations:

- **Primary Red (#FF3B30)**: Reserved for SOS triggers and critical alerts
- **Secondary Blue (#0A1F44)**: Used for navigation elements
- **Success Green (#34C759)**: Status indicators
- **Warning Yellow (#FFD60A)**: Warning states
- **Background**: True black (#000000) for OLED battery saving

## 🧪 Testing

### Manual Testing
1. Install the app on multiple devices
2. Enable Bluetooth and WiFi Direct on all devices
3. Test message relay between devices
4. Test SOS functionality
5. Test emergency mode

### Demo Mode
The app includes a demo flow that shows:
- 2-3 devices connecting
- Message sending without internet
- Relay functionality

## 🔧 Configuration

### Build Variants
- `debug`: Development build with debugging enabled
- `release`: Production build with ProGuard optimization

### Customization
Edit `app/src/main/res/values/colors.xml` to customize the color scheme
Edit `app/src/main/res/values/strings.xml` to customize text strings

## 📝 License

This project is licensed under the MIT License.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📧 Support

For support, please open an issue on GitHub or contact the development team.

## 🙏 Acknowledgments

- Material Design Components
- Android Jetpack libraries
- Hilt Dependency Injection
- Room Database
