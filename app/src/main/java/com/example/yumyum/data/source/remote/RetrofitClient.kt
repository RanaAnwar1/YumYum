package com.example.yumyum.data.source.remote

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.yumyum.data.source.remote.RemoteDataSource as RemoteDataSource1

object RetrofitClient : RemoteDataSource1 {
    private val gson
        get() = GsonBuilder().serializeNulls().create()

    private val retrofit
        get() = Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

}