package com.resqnet.data.database.converters

import androidx.room.TypeConverter
import com.resqnet.data.models.*

class MessageStatusConverter {
    @TypeConverter
    fun fromMessageStatus(status: MessageStatus): String {
        return status.name
    }

    @TypeConverter
    fun toMessageStatus(value: String): MessageStatus {
        return try {
            MessageStatus.valueOf(value)
        } catch (e: IllegalArgumentException) {
            MessageStatus.SENT
        }
    }
}

class MessagePriorityConverter {
    @TypeConverter
    fun fromMessagePriority(priority: MessagePriority): String {
        return priority.name
    }

    @TypeConverter
    fun toMessagePriority(value: String): MessagePriority {
        return try {
            MessagePriority.valueOf(value)
        } catch (e: IllegalArgumentException) {
            MessagePriority.NORMAL
        }
    }
}

class MessageTypeConverter {
    @TypeConverter
    fun fromMessageType(type: MessageType): String {
        return type.name
    }

    @TypeConverter
    fun toMessageType(value: String): MessageType {
        return try {
            MessageType.valueOf(value)
        } catch (e: IllegalArgumentException) {
            MessageType.TEXT
        }
    }
}

class DeviceTypeConverter {
    @TypeConverter
    fun fromDeviceType(type: DeviceType): String {
        return type.name
    }

    @TypeConverter
    fun toDeviceType(value: String): DeviceType {
        return try {
            DeviceType.valueOf(value)
        } catch (e: IllegalArgumentException) {
            DeviceType.OTHER
        }
    }
}

class ConnectionTypeConverter {
    @TypeConverter
    fun fromConnectionType(type: ConnectionType): String {
        return type.name
    }

    @TypeConverter
    fun toConnectionType(value: String): ConnectionType {
        return try {
            ConnectionType.valueOf(value)
        } catch (e: IllegalArgumentException) {
            ConnectionType.BLUETOOTH
        }
    }
}

class LocationDataConverter {
    @TypeConverter
    fun fromLocationData(location: LocationData?): String {
        return if (location != null) {
            "${location.latitude},${location.longitude},${location.accuracy},${location.timestamp}"
        } else {
            ""
        }
    }

    @TypeConverter
    fun toLocationData(value: String): LocationData? {
        return if (value.isEmpty()) {
            null
        } else {
            val parts = value.split(",")
            LocationData(
                latitude = parts[0].toDouble(),
                longitude = parts[1].toDouble(),
                accuracy = parts[2].toFloat(),
                timestamp = parts[3].toLong()
            )
        }
    }
}

class StringListConverter {
    @TypeConverter
    fun fromStringList(list: List<String>?): String {
        return list?.joinToString(",") ?: ""
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return if (value.isEmpty()) {
            emptyList()
        } else {
            value.split(",")
        }
    }
}
