package com.example.yumyum.data.source.remote

import androidx.lifecycle.LiveData
import com.example.yumyum.data.model.Meal
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("categories.php")
    suspend fun listAllMealsCategories(): LiveData<List<Category>>
    //www.themealdb.com/api/json/v1/1/categories.php
    @GET("list.php?a=list")
    suspend fun listAllAreas(): LiveData<List<Area>>
    //https://www.themealdb.com/api/json/v1/1/list.php?a=list
    @GET("filter.php")
    suspend fun listAllMealsByAreas(
        @Query("a") area: String
    ): LiveData<List<Meal>>
    //https://www.themealdb.com/api/json/v1/1/filter.php?a=Canadian
    @GET("filter.php")
    suspend fun listAllMealsByCategory(
        @Query("c") category: String
    ): LiveData<List<Meal>>
    //www.themealdb.com/api/json/v1/1/filter.php?c=Seafood
    @GET("search.php")
    suspend fun searchMealByName(
        @Query("s") mealName: String
    ): LiveData<List<Meal>>
    //www.themealdb.com/api/json/v1/1/search.php?s=Arrabiata
    @GET("lookup.php")
    suspend fun listMealDetailsByID(
        @Query("i") mealId: Int
    ): Meal
    //www.themealdb.com/api/json/v1/1/lookup.php?i=52772

}