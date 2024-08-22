package com.example.yumyum.ui.secondactivity.homescreen

import androidx.recyclerview.widget.DiffUtil
import com.example.yumyum.data.model.Area
import com.example.yumyum.data.model.Category

class CategoryDiffUtil(private val oldList:List<Category>, private val newList:List<Category>): DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int):
            Boolean = oldList[oldItemPosition].idCategory == newList[newItemPosition].idCategory

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
       return oldList[oldItemPosition].strCategory == newList[newItemPosition].strCategory &&
                oldList[oldItemPosition].strCategoryThumb == newList[newItemPosition].strCategoryThumb &&
                oldList[oldItemPosition].strCategoryDescription == newList[newItemPosition].strCategoryDescription
    }
}