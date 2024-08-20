package com.example.yumyum.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.yumyum.data.model.FavoriteMeal

//interface FavouriteMealLocalDataSource {
//    suspend fun insertFavoriteMeal(favoriteMeal: FavoriteMeal): Long
//
//    suspend fun getFavoriteMealsByUserID(userId: Int): LiveData<List<FavoriteMeal>>
//
//    suspend fun removeFavoriteMeal(mealId: String, userId: Int)
//
//}