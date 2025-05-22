package com.example.a3_yangtang33840180.genAI

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MessageViewModel(context: Context): ViewModel() {
    private val messageRepository: MessageRepository = MessageRepository(context)
    val allMessages: Flow<List<Message>> = messageRepository.getAllMessages()

    fun insertMessage(message: Message) = viewModelScope.launch {
        messageRepository.insertMessage(message)
    }

    fun updateMessage(message: Message) = viewModelScope.launch {
        messageRepository.updateMessage(message)
    }


    fun deleteMessageById(messageId: Int) = viewModelScope.launch {
        messageRepository.deleteMessageById(messageId)
    }

    fun deleteAllMessages() = viewModelScope.launch {
        messageRepository.deleteAllMessages()
    }

    class MessageViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val context = context.applicationContext
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            MessageViewModel(context) as T
    }

}