package com.ekghanti.livechat

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
//import android.widget.Toolbar
//import android.widget.Toolbar
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ekghanti.livechat.adapter.ChatAdapter
import com.ekghanti.livechat.apiInterface.ApiInterface
import com.ekghanti.livechat.model.chat.ChatData
import com.ekghanti.livechat.model.chat.Message
import com.ekghanti.livechat.socket.WebSocketListener
import com.squareup.picasso.Picasso
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import okhttp3.WebSocket
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File
import java.io.FileOutputStream

import okhttp3.logging.HttpLoggingInterceptor

class LiveChat : Fragment(R.layout.livechat) {
    private var channelId: String? = null
    private var userName: String? = ""

    @Volatile
    private var chatInstanceId: String? = ""

    private var title: String? = null
    private var subTitle: String? = null

    private lateinit var webSocket: WebSocket
    private lateinit var myRecyclerView: RecyclerView
    private lateinit var myAdapter: ChatAdapter

    private val messageList: MutableList<Message> = mutableListOf()
    private val PICK_IMAGE_REQUEST = 1

    private var imageUri: Uri? = null
    private var uploadedUrl: String? = null

    private var backPressCount: Number? = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.livechat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val scrollView: ScrollView = view.findViewById(R.id.scrollView)

        arguments?.let {
            channelId = it.getString("channelId")
            title = it.getString("title")
            subTitle = it.getString("subTitle")
        }
        val icon = arguments?.getInt("icon")

        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.hide()

        val titleView: TextView = view.findViewById(R.id.title)
        val subTitleView: TextView = view.findViewById(R.id.subTitle)
        val iconView: ImageView = view.findViewById(R.id.iconImage)

        if (icon != null) {
            iconView.setImageResource(icon)
        } else {
            iconView.setImageResource(R.drawable.chat_icon)
        }

        titleView.setText(title)
        subTitleView.setText(subTitle)

        //Log.e("local stored:::", getInstanceIdFromLocal().toString())

        val listener = WebSocketListener(
            { newMessage ->
                requireActivity().runOnUiThread {
                    messageList.add(newMessage)
                    myAdapter.notifyItemInserted(messageList.size - 1)
                    myRecyclerView.scrollToPosition(messageList.size - 1)
                    scrollView.post {
                        scrollView.fullScroll(View.FOCUS_DOWN)
                    }
                }
            },
            chatInstanceId = chatInstanceId.toString(),
            //chatInstanceId = getInstanceIdFromLocal().toString(),
            userName = userName.toString(),
            channelId = channelId.toString(),
            context = requireContext()
        )

        socketConnection(listener)
        setupRecyclerView(view, listener)
        loadData(channelId.toString())

