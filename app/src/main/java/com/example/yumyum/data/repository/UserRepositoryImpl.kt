package com.example.yumyum.data.repository

import com.example.yumyum.data.model.User
import com.example.yumyum.data.source.local.UserLocalDataSource

class UserRepositoryImpl(private val localDataSource: UserLocalDataSource):UserRepository {
    override suspend fun getUserByUsername(username: String): User? {
        return localDataSource.getUserByUsername(username)
    }

    override suspend fun insertUser(user: User) {
        localDataSource.insertUser(user)
    }

    override suspend fun getName(username: String): String {
        return localDataSource.getName(username)
    }

}