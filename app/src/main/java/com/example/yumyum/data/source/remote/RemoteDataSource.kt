package com.example.yumyum.data.source.remote

import androidx.lifecycle.LiveData
import com.example.yumyum.data.model.Meal
import retrofit2.http.GET
import retrofit2.http.Query


interface RemoteDataSource {

    suspend fun listAllMealsCategories(): LiveData<List<Category>>
    suspend fun listAllAreas(): LiveData<List<Area>>
    suspend fun listAllMealsByAreas(area: String): LiveData<List<Meal>>
    suspend fun listAllMealsByCategory(category: String): LiveData<List<Meal>>
    suspend fun searchMealByName(mealName: String): LiveData<List<Meal>>
    suspend fun listMealDetailsByID(mealId: Int): Meal

}