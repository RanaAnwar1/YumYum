package com.example.yumyum.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

interface FavouriteMealLocalDataSource {
    suspend fun insertFavoriteMeal(favoriteMeal: FavoriteMealEntity): Long

    suspend fun getFavoriteMealsByUserID(userId: Int): LiveData<List<FavoriteMealEntity>>

    suspend fun removeFavoriteMeal(mealId: String, userId: Int)

}