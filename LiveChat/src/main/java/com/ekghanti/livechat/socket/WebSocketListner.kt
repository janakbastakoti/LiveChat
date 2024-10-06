package com.ekghanti.livechat.socket

import android.util.Log
import com.ekghanti.livechat.helper.Helper
import com.ekghanti.livechat.model.chat.Message
import com.google.gson.Gson
import okhttp3.Response
import okhttp3.WebSocket
import  okhttp3.WebSocketListener
import org.json.JSONObject
import android.content.Context


class WebSocketListener(
    private val onMessageReceived: (Message) -> Unit,
    private var chatInstanceId: String,
    private var channelId: String,
    private var userName: String,
    private val context: Context
) : WebSocketListener() {

    private var webSocketTemp: WebSocket? = null
    private val gson = Gson()
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
        Log.e("channelId::::", channelId)
        webSocket.send(msg.toString())
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        val message = gson.fromJson(text, Message::class.java)
        chatInstanceId = message?.instanceId.toString()
        saveInstanceIdToLocal(chatInstanceId)
        userName = message?.destinationInfo?.userInfo?.usernameCookie.toString()
        onMessageReceived(message)
        //Log.e("received", "message is received ${text}")
        //Log.e("update id", chatInstanceId.toString())
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

        val appendMessage = gson.fromJson(jsonString.toString(), Message::class.java)

        onMessageReceived(appendMessage)

        val msg = JSONObject().apply {
            put("firstMsg", message)
            put("usernameCookie", userName)
            put("message", message)
            put("chatInstanceId", chatInstanceId)
            put("channelID", channelId)
            put("type", if (type == "feedback") "text" else type)
        }


        if (type == "feedback") {
            closeWebSocket()
        } else webSocketTemp?.send(msg.toString())


        Log.e("msg json", msg.toString())

    }

    fun closeWebSocket() {
        try {
            if (webSocketTemp != null) {
                webSocketTemp?.close(1000, "Closing socket")
                webSocketTemp = null // Clear the reference to avoid further use
            }
        } catch (e: Exception) {
            println("Error closing WebSocket: ${e.message}")
        }
    }

    private fun saveInstanceIdToLocal(instanceId: String?) {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("CHAT_INSTANCE_ID", instanceId)
        editor.apply()
    }

}