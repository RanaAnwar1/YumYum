package com.example.yumyum.data.source.remote

import androidx.lifecycle.LiveData
import com.example.yumyum.data.model.Areas
import com.example.yumyum.data.model.Categories
import com.example.yumyum.data.model.FavoriteMeal
import com.example.yumyum.data.model.Meal
import com.example.yumyum.data.model.Meals
import retrofit2.http.GET
import retrofit2.http.Query


interface RemoteDataSource {

    suspend fun listAllMealsCategories(): Categories
    suspend fun listAllAreas(): Areas
    suspend fun listAllMealsByAreas(area: String): Meals
    suspend fun listAllMealsByCategory(category: String): Meals
    suspend fun searchMealByName(mealName: String): List<FavoriteMeal>
    suspend fun listMealDetailsByID(mealId: Int): FavoriteMeal

}