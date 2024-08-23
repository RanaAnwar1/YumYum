package com.example.yumyum.data.source.remote

import androidx.lifecycle.LiveData
import com.example.yumyum.data.model.Areas
import com.example.yumyum.data.model.Categories
import com.example.yumyum.data.model.FavoriteMeal
import com.example.yumyum.data.model.Meal
import com.example.yumyum.data.model.Meals
import com.example.yumyum.data.model.SearchedMeal
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("categories.php")
    suspend fun listAllMealsCategories(): Categories
    //www.themealdb.com/api/json/v1/1/categories.php
    @GET("list.php?a=list")
    suspend fun listAllAreas(): Areas
    //https://www.themealdb.com/api/json/v1/1/list.php?a=list
    @GET("filter.php")
    suspend fun listAllMealsByAreas(
        @Query("a") area: String
    ): Meals
    //https://www.themealdb.com/api/json/v1/1/filter.php?a=Canadian
    @GET("filter.php")
    suspend fun listAllMealsByCategory(
        @Query("c") category: String
    ): Meals
    //www.themealdb.com/api/json/v1/1/filter.php?c=Seafood
    @GET("search.php")
    suspend fun searchMealByName(
        @Query("s") mealName: String
    ): List<FavoriteMeal>
    //www.themealdb.com/api/json/v1/1/search.php?s=Arrabiata
    @GET("lookup.php")
    suspend fun listMealDetailsByID(
        @Query("i") mealId: String
    ): SearchedMeal
    //www.themealdb.com/api/json/v1/1/lookup.php?i=52772

}