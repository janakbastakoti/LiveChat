package com.ekghanti.livechat.adapter
//
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager

import androidx.recyclerview.widget.RecyclerView
import com.ekghanti.livechat.R

import com.ekghanti.livechat.helper.Helper
import com.ekghanti.livechat.model.chat.Message
import com.ekghanti.livechat.model.chat.SubMessage
import com.squareup.picasso.Picasso

import org.json.JSONObject
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class ChatAdapter(
    val context: Context, val dataList: List<Message>, private val onButtonClick: (String) -> Unit
) : RecyclerView.Adapter<ChatAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //other message
        val otherMsgArea: LinearLayout
        val otherMsg: TextView
        val otherTimestamp: TextView
        val otherImgMsg: ImageView

        //my message
        val myMsgArea: LinearLayout
        val myMsg: TextView
        val myTimestamp: TextView
        val myImgMsg: ImageView
        val buttonArea: LinearLayout
        val msgBtn: Button
        val btnTimestamp: TextView

        //        group
        val groupView: LinearLayout
        val groupTimestamp: TextView
        val subMsgRecyclerView: RecyclerView

        init {
            //other message
            otherTimestamp = itemView.findViewById(R.id.otherTimestamp)
            otherMsgArea = itemView.findViewById(R.id.otherMsgArea)
            //text
            otherMsg = itemView.findViewById(R.id.otherTextMsg)
            //image
            otherImgMsg = itemView.findViewById(R.id.otherImgMsg)

            //my message
            myTimestamp = itemView.findViewById(R.id.myTimestamp)
            myMsgArea = itemView.findViewById(R.id.myMsgArea)
            //text
            myMsg = itemView.findViewById(R.id.myTextMsg)
            //image
            myImgMsg = itemView.findViewById(R.id.myImgMsg)

            //button
            buttonArea = itemView.findViewById(R.id.buttonArea)
            msgBtn = itemView.findViewById(R.id.msgBtn)
            btnTimestamp = itemView.findViewById(R.id.btnTimestamp)

            // group
            groupView = itemView.findViewById(R.id.groupView)
            groupTimestamp = itemView.findViewById(R.id.groupTimestamp)
            subMsgRecyclerView = itemView.findViewById(R.id.subMsgRecyclerView)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.my_message, parent, false)
        return MyViewHolder(itemView);
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val gson = Gson()
        val currentData = dataList[position]
        val helper = Helper()
        val timestamp = currentData.timestamp.toLong()!! // Example: 2 days ago
        val timeAgo = helper.formatTimestamp(timestamp)


        Log.e("currentData:::777", currentData.toString())

        holder.msgBtn.setOnClickListener {
            onButtonClick(currentData?.chatMessage?.message.toString())
        }

        if (currentData?.chatMessage?.displayType == "text") {
            holder.buttonArea.visibility = View.GONE
            if (currentData?.chatMessage?.chatSide == "outgoing") {
                holder.myMsgArea.visibility = View.GONE
                holder.otherMsgArea.visibility = View.VISIBLE

                holder.otherMsg.text = currentData?.chatMessage?.message
                holder.otherMsg.visibility = View.VISIBLE
                holder.otherTimestamp.text = timeAgo

            } else {
                holder.myMsgArea.visibility = View.VISIBLE
                holder.otherMsgArea.visibility = View.GONE


                holder.myMsg.visibility = View.VISIBLE
                holder.myMsg.text = currentData?.chatMessage?.message

                holder.myTimestamp.text = timeAgo
            }
        } else if (currentData?.chatMessage?.displayType == "button") {
            holder.otherMsgArea.visibility = View.GONE
            holder.myMsgArea.visibility = View.GONE
            holder.buttonArea.visibility = View.VISIBLE
            holder.msgBtn.text = currentData?.chatMessage?.message
            holder.btnTimestamp.text = timeAgo

        } else if (currentData?.chatMessage?.displayType == "image") {
            if (currentData?.chatMessage?.chatSide == "outgoing") {
                holder.myMsgArea.visibility = View.GONE
                holder.otherMsgArea.visibility = View.VISIBLE

                holder.otherImgMsg.visibility = View.VISIBLE
                Picasso.get().load(currentData?.chatMessage?.message).into(holder.otherImgMsg)
                holder.otherTimestamp.text = timeAgo
            } else {
                holder.myMsgArea.visibility = View.VISIBLE
                holder.otherMsgArea.visibility = View.GONE

                holder.myImgMsg.visibility = View.VISIBLE
                Picasso.get().load(currentData?.chatMessage?.message).into(holder.myImgMsg)
                holder.myTimestamp.text = timeAgo
            }
        } else if (currentData?.chatMessage?.displayType == "group") {
            holder.groupTimestamp.text = timeAgo
            holder.groupView.visibility = View.VISIBLE
//            Log.e("group", currentData?.chatMessage?.message.toString())

            val listType = object : TypeToken<List<SubMessage>>() {}.type
            val subMsg: List<SubMessage> =
                gson.fromJson(currentData?.chatMessage?.message.toString(), listType)


            val subMsgAdapter = SubMsgAdapter(context, subMsg) { subMessage ->
                // Handle button clicks or actions within SubMsg
                onButtonClick(subMessage.toString())
            }

            // Setup the RecyclerView for sub messages
            holder.subMsgRecyclerView.apply {
                layoutManager = LinearLayoutManager(context) // or GridLayoutManager if needed
                adapter = subMsgAdapter
            }


            // Print the parsed data
//            subMsg.forEach { println(it) }


        }


    }


}