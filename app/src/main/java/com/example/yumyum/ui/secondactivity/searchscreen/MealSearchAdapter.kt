package com.example.yumyum.ui.secondactivity.searchscreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.yumyum.R
import com.example.yumyum.data.model.FavoriteMeal
import com.example.yumyum.data.model.Meal
import com.example.yumyum.data.model.ReturnedMeals
import com.example.yumyum.data.model.SearchedMeal

class MealSearchAdapter(
    private var mealList: List<ReturnedMeals>,
    private val onFavBtClicked: (mealId: String) -> Unit
) : RecyclerView.Adapter<MealSearchAdapter.MealViewHolder>() {


    private var favoriteMealIds: Set<String> = emptySet()
    class MealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mealName: TextView = itemView.findViewById(R.id.tvSearchedTitle)
        val mealImage: ImageView = itemView.findViewById(R.id.imgSearched)
        val mealFavBtn :ImageView =itemView.findViewById(R.id.imgFavoriteIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_meal_view, parent, false)
        return MealViewHolder(view)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val meal = mealList[position]
        val isFavorite = favoriteMealIds.contains(meal.idMeal)
        holder.mealName.text = meal.strMeal
        Glide.with(holder.itemView.context).load(meal.strMealThumb).into(holder.mealImage)
        holder.mealFavBtn.setImageResource(
            if (isFavorite) R.drawable.baseline_favorite_24
            else R.drawable.baseline_favorite_border_24
        )

        holder.mealFavBtn.setOnClickListener {
            val newFavoriteStatus = !isFavorite
            holder.mealFavBtn.setImageResource(
                if (newFavoriteStatus) R.drawable.baseline_favorite_24
                else R.drawable.baseline_favorite_border_24
            )
            meal.idMeal?.let { it1 -> onFavBtClicked(it1) }
        }
        holder.itemView.setOnClickListener {
            val action = meal.idMeal?.let { it1 ->
                SearchFragmentDirections.actionNavigationSearchToNavigationMealDetails(
                    it1
                )
            }
            if (action != null) {
                it.findNavController().navigate(action)
            }
        }
    }

    override fun getItemCount(): Int {
        return mealList.size
    }

    fun updateMeals(newMealList: List<ReturnedMeals>, favoriteIds: Set<String>) {
        mealList = newMealList
        favoriteMealIds = favoriteIds
        notifyDataSetChanged()
    }
}
