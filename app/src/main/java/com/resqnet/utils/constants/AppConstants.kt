package com.resqnet.utils.constants

object AppConstants {
    // App Info
    const val APP_NAME = "ResQNet"
    const val APP_VERSION = "1.0.0"

    // Network
    const val BLUETOOTH_SCAN_TIMEOUT = 10000L // 10 seconds
    const val WIFI_P2P_SCAN_TIMEOUT = 10000L // 10 seconds
    const val MAX_RETRY_ATTEMPTS = 3

    // Message
    const val MAX_MESSAGE_LENGTH = 1000
    const val MESSAGE_RELAY_HOP_LIMIT = 10
    const val MESSAGE_EXPIRY_DAYS = 30

    // Device
    const val DEVICE_TIMEOUT = 300000L // 5 minutes
    const val MAX_DEVICES = 50

    // Emergency
    const val SOS_COOLDOWN = 30000L // 30 seconds
    const val EMERGENCY_MODE_BATTERY_THRESHOLD = 20 // 20%

    // Location
    const val LOCATION_UPDATE_INTERVAL = 5000L // 5 seconds
    const val LOCATION_UPDATE_MIN_DISTANCE = 10f // 10 meters

    // UI
    const val ANIMATION_DURATION = 300L
    const val DEBOUNCE_DELAY = 500L
}

object NotificationChannels {
    const val SOS_CHANNEL = "sos_channel"
    const val MESSAGES_CHANNEL = "messages_channel"
    const val EMERGENCY_CHANNEL = "emergency_channel"
}

object Preferences {
    const val PREF_NAME = "resqnet_prefs"
    const val KEY_EMERGENCY_MODE = "emergency_mode"
    const val KEY_AUTO_RELAY = "auto_relay"
    const val KEY_BLUETOOTH_ENABLED = "bluetooth_enabled"
    const val KEY_WIFI_DIRECT_ENABLED = "wifi_direct_enabled"
    const val KEY_DEVICE_NAME = "device_name"
    const val KEY_LAST_SOS_TIME = "last_sos_time"
}
