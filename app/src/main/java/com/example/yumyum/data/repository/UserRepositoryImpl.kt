package com.example.yumyum.data.repository

import com.example.yumyum.data.model.User

class UserRepositoryImpl:UserRepository {
    override suspend fun getUserByUsername(username: String): User? {
        TODO("Not yet implemented")
    }

    override suspend fun getName(username: String): String {
        TODO("Not yet implemented")
    }

    override suspend fun insertUser(user: User) {
        TODO("Not yet implemented")
    }

}