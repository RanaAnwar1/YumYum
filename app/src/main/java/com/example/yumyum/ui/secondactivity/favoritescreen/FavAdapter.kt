package com.example.yumyum.ui.secondactivity.favoritescreen

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.yumyum.R
import com.example.yumyum.data.model.Area
import com.example.yumyum.data.model.FavoriteMeal
import com.example.yumyum.databinding.MealItemViewBinding
import com.example.yumyum.ui.secondactivity.homescreen.AreaDiffUtil

class FavAdapter(val onClick:() -> Unit):RecyclerView.Adapter<FavAdapter.FavViewHolder>() {

    var favMealList = emptyList<FavoriteMeal>()
    inner class FavViewHolder(val binding: MealItemViewBinding):RecyclerView.ViewHolder(binding.root)

    fun setList(newList:List<FavoriteMeal>){
        val difference = FavDiffUtil(favMealList,newList)
        val result = DiffUtil.calculateDiff(difference)
        result.dispatchUpdatesTo(this)
        favMealList = newList
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val binding = MealItemViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FavViewHolder(binding)
    }

    override fun getItemCount(): Int = favMealList.size

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        holder.binding.apply {
            mealTitle.text = favMealList[position].strMeal
            Glide.with(root)
                .load(favMealList[position].strMealThumb)
                .into(mealImageView)
            mealImageView.setOnClickListener {
                Toast.makeText(root.context, "clicked", Toast.LENGTH_SHORT).show()
            }
            mealFavBt.setImageResource(R.drawable.baseline_favorite_24)

        }
    }
}