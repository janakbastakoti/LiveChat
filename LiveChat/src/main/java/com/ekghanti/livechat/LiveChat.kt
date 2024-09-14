package com.ekghanti.livechat

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.ekghanti.livechat.adapter.ChatAdapter
import com.ekghanti.livechat.model.chat.Message
import okhttp3.WebSocket

class LiveChat: Fragment(R.layout.livechat) {

    private lateinit var webSocket: WebSocket
    private lateinit var myRecyclerView: RecyclerView
    private lateinit var myAdapter: ChatAdapter

    private val messageList: MutableList<Message> = mutableListOf()d
    private val PICK_IMAGE_REQUEST = 1

    private var imageUri: Uri? = null
    private var uploadedUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.livechat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)









    }







}