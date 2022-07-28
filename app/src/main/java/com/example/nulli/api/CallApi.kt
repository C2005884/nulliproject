package com.example.nulli.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class CallApi {

    private val gson: Gson? = GsonBuilder().setLenient().create()
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://openapi.naver.com/v1/")
        .addConverterFactory(GsonConverterFactory.create(gson!!))
        .client(client)
        .build()

    fun search(text:String){

    }
}