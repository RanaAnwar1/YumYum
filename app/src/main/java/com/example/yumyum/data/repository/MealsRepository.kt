package com.example.yumyum.data.repository

import androidx.lifecycle.LiveData
import com.example.yumyum.data.model.Areas
import com.example.yumyum.data.model.Categories
import com.example.yumyum.data.model.FavoriteMeal
import com.example.yumyum.data.model.Meal

interface MealsRepository{
    suspend fun getAreas():Areas
    suspend fun getCategories():Categories
    suspend fun getMealsByAreas(area:String):List<Meal>
    suspend fun getMealsByCategories(category:String):List<Meal>
    suspend fun getMealById(id:Int):FavoriteMeal
    suspend fun searchMealsByName(name:String):List<FavoriteMeal>
}