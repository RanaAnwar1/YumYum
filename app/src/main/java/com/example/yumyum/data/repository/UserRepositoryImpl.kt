package com.example.yumyum.data.repository

import com.example.yumyum.data.model.User

class UserRepositoryImpl:UserRepository {
    override suspend fun getUserByUsername(): User? {
        TODO("Not yet implemented")
    }

    override suspend fun insertUser(user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun getName(): String {
        TODO("Not yet implemented")
    }
}