package com.yasarkiremitci.chatgbt.models

data class ChatModel(
    val choices: List<Choice>
) {
    data class Choice(
        val message: Message
    )

    data class Message(
        val role: String,
        val content: String
    )
}

