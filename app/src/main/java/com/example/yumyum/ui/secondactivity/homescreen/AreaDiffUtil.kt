package com.example.yumyum.ui.secondactivity.homescreen

import androidx.recyclerview.widget.DiffUtil
import com.example.yumyum.data.model.Area

class AreaDiffUtil(private val oldList:List<Area>, private val newList:List<Area>):DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int):
            Boolean = oldList[oldItemPosition].strArea == newList[newItemPosition].strArea

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int):
            Boolean = oldList[oldItemPosition].strArea == newList[newItemPosition].strArea
}