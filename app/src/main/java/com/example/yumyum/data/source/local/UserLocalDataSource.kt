package com.example.yumyum.data.source.local

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

interface UserLocalDataSource {
    suspend fun insertUser(user: UserEntity): Int

    suspend fun getUserById(userId: Int): UserEntity?

    suspend fun getUserWithFavoriteMeals(userId: Int): UserWithFavoriteMeals?
}