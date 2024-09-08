package com.ekghanti.livechat.helper

import android.util.Log
import org.json.JSONObject
import java.util.UUID
import java.util.concurrent.TimeUnit
import java.text.SimpleDateFormat
import java.util.*


class Helper {
    //function responsible to make chat message object
    fun makeChatMessage(chatInstanceId: String, channelId: String, message: String, type: String): String {
        val json = JSONObject()

        // Generating unique UUIDs for instanceId, channelID, and messageId
        val messageId = UUID.randomUUID().toString()

        // Getting the current timestamp in seconds with milliseconds as the decimal part
        val timestamp = System.currentTimeMillis() / 1000.0

        // Constructing the JSON structure
        json.put("instanceId", chatInstanceId)
        json.put("channelID", channelId)
        json.put("messageId", messageId)
        json.put("timestamp", timestamp)

        // Creating the chatMessage object
        val chatMessage = JSONObject()
        chatMessage.put("message", message)
        chatMessage.put("displayType", type)
        chatMessage.put("chatSide", "incoming")
        json.put("chatMessage", chatMessage)

        // Creating the destinationInfo object
        val destinationInfo = JSONObject()
        destinationInfo.put("entityType", "client")

        val userInfo = JSONObject()
        userInfo.put("ipAddress", "27.34.66.120")
        userInfo.put("usernameCookie", "")
        userInfo.put("user_name", JSONObject.NULL)
        destinationInfo.put("userInfo", userInfo)

        json.put("destinationInfo", destinationInfo)

        // Creating the sourceInfo object
        val sourceInfo = JSONObject()
        sourceInfo.put("entityType", "automatedResponse")
        sourceInfo.put("userInfo", JSONObject.NULL)
        json.put("sourceInfo", sourceInfo)

        // Returning the JSON object as a string
        return json.toString()
    }

    //    function to make timestamp into ago format
    fun getTimeAgo(timestamp: Long): String {
        val now = System.currentTimeMillis() / 1000

        // Calculate the difference in milliseconds
        val diff = now - timestamp

        return when {
            diff < TimeUnit.MINUTES.toMillis(1) -> {
                val seconds = TimeUnit.MILLISECONDS.toSeconds(diff)
                if (seconds == 1L) "1 second ago" else "$seconds seconds ago"
            }

            diff < TimeUnit.HOURS.toMillis(1) -> {
                val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
                if (minutes == 1L) "1 minute ago" else "$minutes minutes ago"
            }

            diff < TimeUnit.DAYS.toMillis(1) -> {
                val hours = TimeUnit.MILLISECONDS.toHours(diff)
                if (hours == 1L) "1 hour ago" else "$hours hours ago"
            }

            diff < TimeUnit.DAYS.toMillis(7) -> {
                val days = TimeUnit.MILLISECONDS.toDays(diff)
                if (days == 1L) "1 day ago" else "$days days ago"
            }

            diff < TimeUnit.DAYS.toMillis(30) -> {
                val weeks = TimeUnit.MILLISECONDS.toDays(diff) / 7
                if (weeks == 1L) "1 week ago" else "$weeks weeks ago"
            }

            diff < TimeUnit.DAYS.toMillis(365) -> {
                val months = TimeUnit.MILLISECONDS.toDays(diff) / 30
                if (months == 1L) "1 month ago" else "$months months ago"
            }

            else -> {
                val years = TimeUnit.MILLISECONDS.toDays(diff) / 365
                if (years == 1L) "1 year ago" else "$years years ago"
            }
        }
    }

    //    function to give time
    fun formatTimestamp(timestamp: Long): String {
        val now = System.currentTimeMillis()
        Log.e("timindidi0000", timestamp.toString())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp * 1000

        val today = Calendar.getInstance()
        val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }

        return when {
            // If the timestamp is from today
            calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                    calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR) -> {
                SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(timestamp * 1000))
            }
            // If the timestamp is from yesterday
            calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) &&
                    calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR) -> {
                "Yesterday"
            }
            // If the timestamp is from an earlier date
            else -> {
                SimpleDateFormat("d MMM", Locale.getDefault()).format(Date(timestamp * 1000))
            }
        }
    }
}
