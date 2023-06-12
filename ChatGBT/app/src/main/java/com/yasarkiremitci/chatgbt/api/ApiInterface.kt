package com.yasarkiremitci.chatgbt.api

import com.yasarkiremitci.chatgbt.models.ChatModel
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiInterface {

    @POST("v1/chat/completions")
    suspend fun getChat(

        @Header("Content_Type") contentType: String,
        @Header("Authorization") authorization: String,
        @Body requestBody: RequestBody

    ) : ChatModel
}