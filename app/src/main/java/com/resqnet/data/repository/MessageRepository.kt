package com.resqnet.data.repository

import com.resqnet.data.database.MessageDao
import com.resqnet.data.models.Message
import com.resqnet.data.models.MessagePriority
import com.resqnet.data.models.MessageStatus
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageRepository @Inject constructor(
    private val messageDao: MessageDao
) {

    fun getAllMessages(): Flow<List<Message>> = messageDao.getAllMessages()

    fun getConversation(senderId: String, receiverId: String): Flow<List<Message>> =
        messageDao.getConversation(senderId, receiverId)

    fun getMessagesByPriority(priority: MessagePriority): Flow<List<Message>> =
        messageDao.getMessagesByPriority(priority)

    fun getMessagesByStatus(status: MessageStatus): Flow<List<Message>> =
        messageDao.getMessagesByStatus(status)

    suspend fun insertMessage(message: Message) = messageDao.insertMessage(message)

    suspend fun insertMessages(messages: List<Message>) = messageDao.insertMessages(messages)

    suspend fun updateMessage(message: Message) = messageDao.updateMessage(message)

    suspend fun deleteMessage(message: Message) = messageDao.deleteMessage(message)

    suspend fun deleteMessageById(messageId: String) = messageDao.deleteMessageById(messageId)

    suspend fun getMessageById(messageId: String): Message? = messageDao.getMessageById(messageId)

    suspend fun getMessageCount(): Int = messageDao.getMessageCount()

    suspend fun deleteOldMessages(timestamp: Long) = messageDao.deleteOldMessages(timestamp)

    /**
     * Create a new message with auto-generated ID
     */
    fun createMessage(
        senderId: String,
        senderName: String,
        receiverId: String?,
        content: String,
        priority: MessagePriority = MessagePriority.NORMAL,
        location: com.resqnet.data.models.LocationData? = null
    ): Message {
        return Message(
            id = UUID.randomUUID().toString(),
            senderId = senderId,
            senderName = senderName,
            receiverId = receiverId,
            content = content,
            timestamp = System.currentTimeMillis(),
            status = MessageStatus.SENT,
            priority = priority,
            location = location,
            isRelayed = false,
            relayPath = emptyList()
        )
    }

    /**
     * Create a relayed message
     */
    fun createRelayedMessage(
        originalMessage: Message,
        relayDeviceId: String
    ): Message {
        return originalMessage.copy(
            isRelayed = true,
            relayPath = originalMessage.relayPath + relayDeviceId
        )
    }
}
