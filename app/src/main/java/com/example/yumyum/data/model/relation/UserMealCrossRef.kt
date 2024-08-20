package com.example.yumyum.data.model.relation

import androidx.room.Entity

@Entity(primaryKeys = ["username","idMeal"])
data class UserMealCrossRef(
    val username:String,
    val idMeal:String
)
