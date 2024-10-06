package com.ekghanti.livechatapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ekghanti.livechat.LiveChatWebview
import com.ekghanti.livechatapp.databinding.ActivityMainBinding

class PageOne : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_page_one)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val bundle = Bundle()
        bundle.putString("BaseUrl", "https://chat.orbit360.cx:8443/chat/772f2b31-14cd-431d-905b-bda1ab8292a0")
        val liveChatWebviewFragment = LiveChatWebview()
        liveChatWebviewFragment.arguments = bundle
        val fragmentManager = supportFragmentManager
        val fragmentTransction = fragmentManager.beginTransaction()
        fragmentTransction.replace(R.id.webviewlayout, liveChatWebviewFragment)
        fragmentTransction.commit()



    }
}