package com.example.yumyum.ui.secondactivity.mealsviewscreen

import androidx.recyclerview.widget.DiffUtil
import com.example.yumyum.data.model.Meal


class MealDiffUtil(private val oldList:List<Meal>, private val newList:List<Meal>):DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int):
            Boolean = oldList[oldItemPosition].idMeal == newList[newItemPosition].idMeal

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].strMeal == newList[newItemPosition].strMeal &&
                oldList[oldItemPosition].strMealThumb == newList[newItemPosition].strMealThumb
    }
}