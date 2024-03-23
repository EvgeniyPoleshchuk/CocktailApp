package com.example.coctailapp.ApiService

import com.example.coctailapp.model.Cocktail
import com.example.coctailapp.model.IngredientList
import okhttp3.Interceptor
import okhttp3.OkHttpClient


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface CocktailService {
    @GET("cocktail")
    suspend fun getCocktail(@Query("name") name: String,
                            @Query("ingredients") ingredients:String): Cocktail
}

val apiKey = "3FHknp36SH2vOawgtFl0MA==Xtsq3k9f6uuJvYtf"
val interceptor = Interceptor { chain ->
    val originalRequest = chain.request()

    val newRequest = originalRequest.newBuilder()
        .header("X-Api-Key", apiKey)
        .build()

    chain.proceed(newRequest)
}

val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(interceptor)
    .build()

var retrofit = Retrofit.Builder()
    .client(okHttpClient)
    .baseUrl("https://api.api-ninjas.com/v1/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()
var cocktailService = retrofit.create(CocktailService::class.java)

