package com.ekghanti.livechat.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ekghanti.livechat.R
import com.ekghanti.livechat.model.chat.SubMessage

class SubMsgAdapter(
    val context: Context,
    val dataList: List<SubMessage>,
    private val onButtonClick: (String) -> Unit
) : RecyclerView.Adapter<SubMsgAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //        button
        val subButtonArea: LinearLayout
        val subMsgBtn: Button


        //        text
        val subMsgArea: LinearLayout
        val subMsgText: TextView

        init {
            //button
            subButtonArea = itemView.findViewById(R.id.subButtonArea)
            subMsgBtn = itemView.findViewById(R.id.subMsgBtn)

            //text
            subMsgArea = itemView.findViewById(R.id.subMsgArea)
            subMsgText = itemView.findViewById(R.id.subMsgText)


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.sub_message, parent, false)
        return MyViewHolder(itemView);
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentData = dataList[position]

        Log.e("sub*******", currentData?.message.toString())

        holder.subMsgBtn.setOnClickListener {
            onButtonClick(currentData.message)
        }


        if (currentData?.displayType == "button") {
            holder.subButtonArea.visibility = View.VISIBLE
            holder.subMsgArea.visibility = View.GONE

            holder.subMsgBtn.text = currentData?.message
            Log.e("sub******* type", currentData?.displayType.toString())
        } else if (currentData?.displayType == "text") {
            Log.e("sub******* type other", currentData?.displayType.toString())
            holder.subMsgArea.visibility = View.VISIBLE
            holder.subButtonArea.visibility = View.GONE

            holder.subMsgText.text = currentData.message
        }


        //holder.subMsgText.text = "Hello world"
    }
}