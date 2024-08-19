package com.example.yumyum.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.yumyum.data.model.FavoriteMeal
import com.example.yumyum.data.model.User

@Database(
    entities = [User::class,FavoriteMeal::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ApplicationDatabase : RoomDatabase(), FavouriteMealLocalDataSource, UserLocalDataSource {
    abstract fun mealDao(): FavoriteMealDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: ApplicationDatabase? = null
        fun getInstance(context: Context): ApplicationDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ApplicationDatabase::class.java,
                    "app_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

    override suspend fun insertUser(user: User) {
        userDao().insertUser(user)
    }

    override suspend fun getUserByUsername(username: String): User? {
        return userDao().getUserByUsername(username)
    }

    override suspend fun getName(username: String): User? {
        // TODO: implement the userdao getname function and override it here
        return null
    }
}