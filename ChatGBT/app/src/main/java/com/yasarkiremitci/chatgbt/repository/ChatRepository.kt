package com.yasarkiremitci.chatgbt.repository

import com.google.gson.Gson
import com.yasarkiremitci.chatgbt.api.ApiInterface
import com.yasarkiremitci.chatgbt.api.Util
import com.yasarkiremitci.chatgbt.database.ChatMessage
import com.yasarkiremitci.chatgbt.database.ChatMessageDao
import com.yasarkiremitci.chatgbt.models.ChatRequest
import com.yasarkiremitci.chatgbt.models.MessageModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

class ChatRepository(private val apiInterface: ApiInterface,private val chatDao: ChatMessageDao) {

    suspend fun insertMessage(message: ChatMessage) {
        chatDao.insert(message)
    }

    suspend fun getAllMessages(): List<ChatMessage> {
        return chatDao.getAllMessages()
    }


    suspend fun getChatMessages(message: String): ArrayList<MessageModel> = withContext(Dispatchers.IO) {
        val requestBody = RequestBody.create(
            "application/json".toMediaTypeOrNull(),
            Gson().toJson(
                ChatRequest(
                    model = "gpt-3.5-turbo",
                    messages = listOf(
                        ChatRequest.Message(
                            role = "user",
                            content = message
                        )
                    ),
                    temperature = 0.1,
                    max_tokens = 4000
                )
            )
        )

        try {
            val response = apiInterface.getChat("application/json", "Bearer ${Util.API_KEY}", requestBody)
            val textResponse = response.choices[0].message.content

            val replacedResponse = textResponse.replace("\n", "")
            //val responseMessage = ChatMessage(isUser = false, message = replacedResponse)

            val messageList = ArrayList<MessageModel>()
            messageList.add(MessageModel(isUser = false, message = replacedResponse))

            messageList
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        } as ArrayList<MessageModel>
    }
}
