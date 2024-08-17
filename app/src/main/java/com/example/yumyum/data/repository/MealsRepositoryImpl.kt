package com.example.yumyum.data.repository

import com.example.yumyum.data.source.local.FavouriteMealLocalDataSource
import com.example.yumyum.data.source.remote.RemoteDataSource

class MealsRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: FavouriteMealLocalDataSource,
) :MealsRepository{
    // TODO: implement required functions 
}