package com.example.yumyum.data.source.remote

import com.example.yumyum.data.model.Areas
import com.example.yumyum.data.model.Categories
import com.example.yumyum.data.model.Meals
import com.example.yumyum.data.model.ReturnedMeal
import com.example.yumyum.data.model.SearchedMeal


interface RemoteDataSource {

    suspend fun listAllMealsCategories(): Categories
    suspend fun listAllAreas(): Areas
    suspend fun listAllMealsByAreas(area: String): Meals
    suspend fun listAllMealsByCategory(category: String): Meals
    suspend fun searchMealByName(mealName: String): SearchedMeal
    suspend fun listMealDetailsByID(mealId: String): ReturnedMeal

}