        // Click me button
        //val button: Button = view.findViewById(R.id.clickMeBtn)
        //button.setOnClickListener {
        //    listener.onClickMe()
        //}


        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    if (backPressCount == 0) {
                        backPressCount = 1
                        showModalDialog(listener, view)
                    } else {
                        requireActivity().finish()
                    }
                }
            })


        // Send message button click
        val sendButton: ImageButton = view.findViewById(R.id.sendMessageButton)
        val messageEditor: EditText = view.findViewById(R.id.editTextText)
        sendButton.setOnClickListener {
            val textMsg = messageEditor.text.toString()
            val pickedImage: ImageView = view.findViewById(R.id.imageView)

            if (uploadedUrl != null) {
                listener.sendMessage(uploadedUrl!!, "image")
                clearImageSelection(pickedImage)
            } else if (textMsg.any { it.isLetter() }) {
                listener.sendMessage(textMsg, "text")
                messageEditor.post {
                    messageEditor.requestFocus()
                    messageEditor.setText("")
                }
            }

        }

        // Image picker function
        val imagePicker: ImageButton = view.findViewById(R.id.addAttachment)
        imagePicker.setOnClickListener {
            openGallery()
        }

    }

    private fun showModalDialog(listener: WebSocketListener, view: View) {
        // Create an AlertDialog Builder
        val builder = AlertDialog.Builder(requireContext())

        // Set the title and message of the modal dialog
        builder.setTitle("Alert")
        builder.setMessage("Are you sure you want to close the conversation ?")

        // Add an action button
        builder.setPositiveButton("OK") { dialogInterface: DialogInterface, _: Int ->
            // Dismiss the dialog when OK is clicked
            listener.sendMessage("Conversation Closed", "feedback")
            val editorLayout: LinearLayout = view.findViewById(R.id.editorLayout)
            editorLayout.visibility = View.GONE
            dialogInterface.dismiss()
        }

        // Optionally, you can add a cancel button too
        builder.setNegativeButton("Cancel") { dialogInterface: DialogInterface, _: Int ->
            // Dismiss the dialog when Cancel is clicked
            dialogInterface.dismiss()
        }

        // Create and show the dialog
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val pickedImage: ImageView = requireView().findViewById(R.id.imageView)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == AppCompatActivity.RESULT_OK && data != null && data.data != null) {
            pickedImage.visibility = ImageView.VISIBLE
            imageUri = data.data!!
            Picasso.get().load(imageUri).into(pickedImage)

            onUpload()
        }
    }

    //    function to get file extension
    private fun getFileExtensionFromUri(uri: Uri): String? {
        val mimeType = requireContext().contentResolver.getType(uri)
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
    }

    //  funtion to upload image
    private fun onUpload() {
        val fileDir = requireContext().filesDir
        val fileExtension = imageUri?.let { getFileExtensionFromUri(it) }

        val file = File(fileDir, "image.${fileExtension}")
        val inputStream = imageUri?.let { requireContext().contentResolver.openInputStream(it) }
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)

        val requestBody = file.asRequestBody("image/*".toMediaType())
        val part = MultipartBody.Part.createFormData("sending_file", file.name, requestBody)

        val retrofitBuilderUpload = Retrofit.Builder().baseUrl("https://chat.orbit360.cx:8443/")
            .addConverterFactory(ScalarsConverterFactory.create()).build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilderUpload.uploadImage(part)
        retrofitData.enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                if (response.isSuccessful) {
                    uploadedUrl = response.body().toString()
                } else {
                    Log.e("Image upload failed", "Response code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<String?>, p1: Throwable) {
                Toast.makeText(
                    requireContext(), "Failed to load data. Please try again.", Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun setupRecyclerView(view: View, listener: WebSocketListener) {
        myRecyclerView = view.findViewById(R.id.recyclerView)
        //myRecyclerView.
        myAdapter = ChatAdapter(requireContext(), messageList) { message ->
            listener.sendMessage(message, "text", isButton = true)
        }
        myRecyclerView.adapter = myAdapter
        myRecyclerView.layoutManager = LinearLayoutManager(requireContext())

    }

    private fun socketConnection(listener: WebSocketListener) {
        val client = OkHttpClient()
        val request: Request = Request.Builder().url("wss://chat.orbit360.cx:8443/").build()
        webSocket = client.newWebSocket(request, listener)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun clearImageSelection(pickedImage: ImageView) {
        imageUri = null
        uploadedUrl = null
        pickedImage.setImageDrawable(null)
        pickedImage.visibility = ImageView.GONE
    }


    //function to api call
    private fun loadData(id: String) {
        Log.e("api call", "error entering...")
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://chat.orbit360.cx:8443/chatStorageWhook/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)

        // Pass the dynamic ID to the API call
        val retrofitData = retrofitBuilder.getData(id)

        retrofitData.enqueue(object : Callback<ChatData?> {
            override fun onResponse(call: Call<ChatData?>, response: Response<ChatData?>) {
                response.body()?.messages?.let { messages ->
                    Log.e("this is my:::::", messages.toString())
                    //messageList.addAll(messages)  // Update the message list
                    //myAdapter.notifyDataSetChanged()  // Notify the adapter
                }
            }

            override fun onFailure(call: Call<ChatData?>, p1: Throwable) {
                Log.e("this is my:::::error", "error")
                // Handle the failure
            }
        })
    }


    private fun sendMessageToApi(message: String) {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://chat.orbit360.cx:8443/chatStorageWhook/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)


        // Create the request body
        val messageRequest = JSONObject().apply {
            put("feedback", "like")
            put("instanceId", "772f2b31-14cd-431d-905b-bda1ab8292a0")
            put("channel_id", "772f2b31-14cd-431d-905b-bda1ab8292a0")
        }

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Log request and response body
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        // Make the POST request
        val retrofitData = retrofitBuilder.sendFeedback(messageRequest)

        retrofitData.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                println("response sending message........")
                if (response.isSuccessful) {
                    // Handle success, e.g., notify the user or update the UI
                    println("Message sent successfully: ${response.body()}")
                } else {
                    // Handle the case when the response is not successful
                    println("Failed to send message: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Handle failure, e.g., show a Toast
                println("Error sending message: ${t.message}")
            }
        })
    }

    //get instanceId from local
    private fun getInstanceIdFromLocal(): String? {
        val sharedPreferences =
            requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("CHAT_INSTANCE_ID", "") ?: ""
    }

}