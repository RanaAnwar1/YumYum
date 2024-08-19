package com.example.yumyum.data.model

import com.google.gson.annotations.SerializedName

data class Meal(
    val idMeal: String,
    val strMeal: String,
    val strMealThumb: String
)
data class Category (

    @SerializedName("categories" ) var categories : ArrayList<Categories> = arrayListOf()

)
data class Categories (

    @SerializedName("idCategory"             ) var idCategory             : String? = null,
    @SerializedName("strCategory"            ) var strCategory            : String? = null,
    @SerializedName("strCategoryThumb"       ) var strCategoryThumb       : String? = null,
    @SerializedName("strCategoryDescription" ) var strCategoryDescription : String? = null

)
data class Area (

    @SerializedName("meals" ) var meals : ArrayList<Meals> = arrayListOf()

)
data class Meals (

    @SerializedName("strArea" ) var strArea : String? = null

)