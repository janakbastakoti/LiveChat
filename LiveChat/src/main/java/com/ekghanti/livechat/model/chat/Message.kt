package com.ekghanti.livechat.model.chat

data class Message(
    val channelID: String,
    val chatMessage: ChatMessage,
    val destinationInfo: DestinationInfo,
    val instanceId: String,
    val messageId: String,
    val sourceInfo: SourceInfo,
    val timestamp: Double
)