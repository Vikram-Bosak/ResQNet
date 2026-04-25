package com.resqnet.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Message data model for offline communication
 */
@Entity(tableName = "messages")
data class Message(
    @PrimaryKey
    val id: String,
    val senderId: String,
    val senderName: String,
    val receiverId: String?,
    val content: String,
    val timestamp: Long,
    val status: MessageStatus,
    val priority: MessagePriority,
    val location: LocationData?,
    val isRelayed: Boolean = false,
    val relayPath: List<String> = emptyList(),
    val messageType: MessageType = MessageType.TEXT
)

enum class MessageStatus {
    SENT,
    DELIVERED,
    RELAYED,
    FAILED
}

enum class MessagePriority {
    SOS,
    HIGH,
    NORMAL,
    LOW
}

enum class MessageType {
    TEXT,
    VOICE,
    LOCATION,
    EMERGENCY
}

data class LocationData(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float,
    val timestamp: Long
)
