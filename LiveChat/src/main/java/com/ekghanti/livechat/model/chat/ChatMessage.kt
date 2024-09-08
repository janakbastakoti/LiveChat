package com.ekghanti.livechat.model.chat

data class ChatMessage(
    val chatSide: String,
    val displayType: String,
    val message: String
)