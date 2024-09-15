package com.ekghanti.livechat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.fragment.app.Fragment

class LiveChatWebview: Fragment(R.layout.livechat_webview) {
    private var baseUrl: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.livechat_webview, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        arguments?.let {
            baseUrl = it.getString("BaseUrl")
        }

        Log.e("baseUrl", baseUrl.toString())
        // Initialize the WebView and set its settings
        val webView: WebView = view.findViewById(R.id.webView)
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.mediaPlaybackRequiresUserGesture = false
        webSettings.allowFileAccess = true
        webSettings.setSupportZoom(true)

        // Load the URL
        webView.loadUrl(baseUrl.toString())
    }

}