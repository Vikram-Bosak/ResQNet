package com.resqnet.data.database

import androidx.room.*
import com.resqnet.data.models.Message
import com.resqnet.data.models.MessagePriority
import com.resqnet.data.models.MessageStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Query("SELECT * FROM messages ORDER BY timestamp DESC")
    fun getAllMessages(): Flow<List<Message>>

    @Query("SELECT * FROM messages WHERE senderId = :senderId OR receiverId = :receiverId ORDER BY timestamp DESC")
    fun getConversation(senderId: String, receiverId: String): Flow<List<Message>>

    @Query("SELECT * FROM messages WHERE priority = :priority ORDER BY timestamp DESC")
    fun getMessagesByPriority(priority: MessagePriority): Flow<List<Message>>

    @Query("SELECT * FROM messages WHERE status = :status ORDER BY timestamp DESC")
    fun getMessagesByStatus(status: MessageStatus): Flow<List<Message>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: Message)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<Message>)

    @Update
    suspend fun updateMessage(message: Message)

    @Delete
    suspend fun deleteMessage(message: Message)

    @Query("DELETE FROM messages WHERE id = :messageId")
    suspend fun deleteMessageById(messageId: String)

    @Query("DELETE FROM messages WHERE timestamp < :timestamp")
    suspend fun deleteOldMessages(timestamp: Long)

    @Query("SELECT * FROM messages WHERE id = :messageId")
    suspend fun getMessageById(messageId: String): Message?

    @Query("SELECT COUNT(*) FROM messages")
    suspend fun getMessageCount(): Int
}
