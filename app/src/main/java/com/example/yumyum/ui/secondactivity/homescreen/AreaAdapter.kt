package com.example.yumyum.ui.secondactivity.homescreen

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.yumyum.data.model.Area
import com.example.yumyum.data.model.Meal
import com.example.yumyum.databinding.ViewItemAreaBinding
import com.example.yumyum.ui.secondactivity.mealsviewscreen.MealDiffUtil

class AreaAdapter(val onClick:(area:String) -> Unit):RecyclerView.Adapter<AreaAdapter.AreaViewHolder>() {


    inner class AreaViewHolder(val binding:ViewItemAreaBinding):RecyclerView.ViewHolder(binding.root)

    var areas:List<Area> = emptyList()
        set(value) {
            val difference = AreaDiffUtil(field,value)
            val result = DiffUtil.calculateDiff(difference)
            result.dispatchUpdatesTo(this)
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreaViewHolder {
        val binding = ViewItemAreaBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AreaViewHolder(binding)
    }

    override fun getItemCount(): Int  = areas.size

    override fun onBindViewHolder(holder: AreaViewHolder, position: Int) {
        holder.binding.apply {
            areaRecyclerBt.text = areas[position].strArea
            areaRecyclerBt.setOnClickListener {
                onClick(areas[position].strArea)
            }
        }
    }

}