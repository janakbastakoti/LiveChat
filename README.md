# LiveChat SDK

A  Kotlin-based live chat SDK for Android . Integrates customizable live chat functionality into mobile applications.

## Prerequisites

- Android Studio setup
- Kotlin enabled in your project
- Android minSdkVersion 21 or higher

### Summary:
This guide explains how to:
1. Add the Maven repository and SDK dependency.
2. Set up a container in an activity's layout.
3. Embed the LiveChat fragment and pass the `channelId` for initializing chat functionality.

You can modify the `channelId` based on your app’s specific configuration.

## Installation

### Step 1: Add Maven Repository

In your project's root `build.gradle` file, add the following code at the end of the `repositories` section:

```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
      <-- Add Me-->  maven { url 'https://jitpack.io' } <-- Add Me-->
    }
}
```

### Step 2. Add the dependency
```
dependencies {
    implementation 'com.github.janakbastakoti:LiveChat:Tag'
}
```
### use latest release Tag


## Usage


###  To Use Live Chatbot

## Step 1: Create the Layout

```
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/main"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <FrameLayout
        android:id="@+id/livechatlayout"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:layout_marginTop="16dp"
        android:layout_marginBottom="16dp"
        android:layout_marginStart="16dp"
        android:layout_marginEnd="16dp"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

## Step 2: Add LiveChat in MainActivity

```
package com.example.livechatapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.livechat.LiveChat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Apply padding to avoid system bars overlapping content
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize LiveChat with the channel ID
        val bundle = Bundle()
        bundle.putString("channelId", "YOUR CHATBOT CHANNEL ID") // Replace with your channel ID
        val liveChatWebviewFragment = LiveChat()
        liveChatWebviewFragment.arguments = bundle

        // Replace the container with the LiveChat fragment
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.livechatlayout, liveChatWebviewFragment)
        fragmentTransaction.commit()
    }
}

```





<!-- ###  To Use Webview Chatbot


## Step 1: Create the Layout

```
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="com.ekghanti.livechatapp.PageOne">

    <androidx.fragment.app.FragmentContainerView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:id="@+id/webviewlayout"
        />

</androidx.constraintlayout.widget.ConstraintLayout>
```
## Step 2: Add LiveChat in MainActivity
```
package com.example.livechatapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ekghanti.livechat.LiveChatWebview
import com.ekghanti.livechatapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
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
        bundle.putString("BaseUrl", "YOUR LIVECHAT URL")
        val liveChatWebviewFragment = LiveChatWebview()
        liveChatWebviewFragment.arguments = bundle
        val fragmentManager = supportFragmentManager
        val fragmentTransction = fragmentManager.beginTransaction()
        fragmentTransction.replace(R.id.webviewlayout, liveChatWebviewFragment)
        fragmentTransction.commit()

    }
}

``` -->




