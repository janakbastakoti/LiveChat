package com.ekghanti.livechat.model.chat

data class ChatMessage(
    val chatSide: String,
    val displayType: String,
    val message: String
)

sealed class ChatContent {
    data class TextMessage(val text: String) : ChatContent()
    data class ListMessage(val messages: List<SubMessage>) : ChatContent()
}
