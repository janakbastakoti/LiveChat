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

        val btn3: Button = findViewById(R.id.btn3)
        btn3.setOnClickListener {
            val bundle = Bundle().apply {
                putString("channelId", "772f2b31-14cd-431d-905b-bda1ab8292a0")
                putString("title", "Chat Bot New")
                putString("subTitle", "Hello world!")
                putInt("icon", R.drawable.logo)
            }

            // Create an instance of LiveChat (BottomSheetDialogFragment)
            val liveChatBottomSheet = LiveChat()
            liveChatBottomSheet.arguments = bundle


            // Wait for the fragment to be displayed and then adjust the height
            liveChatBottomSheet.dialog?.setOnShowListener { dialog ->
                val bottomSheetDialog = dialog as BottomSheetDialog
                val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                bottomSheet?.let {
                    val behavior = BottomSheetBehavior.from(it)

                    // Get the screen height
                    val displayMetrics = resources.displayMetrics
                    val screenHeight = displayMetrics.heightPixels

                    // Calculate the height as full height minus 10dp
                    val topPaddingInPx = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        10f,
                        resources.displayMetrics
                    ).toInt()

                    // Set the layout height to full screen height minus 10dp
                    it.layoutParams.height = screenHeight - topPaddingInPx
                    it.layoutParams = it.layoutParams

                    // Expand the bottom sheet
                    behavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }

            // Show the BottomSheetDialogFragment
            liveChatBottomSheet.show(supportFragmentManager, liveChatBottomSheet.tag)








        }



    }


}