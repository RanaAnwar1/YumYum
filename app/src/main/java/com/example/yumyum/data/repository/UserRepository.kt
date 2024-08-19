package com.example.yumyum.data.repository

import com.example.yumyum.data.model.User

interface UserRepository {
    suspend fun getUserByUsername(username:String): User?
    suspend fun getName(username:String):String
    suspend fun insertUser(user: User)
}