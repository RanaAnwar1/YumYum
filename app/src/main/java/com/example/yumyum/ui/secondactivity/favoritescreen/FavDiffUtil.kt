package com.example.yumyum.ui.secondactivity.favoritescreen

import androidx.recyclerview.widget.DiffUtil
import com.example.yumyum.data.model.Area
import com.example.yumyum.data.model.FavoriteMeal

class FavDiffUtil(private val oldList:List<FavoriteMeal>, private val newList:List<FavoriteMeal>): DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int):
            Boolean = oldList[oldItemPosition].idMeal == newList[newItemPosition].idMeal

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean{
        return oldList[oldItemPosition].strMeal == newList[newItemPosition].strMeal &&
                oldList[oldItemPosition].strMealThumb == newList[newItemPosition].strMealThumb &&
                oldList[oldItemPosition].strArea == newList[newItemPosition].strArea
    }
}