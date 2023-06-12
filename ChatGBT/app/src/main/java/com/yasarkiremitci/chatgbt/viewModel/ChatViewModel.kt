package com.yasarkiremitci.chatgbt.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yasarkiremitci.chatgbt.database.ChatMessage
import com.yasarkiremitci.chatgbt.models.MessageModel
import com.yasarkiremitci.chatgbt.repository.ChatRepository
import kotlinx.coroutines.launch

class ChatViewModel(private val chatRepository: ChatRepository) : ViewModel() {

    private val _messageList = MutableLiveData<List<MessageModel>>()
    val messageList: LiveData<List<MessageModel>> get() = _messageList



    suspend fun insertMessage(message: ChatMessage) {
        viewModelScope.launch {
            chatRepository.insertMessage(message)
        }
    }


    suspend fun getAllMessages() {
        viewModelScope.launch {
            val chatMessages = chatRepository.getAllMessages()
            val messages = chatMessages.map { chatMessage ->
                MessageModel(
                    chatMessage.isUser,
                    chatMessage.message
                )
            }
            _messageList.value = messages
        }
    }


    fun botWelcomeMessage() {
        val message = MessageModel(false, "Hello! How can i help you?")
        _messageList.value = listOf(message)
    }

    fun sendMessage(messageText: String) {
        val message = MessageModel(true, messageText)
        _messageList.value = _messageList.value?.plus(message)
    }

    fun getChatMessages(messageText: String) {
        viewModelScope.launch {
            val messages = chatRepository.getChatMessages(messageText)
            messages?.let {
                val newMessages = _messageList.value?.plus(it)
                _messageList.value = newMessages!!
            }
        }
    }

    fun removeLastMessage() {
        val currentMessages = _messageList.value.orEmpty()
        if (currentMessages.isNotEmpty()) {
            val updatedMessages = currentMessages.toMutableList()
            updatedMessages.removeAt(updatedMessages.size - 1)
            _messageList.value = updatedMessages
        }
    }
}
