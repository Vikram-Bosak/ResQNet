package com.resqnet.utils.helpers

import android.content.Context
import android.location.Location
import com.resqnet.data.models.LocationData
import java.text.SimpleDateFormat
import java.util.*

object LocationHelper {

    /**
     * Convert Android Location to LocationData
     */
    fun toLocationData(location: Location): LocationData {
        return LocationData(
            latitude = location.latitude,
            longitude = location.longitude,
            accuracy = location.accuracy,
            timestamp = location.time
        )
    }

    /**
     * Calculate distance between two locations in meters
     */
    fun calculateDistance(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Float {
        val results = FloatArray(1)
        Location.distanceBetween(lat1, lon1, lat2, lon2, results)
        return results[0]
    }

    /**
     * Format location as string
     */
    fun formatLocation(location: LocationData): String {
        return "${String.format("%.6f", location.latitude)}, ${String.format("%.6f", location.longitude)}"
    }

    /**
     * Get formatted timestamp
     */
    fun formatTimestamp(timestamp: Long): String {
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}

object PermissionHelper {

    /**
     * Check if all required permissions are granted
     */
    fun hasAllPermissions(context: Context, permissions: Array<String>): Boolean {
        return permissions.all {
            android.content.pm.PackageManager.PERMISSION_GRANTED ==
            context.checkSelfPermission(it)
        }
    }

    /**
     * Get missing permissions
     */
    fun getMissingPermissions(context: Context, permissions: Array<String>): List<String> {
        return permissions.filter {
            context.checkSelfPermission(it) != android.content.pm.PackageManager.PERMISSION_GRANTED
        }
    }
}

object NetworkHelper {

    /**
     * Convert RSSI to signal strength percentage
     */
    fun rssiToPercentage(rssi: Int): Int {
        val minRssi = -100
        val maxRssi = -50
        val normalized = ((rssi - minRssi).toFloat() / (maxRssi - minRssi)) * 100
        return normalized.coerceIn(0f, 100f).toInt()
    }

    /**
     * Get signal strength label
     */
    fun getSignalStrengthLabel(rssi: Int): String {
        val percentage = rssiToPercentage(rssi)
        return when {
            percentage > 70 -> "Strong"
            percentage > 40 -> "Medium"
            else -> "Weak"
        }
    }

    /**
     * Check if device is within range
     */
    fun isWithinRange(rssi: Int): Boolean {
        return rssiToPercentage(rssi) > 10
    }
}

object MessageHelper {

    /**
     * Generate unique message ID
     */
    fun generateMessageId(): String {
        return "${System.currentTimeMillis()}-${(0..9999).random()}"
    }

    /**
     * Check if message should be relayed
     */
    fun shouldRelayMessage(
        relayPath: List<String>,
        currentDeviceId: String,
        hopLimit: Int = 10
    ): Boolean {
        // Don't relay if already relayed by this device
        if (relayPath.contains(currentDeviceId)) {
            return false
        }

        // Don't relay if hop limit exceeded
        if (relayPath.size >= hopLimit) {
            return false
        }

        return true
    }

    /**
     * Truncate message if too long
     */
    fun truncateMessage(message: String, maxLength: Int = 1000): String {
        return if (message.length > maxLength) {
            message.substring(0, maxLength - 3) + "..."
        } else {
            message
        }
    }
}
