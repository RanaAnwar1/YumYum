package com.example.yumyum.data.repository

interface UserRepository {
    suspend fun getUsername():String
    suspend fun getPassword():String
}