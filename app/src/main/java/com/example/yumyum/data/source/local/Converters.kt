package com.example.yumyum.data.source.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class Converters {

    @TypeConverter
    fun fromListOfStringsToString(list: List<String>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromStringToListOfStrings(value: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, type)
    }

}