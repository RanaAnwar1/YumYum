package com.example.yumyum.data.source.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

//@Database(entities = , version = 1, exportSchema = false)
//@TypeConverters(Converters::class)
abstract class ApplicationDatabase : RoomDatabase(), FavouriteMealLocalDataSource, UserLocalDataSource {
    abstract fun mealDao(): MealDao
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

}