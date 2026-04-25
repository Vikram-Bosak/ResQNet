package com.resqnet.domain.usecases

import com.resqnet.data.models.Message
import com.resqnet.data.models.MessagePriority
import com.resqnet.data.models.MessageStatus
import com.resqnet.data.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke(
        senderId: String,
        senderName: String,
        receiverId: String?,
        content: String,
        priority: MessagePriority = MessagePriority.NORMAL,
        location: com.resqnet.data.models.LocationData? = null
    ): Message {
        val message = messageRepository.createMessage(
            senderId = senderId,
            senderName = senderName,
            receiverId = receiverId,
            content = content,
            priority = priority,
            location = location
        )
        messageRepository.insertMessage(message)
        return message
    }
}

class GetMessagesUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    operator fun invoke(): Flow<List<Message>> = messageRepository.getAllMessages()
}

class GetConversationUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    operator fun invoke(senderId: String, receiverId: String): Flow<List<Message>> =
        messageRepository.getConversation(senderId, receiverId)
}

class GetPriorityMessagesUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    operator fun invoke(priority: MessagePriority): Flow<List<Message>> =
        messageRepository.getMessagesByPriority(priority)
}

class UpdateMessageStatusUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke(messageId: String, status: MessageStatus) {
        messageRepository.getMessageById(messageId)?.let { message ->
            messageRepository.updateMessage(message.copy(status = status))
        }
    }
}

class RelayMessageUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke(messageId: String, relayDeviceId: String): Message? {
        val originalMessage = messageRepository.getMessageById(messageId) ?: return null
        val relayedMessage = messageRepository.createRelayedMessage(
            originalMessage = originalMessage,
            relayDeviceId = relayDeviceId
        )
        messageRepository.updateMessage(relayedMessage)
        return relayedMessage
    }
}

class DeleteOldMessagesUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke(daysToKeep: Int = 30) {
        val timestamp = System.currentTimeMillis() - (daysToKeep * 24 * 60 * 60 * 1000L)
        messageRepository.deleteOldMessages(timestamp)
    }
}
