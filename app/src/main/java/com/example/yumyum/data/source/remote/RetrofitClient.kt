package com.example.yumyum.data.source.remote

import com.example.yumyum.data.model.Areas
import com.example.yumyum.data.model.Categories
import com.example.yumyum.data.model.Meals
import com.example.yumyum.data.model.ReturnedMeal
import com.example.yumyum.data.model.SearchedMeal
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

    private val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
    override suspend fun listAllMealsCategories(): Categories {
        return apiService.listAllMealsCategories()
    }

    override suspend fun listAllAreas(): Areas{
        return apiService.listAllAreas()
    }

    override suspend fun listAllMealsByAreas(area: String): Meals {
        return apiService.listAllMealsByAreas(area)
    }

    override suspend fun listAllMealsByCategory(category: String): Meals {
        return apiService.listAllMealsByCategory(category)
    }

    override suspend fun searchMealByName(mealName: String): SearchedMeal{
        return apiService.searchMealByName(mealName)
    }

    override suspend fun listMealDetailsByID(mealId: String): ReturnedMeal {
        return apiService.listMealDetailsByID(mealId)
    }
}