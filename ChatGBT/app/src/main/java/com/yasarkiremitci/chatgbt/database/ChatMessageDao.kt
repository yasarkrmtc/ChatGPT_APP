package com.yasarkiremitci.chatgbt.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.yasarkiremitci.chatgbt.database.ChatMessage

@Dao
interface ChatMessageDao {
    @Insert
    suspend fun insert(message: ChatMessage)

    @Query("SELECT * FROM chat_messages")
    suspend fun getAllMessages(): List<ChatMessage>

    @Query("SELECT COUNT(*) FROM chat_messages")
    fun getMessageCount(): Int







}

