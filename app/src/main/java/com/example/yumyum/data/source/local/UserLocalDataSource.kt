package com.example.yumyum.data.source.local

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.yumyum.data.model.User

interface UserLocalDataSource {
    suspend fun insertUser(user: User)
    suspend fun getUserByUsername(username: String): User?

    suspend fun getName(username: String): String
}