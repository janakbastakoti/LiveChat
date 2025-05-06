package com.ekghanti.livechat.apiInterface

import com.ekghanti.livechat.model.chat.ChatData
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.Part
import retrofit2.http.Path


data class MessageRequest(
    val feedback: String,
    val instanceId: String,
    val channel_id: String
)

interface ApiInterface {
    @Headers(
        "Accept: */*",
        "Content-Type: application/json"
    )

    @GET("{id}")
    fun getData(@Path("id") id: String): Call<ChatData>

    @Multipart
    @POST("uploadImage")
    fun uploadImage(
        @Part image: MultipartBody.Part
    ): Call<String>

    @POST("feedback") // Specify your POST endpoint here
    fun sendFeedback(@Body messageRequest: JSONObject): Call<ResponseBody> // Use the appropriate response type


    //
    @GET("{id}") // Specify your POST endpoint here
    fun getBotInfo(@Path("id") id: String): Call<ResponseBody>
}