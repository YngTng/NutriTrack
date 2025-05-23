package com.example.a3_yangtang33840180.data.genAI

import android.content.Context
import kotlinx.coroutines.flow.Flow

class MessageRepository {
    var messageDAO: MessageDAO

    constructor(context: Context) {
        messageDAO = MessageDatabase.getDatabase(context).messageDao()
    }

    suspend fun insertMessage(message: Message) {
        messageDAO.insertMessage(message)
    }

    suspend fun updateMessage(message: Message) {
        messageDAO.updateMessage(message)
    }

    suspend fun deleteAllMessages() {
        messageDAO.deleteAllMessages()
    }

    fun getAllMessages(): Flow<List<Message>> = messageDAO.getAllMessages()

}