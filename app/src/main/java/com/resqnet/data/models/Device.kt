package com.resqnet.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Device model for nearby mesh nodes
 */
@Entity(tableName = "devices")
data class Device(
    @PrimaryKey
    val deviceId: String,
    val deviceName: String,
    val deviceType: DeviceType,
    val connectionType: ConnectionType,
    val signalStrength: Int, // 0-100
    val distance: Float?, // in meters, null if unknown
    val lastSeen: Long,
    val isConnected: Boolean = false,
    val isEmergencyMode: Boolean = false
)

enum class DeviceType {
    ANDROID,
    IOS,
    OTHER
}

enum class ConnectionType {
    BLUETOOTH,
    WIFI_DIRECT,
    BOTH
}
