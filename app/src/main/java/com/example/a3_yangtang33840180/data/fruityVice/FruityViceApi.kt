package com.example.a3_yangtang33840180.data.fruityVice

import retrofit2.http.GET
import retrofit2.http.Path

interface FruityViceApi {
    @GET("api/fruit/{name}")
    suspend fun getFruit(@Path("name") fruitName: String): Fruit
}