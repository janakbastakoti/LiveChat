package com.ekghanti.livechatapp

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ekghanti.livechat.LiveChat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val btn1: Button = findViewById(R.id.btn1)
        btn1.setOnClickListener {

            val intent = Intent(this, PageOne::class.java)
            startActivity(intent)


        }

        val btn2: Button = findViewById(R.id.btn2)
        btn2.setOnClickListener {

            val intent = Intent(this, PageTwo::class.java)
            startActivity(intent)

        }

        //val btn3: Button = findViewById(R.id.btn3)
        //btn3.setOnClickListener {
        //    val bundle = Bundle().apply {
        //        putString("channelId", "fd0600bb-582c-4143-b764-06f75ff38991")
        //        putString("title", "Chat Bot New")
        //        putString("subTitle", "Hello world!")
        //        putInt("icon", R.drawable.logo)
        //    }
        //
        //    // Create an instance of LiveChat (BottomSheetDialogFragment)
        //    val liveChatBottomSheet = LiveChat().apply {
        //        arguments = bundle
        //    }
        //
        //    // Show the BottomSheetDialogFragment
        //    liveChatBottomSheet.show(supportFragmentManager, liveChatBottomSheet.tag)
        //
        //    // Wait for the fragment to be displayed and then adjust the height
        //    liveChatBottomSheet.dialog?.setOnShowListener { dialog ->
        //        val bottomSheetDialog = dialog as BottomSheetDialog
        //        val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        //
        //        bottomSheet?.let {
        //            // Set the bottom sheet to be full height
        //            val layoutParams = it.layoutParams
        //            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT // Set to full height
        //            it.layoutParams = layoutParams
        //
        //            // Optional: Set a background color if needed
        //            // it.setBackgroundColor(Color.WHITE)
        //
        //            // Expand the bottom sheet
        //            BottomSheetBehavior.from(it).state = BottomSheetBehavior.STATE_EXPANDED
        //        }
        //    }
        //}





    }


}