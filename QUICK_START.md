# ResQNet Quick Start Guide

## 🚀 Get Started in 5 Minutes

### Step 1: Install Prerequisites

1. **Download Android Studio**
   - Visit: https://developer.android.com/studio
   - Install Android Studio Arctic Fox or later

2. **Install Android SDK**
   - Open Android Studio
   - SDK Manager → Install SDK Platform 24 or higher
   - Install Android SDK Build-Tools

3. **Setup JDK**
   - Android Studio includes JDK 17 by default
   - Or install JDK 17+ separately

### Step 2: Clone and Open Project

```bash
# Clone the repository
git clone https://github.com/yourusername/ResQNet.git
cd ResQNet

# Open in Android Studio
# File → Open → Select ResQNet folder
```

### Step 3: Build and Run

1. **Wait for Gradle Sync**
   - Android Studio will automatically sync Gradle
   - Wait for the sync to complete (may take a few minutes)

2. **Connect Device or Start Emulator**
   - Connect Android device via USB (enable USB debugging)
   - Or start an emulator from AVD Manager

3. **Run the App**
   - Click the green Run button (▶)
   - Or press Shift+F10
   - Or Run → Run 'app'

### Step 4: Grant Permissions

When the app first launches, grant these permissions:
- ✅ Location (Fine & Coarse)
- ✅ Bluetooth
- ✅ Nearby Devices

### Step 5: Test the App

#### Test SOS Feature
1. Tap the large red SOS button
2. Confirm the alert
3. Check that SOS message is sent

#### Test Messaging
1. Tap "Send Message"
2. Type a message
3. Send it

#### Test Device Discovery
1. Tap "Nearby Devices"
2. Wait for devices to appear
3. Tap on a device to chat

#### Test Broadcast
1. Tap "Broadcast Alert"
2. Select a template or type custom message
3. Send broadcast

#### Test Emergency Mode
1. Go to Settings
2. Enable Emergency Mode
3. Check battery optimization

## 📱 Demo Mode

To test with multiple devices:

1. **Install on 2-3 devices**
   - Build APK: `./gradlew assembleDebug`
   - Install on each device

2. **Enable Bluetooth & WiFi**
   - Enable on all devices
   - Keep devices within 10 meters

3. **Test Mesh Network**
   - Open app on all devices
   - Wait for devices to discover each other
   - Send messages between devices
   - Test relay functionality

## 🔧 Common Issues

### Issue: Gradle Sync Fails
**Solution:**
```bash
# Clean and rebuild
./gradlew clean
./gradlew build
```

### Issue: Bluetooth Not Working
**Solution:**
- Enable Bluetooth on device
- Grant Location permission
- Check device compatibility

### Issue: No Devices Found
**Solution:**
- Ensure Bluetooth is enabled
- Check Location permission
- Move devices closer together
- Wait 10-30 seconds for discovery

### Issue: App Crashes
**Solution:**
- Check Logcat for errors
- Ensure all permissions granted
- Try clearing app data

## 🎯 Next Steps

### Customize the App

1. **Change Colors**
   - Edit `app/src/main/res/values/colors.xml`

2. **Change Text**
   - Edit `app/src/main/res/values/strings.xml`

3. **Add Features**
   - Check `PROJECT_STRUCTURE.md` for architecture
   - Follow MVVM pattern

### Build for Production

1. **Generate Keystore**
   ```bash
   keytool -genkey -v -keystore resqnet.keystore -alias resqnet
   ```

2. **Configure Signing**
   - Edit `app/build.gradle.kts`
   - Add signing configuration

3. **Build Release APK**
   ```bash
   ./gradlew assembleRelease
   ```

### Deploy to Play Store

1. Create Google Play Developer account
2. Create app listing
3. Upload signed APK/AAB
4. Submit for review

## 📚 Learn More

- **Full Documentation**: See `README.md`
- **Build Instructions**: See `BUILD.md`
- **Project Structure**: See `PROJECT_STRUCTURE.md`

## 💡 Tips

### Performance
- Enable Gradle cache in `gradle.properties`
- Use release build for testing performance
- Monitor battery usage in emergency mode

### Testing
- Test on physical devices (emulator has limitations)
- Test with multiple devices for mesh network
- Test in different environments (indoor/outdoor)

### Debugging
- Use Android Studio Logcat
- Enable USB debugging
- Check database with App Inspection

## 🆘 Need Help?

1. **Check Documentation**
   - README.md for overview
   - BUILD.md for build issues
   - PROJECT_STRUCTURE.md for architecture

2. **Search Issues**
   - Check GitHub Issues
   - Search Stack Overflow

3. **Contact Support**
   - Open GitHub Issue
   - Contact development team

## ✅ Checklist

Before deploying:

- [ ] Test on multiple devices
- [ ] Test all features (SOS, messaging, broadcast)
- [ ] Test emergency mode
- [ ] Check battery usage
- [ ] Test without internet
- [ ] Verify permissions
- [ ] Build release APK
- [ ] Sign APK
- [ ] Test on target Android versions
- [ ] Update documentation

## 🎉 You're Ready!

You now have ResQNet running on your device. Start exploring the features and help build the offline emergency network!

For more detailed information, check out the full documentation in the project repository.
