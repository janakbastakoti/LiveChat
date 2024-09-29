package com.ekghanti.livechatapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ekghanti.livechat.LiveChat


class PageTwo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_page_two)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        val bundle = Bundle()
        bundle.putString("channelId", "772f2b31-14cd-431d-905b-bda1ab8292a0")
        bundle.putString("title", "Chat Bot New")
        bundle.putString("subTitle", "Hello world!")
        bundle.putInt("icon", R.drawable.logo)
        val liveChatWebviewFragment = LiveChat()
        liveChatWebviewFragment.arguments = bundle

        val fragmentManager = supportFragmentManager
        val fragmentTransction = fragmentManager.beginTransaction()
        fragmentTransction.replace(R.id.livechatlayout, liveChatWebviewFragment)
        fragmentTransction.commit()

    }
}