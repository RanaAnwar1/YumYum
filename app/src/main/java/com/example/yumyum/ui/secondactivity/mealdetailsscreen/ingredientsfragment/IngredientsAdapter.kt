package com.example.yumyum.ui.secondactivity.mealdetailsscreen.ingredientsfragment

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.yumyum.R
import com.example.yumyum.databinding.IngredientItemViewBinding


class IngredientsAdapter(private val ingredients: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val binding = IngredientItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IngredientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val ingredient = ingredients[position]
        Log.d("IngredientsFragment", "ingredients adapter $ingredient")
        holder.bind(ingredient)
    }

    override fun getItemCount(): Int = ingredients.size

    class IngredientViewHolder(private val binding: IngredientItemViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(ingredient: Ingredient) {
            binding.ingredientTitle.text = ingredient.title
            binding.ingredientMeasurement.text = ingredient.measurement
        }
    }
}
data class Ingredient(val title: String, val measurement: String)
