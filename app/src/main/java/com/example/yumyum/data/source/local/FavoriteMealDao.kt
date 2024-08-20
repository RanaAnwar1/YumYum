package com.example.yumyum.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.yumyum.data.model.FavoriteMeal
import com.example.yumyum.data.model.relation.UserMealCrossRef

@Dao
interface FavoriteMealDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertFavoriteMeal(favoriteMeal: FavoriteMeal)

  @Query("select * from FavoriteMeal where idMeal in ( select idMeal from UserMealCrossRef where username = :username)")
  suspend fun getFavoriteMealsByUsername(username: String): List<FavoriteMeal>

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insertCrossRef(crossRef: UserMealCrossRef)
}