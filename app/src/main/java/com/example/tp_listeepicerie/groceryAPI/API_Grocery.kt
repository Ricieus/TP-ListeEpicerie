package com.example.tp_listeepicerie.groceryAPI

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface API_Grocery {
//    @GET("product/default_products_en.json")
//    suspend fun getProducts(
//        @Path("languageCode") languageCode: String = "en",
//    ): List<ResultItem>
}

object ApiClient {
    private const val BASE_URL: String = "https://github.com/DanielRendox/GroceryGenius/blob/develop/assets/"

    private val gson: Gson by lazy {
        GsonBuilder().setLenient().create()
    }

    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder().build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val apiService: API_Grocery by lazy {
        retrofit.create(API_Grocery::class.java)
    }
}