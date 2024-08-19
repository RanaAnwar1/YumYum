package com.example.yumyum.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteMealDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertFavoriteMeal(favoriteMeal: FavoriteMealEntity): Long

  @Query("SELECT * FROM favorite_meals WHERE userOwnerId = :userId")
  suspend fun getFavoriteMealsByUserID(userId: Int): LiveData<List<FavoriteMealEntity>>

  @Query("DELETE FROM favorite_meals WHERE mealId = :mealId AND userOwnerId = :userId")
  suspend fun removeFavoriteMeal(mealId: String, userId: Int)

}