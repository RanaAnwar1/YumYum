package com.example.yumyum.ui.secondactivity.mealsviewscreen

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.yumyum.R
import com.example.yumyum.data.model.Meal
import com.example.yumyum.databinding.MealItemViewBinding
import com.example.yumyum.ui.secondactivity.searchscreen.SearchFragmentDirections


class MealAdapter(val onFavBtClicked:(mealId:String) -> Unit):RecyclerView.Adapter<MealAdapter.MealViewHolder>() {

    var meals:List<Meal> = emptyList()
        set(value) {
            val difference = MealDiffUtil(field,value)
            val result = DiffUtil.calculateDiff(difference)
            result.dispatchUpdatesTo(this)
            field = value
        }
    var favoriteMealIds: Set<String> = emptySet()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    inner class MealViewHolder(val binding: MealItemViewBinding):RecyclerView.ViewHolder(binding.root)




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val binding = MealItemViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MealViewHolder(binding)
    }

    override fun getItemCount(): Int = meals.size

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val meal = meals[position]
        val isFavorite = favoriteMealIds.contains(meal.idMeal)
        holder.binding.apply {
            mealTitle.text = meal.strMeal
            Glide.with(holder.binding.root)
                .load(meal.strMealThumb)
                .into(mealImageView)
            mealFavBt.setImageResource(
                if (isFavorite) R.drawable.baseline_favorite_24
                else R.drawable.baseline_favorite_border_24
            )

            mealFavBt.setOnClickListener {
                val newFavoriteStatus = !isFavorite
                mealFavBt.setImageResource(
                    if (newFavoriteStatus) R.drawable.baseline_favorite_24
                    else R.drawable.baseline_favorite_border_24
                )
                onFavBtClicked(meal.idMeal)
                
            }
        }
        holder.itemView.setOnClickListener {
            val action = meal.idMeal.let { mealId ->
                MealsViewFragmentDirections.actionMealsViewFragmentToNavigationMealDetails(mealId)
            }
            it.findNavController().navigate(action)
        }
    }


}