package com.example.yumyum.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.yumyum.data.model.FavoriteMeal

@Dao
interface FavoriteMealDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertFavoriteMeal(favoriteMeal: FavoriteMeal): Long

  @Query("SELECT * FROM FavoriteMeal WHERE username = :userId")
  suspend fun getFavoriteMealsByUserID(userId: String): LiveData<List<FavoriteMeal>>

  @Query("DELETE FROM FavoriteMeal WHERE idMeal = :mealId AND username = :userId")
  suspend fun removeFavoriteMeal(mealId: String, userId: Int)

}