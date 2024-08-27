package com.example.yumyum.ui.secondactivity.favoritescreen

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.yumyum.R
import com.example.yumyum.data.model.Area
import com.example.yumyum.data.model.FavoriteMeal
import com.example.yumyum.databinding.ItemMealViewBinding
import com.example.yumyum.ui.secondactivity.MealViewModel
import com.example.yumyum.ui.secondactivity.homescreen.AreaDiffUtil
import com.example.yumyum.ui.secondactivity.searchscreen.SearchFragmentDirections
import com.example.yumyum.util.Constant

class FavAdapter(
    private val onClick:(id:String) -> Unit
) :RecyclerView.Adapter<FavAdapter.FavViewHolder>() {

    inner class FavViewHolder(val binding: ItemMealViewBinding):RecyclerView.ViewHolder(binding.root)


    var favMealList = emptyList<FavoriteMeal>()
        set(value) {
            val diffCallback = FavDiffUtil(favMealList, value)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            field = value
            diffResult.dispatchUpdatesTo(this)
            notifyDataSetChanged()
        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val binding = ItemMealViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FavViewHolder(binding)
    }

    override fun getItemCount(): Int = favMealList.size

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        holder.binding.apply {
            tvSearchedTitle.text = favMealList[position].strMeal
            Glide.with(root)
                .load(favMealList[position].strMealThumb)
                .into(imgSearched)
            imgSearched.setOnClickListener {
                Toast.makeText(root.context, "clicked", Toast.LENGTH_SHORT).show()
            }
            imgFavoriteIcon.setImageResource(R.drawable.baseline_favorite_24)
        }
        holder.binding.imgFavoriteIcon.setOnClickListener {
           try {
                holder.binding.imgFavoriteIcon.setImageResource(R.drawable.baseline_favorite_border_24)
                onClick(favMealList[position].idMeal)
            }catch (e: Exception){
                Log.e("Favorite Screen", "Favorite button : ${e.message}")
            }

        }

        holder.itemView.setOnClickListener {
            val action = favMealList[position].idMeal.let { it1 ->
                FavoriteFragmentDirections.actionNavigationFavoriteToNavigationMealDetails(
                    it1
                )
            }
            it.findNavController().navigate(action)

        }
    }
}