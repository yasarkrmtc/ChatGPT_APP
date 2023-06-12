package com.yasarkiremitci.chatgbt.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiUtilities {
    val client = OkHttpClient.Builder()
        .readTimeout(80, TimeUnit.SECONDS)
        .build()

    fun getApiInterface(): ApiInterface {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openai.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiInterface::class.java)
    }
}
