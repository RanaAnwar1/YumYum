package com.example.yumyum.data.source.remote

import androidx.lifecycle.LiveData
import com.example.yumyum.data.model.Meal
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient : RemoteDataSource {
    private val gson
        get() = GsonBuilder().serializeNulls().create()

    private val retrofit
        get() = Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
    override suspend fun listAllMealsCategories(): LiveData<List<Category>> {
        return apiService.listAllMealsCategories()
    }

    override suspend fun listAllAreas(): LiveData<List<Area>> {
        return apiService.listAllAreas()
    }

    override suspend fun listAllMealsByAreas(area: String): LiveData<List<Meal>> {
        return apiService.listAllMealsByAreas(area)
    }

    override suspend fun listAllMealsByCategory(category: String): LiveData<List<Meal>> {
        return apiService.listAllMealsByCategory(category)
    }

    override suspend fun searchMealByName(mealName: String): LiveData<List<Meal>> {
        return apiService.searchMealByName(mealName)
    }

    override suspend fun listMealDetailsByID(mealId: Int): Meal {
        return apiService.listMealDetailsByID(mealId)
    }
}