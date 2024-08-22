package com.example.yumyum.data.repository

import androidx.lifecycle.LiveData
import com.example.yumyum.data.model.Areas
import com.example.yumyum.data.model.Categories
import com.example.yumyum.data.model.FavoriteMeal
import com.example.yumyum.data.model.Meal
import com.example.yumyum.data.source.local.FavouriteMealLocalDataSource
import com.example.yumyum.data.source.remote.RemoteDataSource

class MealsRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
) :MealsRepository{
    override suspend fun getAreas(): Areas {
        return remoteDataSource.listAllAreas()
    }

    override suspend fun getCategories(): Categories {
        return remoteDataSource.listAllMealsCategories()
    }

    override suspend fun getMealsByAreas(area:String): List<Meal> {
        return remoteDataSource.listAllMealsByAreas(area).meals
    }

    override suspend fun getMealsByCategories(category: String): List<Meal> {
        return remoteDataSource.listAllMealsByCategory(category).meals
    }

    override suspend fun getMealById(id: Int): FavoriteMeal {
        return remoteDataSource.listMealDetailsByID(id)
    }

    override suspend fun searchMealsByName(name: String): List<FavoriteMeal> {
        return remoteDataSource.searchMealByName(name)
    }
}