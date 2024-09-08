package com.example.ekghanti_livechat_sdk.socket

import android.util.Log
import com.ekghanti.livechat.helper.Helper
import com.ekghanti.livechat.model.chat.Message
import com.google.gson.Gson
import okhttp3.Response
import okhttp3.WebSocket
import  okhttp3.WebSocketListener
import org.json.JSONObject


class WebSocketListener(private val onMessageReceived: (Message) -> Unit) : WebSocketListener() {

    private var webSocketTemp: WebSocket? = null
    private val gson = Gson()
    private var chatInstanceId: String = ""
    private var channelId = "fd0caaa3-f1cb-4d0a-a452-171f21ec16ee"
    private var userName = ""
    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        this.webSocketTemp = webSocket
        val msg = JSONObject().apply {
            put("firstMsg", "client")
            put("usernameCookie", userName)
            put("message", null)
            put("chatInstanceId", chatInstanceId)
            put("channelID", channelId)
        }
//        chatInstanceId =
        Log.e("status", "socket is connected ${response.toString()} ${msg}")
        webSocket.send(msg.toString())
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        val message = gson.fromJson(text, Message::class.java)
        chatInstanceId = message?.instanceId.toString()
        userName = message?.destinationInfo?.userInfo?.usernameCookie.toString()
        onMessageReceived(message)
        Log.e("received", "message is received ${text}")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        Log.e("closing", "closing is received")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        Log.e("failure", "failure is received")
    }

    fun sendMessage(message: String, type: String, isButton: Boolean = false) {

        val helper = Helper()
        val jsonString = helper.makeChatMessage(chatInstanceId, channelId, message, type)
        //Log.e("Heloo printing", "message is sent ${jsonString}")
        val appendMessage = gson.fromJson(jsonString.toString(), Message::class.java)
        if(!isButton) onMessageReceived(appendMessage)

        val msg = JSONObject().apply {
            put("firstMsg", message)
            put("usernameCookie", userName)
            put("message", message)
            put("chatInstanceId", chatInstanceId)
            put("channelID", channelId)
            put("type", type)
        }

        webSocketTemp?.send(msg.toString())

    }

    fun onClickMe() {

        val msg = JSONObject().apply {
            put("firstMsg", "client")
            put("usernameCookie", userName)
            put("message", "Click Me")
            put("chatInstanceId", chatInstanceId)
            put("channelID", channelId)
            put("type", "text")
        }
        webSocketTemp?.send(msg.toString())
    }


}