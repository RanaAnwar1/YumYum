package com.example.yumyum.data.repository

import androidx.lifecycle.LiveData
import com.example.yumyum.data.model.Areas
import com.example.yumyum.data.model.Categories
import com.example.yumyum.data.model.FavoriteMeal
import com.example.yumyum.data.model.Meal
import com.example.yumyum.data.model.SearchedMeal
import com.example.yumyum.data.model.relation.UserMealCrossRef

interface MealsRepository{
    suspend fun getAreas():Areas
    suspend fun getCategories():Categories
    suspend fun getMealsByAreas(area:String):List<Meal>
    suspend fun getMealsByCategories(category:String):List<Meal>
    suspend fun getMealById(id:String): SearchedMeal
    suspend fun searchMealsByName(name:String):List<FavoriteMeal>
    suspend fun insertFavoriteMeal(meal:FavoriteMeal)
    suspend fun insertCrossRef(crossRef: UserMealCrossRef)
    suspend fun getMealsByUsername(username:String):List<FavoriteMeal>
}