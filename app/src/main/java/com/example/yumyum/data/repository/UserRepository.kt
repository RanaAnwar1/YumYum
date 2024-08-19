package com.example.yumyum.data.repository

import com.example.yumyum.data.model.User

interface UserRepository {
    suspend fun getUserByUsername(): User?
    suspend fun getName():String
    suspend fun insertUser(user: User)
}