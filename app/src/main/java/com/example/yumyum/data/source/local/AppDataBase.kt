package com.example.yumyum.data.source.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

//@Database(entities = , version = 1, exportSchema = false)
//@TypeConverters(Converters::class)
abstract class AppDataBase : RoomDatabase(), LocalDataSource {
    abstract fun mealDao(): MealDao

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null
        fun getInstance(context: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "app_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}