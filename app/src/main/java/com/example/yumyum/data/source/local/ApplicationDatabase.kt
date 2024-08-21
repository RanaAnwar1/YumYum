package com.example.yumyum.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.yumyum.data.model.FavoriteMeal
import com.example.yumyum.data.model.User
import com.example.yumyum.data.model.relation.UserMealCrossRef

@Database(
    entities = [User::class,FavoriteMeal::class,UserMealCrossRef::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ApplicationDatabase : RoomDatabase(), FavouriteMealLocalDataSource,  UserLocalDataSource {
    abstract fun FavoriteMealDao(): FavoriteMealDao
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
                ).fallbackToDestructiveMigration().build()
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

    override suspend fun getName(username: String): String {
        return userDao().getName(username)
    }

    override suspend fun insertFavoriteMeal(favoriteMeal: FavoriteMeal) {
        return FavoriteMealDao().insertFavoriteMeal(favoriteMeal)
    }

    override suspend fun getFavoriteMealsByUsername(username: String): List<FavoriteMeal> {
        return FavoriteMealDao().getFavoriteMealsByUsername(username)
    }

    override suspend fun insertCrossRef(crossRef: UserMealCrossRef) {
        return FavoriteMealDao().insertCrossRef(crossRef)
    }
}