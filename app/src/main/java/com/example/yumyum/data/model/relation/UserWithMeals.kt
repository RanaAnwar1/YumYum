package com.example.yumyum.data.model.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.yumyum.data.model.FavoriteMeal
import com.example.yumyum.data.model.User

data class UserWithMeals(
    @Embedded val user:User,
    @Relation(
        parentColumn = "username",
        entityColumn = "idMeal",
        associateBy = Junction(UserMealCrossRef::class)
    )
    val meals:List<FavoriteMeal>
)
