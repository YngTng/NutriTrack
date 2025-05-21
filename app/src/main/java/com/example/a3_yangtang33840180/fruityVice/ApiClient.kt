package com.example.a3_yangtang33840180.fruityVice

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    val api: FruityViceApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.fruityvice.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FruityViceApi::class.java)
    }
}