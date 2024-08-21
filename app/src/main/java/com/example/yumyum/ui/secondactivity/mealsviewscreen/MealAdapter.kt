package com.example.yumyum.ui.secondactivity.mealsviewscreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.yumyum.data.model.Meal
import com.example.yumyum.databinding.MealItemViewBinding
import com.example.yumyum.ui.secondactivity.homescreen.Cat

class MealAdapter:RecyclerView.Adapter<MealAdapter.MealViewHolder>() {

    var meals:List<Meal> = emptyList()
    inner class MealViewHolder(val binding: MealItemViewBinding):RecyclerView.ViewHolder(binding.root)

    fun setList(newList:List<Meal>){
        meals = newList
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val binding = MealItemViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MealViewHolder(binding)
    }

    override fun getItemCount(): Int = meals.size

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        holder.binding.apply {
            mealTitle.text = meals[position].strMeal
            Glide.with(holder.binding.root)
                .load(meals[position].strMealThumb)
                .into(mealImageView)
        }
    }
}