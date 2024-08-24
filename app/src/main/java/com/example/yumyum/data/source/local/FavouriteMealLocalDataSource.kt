package com.example.yumyum.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.yumyum.data.model.FavoriteMeal
import com.example.yumyum.data.model.relation.UserMealCrossRef

interface FavouriteMealLocalDataSource {
    suspend fun insertFavoriteMeal(favoriteMeal: FavoriteMeal)

    suspend fun getFavoriteMealsByUsername(username: String): List<FavoriteMeal>

    suspend fun insertCrossRef(crossRef: UserMealCrossRef)

    suspend fun deleteFavoriteMeal(favoriteMeal: FavoriteMeal)

    suspend fun deleteCrossRef(username: String, idMeal: String)

}