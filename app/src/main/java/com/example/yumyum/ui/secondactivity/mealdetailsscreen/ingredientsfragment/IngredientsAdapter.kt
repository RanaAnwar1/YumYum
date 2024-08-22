package com.example.yumyum.ui.secondactivity.mealdetailsscreen.ingredientsfragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.yumyum.R


class IngredientsAdapter(private val ingredients: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.ingredient_item_view, parent, false)
        return IngredientViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val ingredient = ingredients[position]
        holder.bind(ingredient)
    }

    override fun getItemCount(): Int = ingredients.size

    class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ingredientTitle: TextView = itemView.findViewById(R.id.ingredientTitle)
        private val ingredientMeasurement: TextView = itemView.findViewById(R.id.ingredientMeasurement)

        fun bind(ingredient: Ingredient) {
            ingredientTitle.text = ingredient.title
            ingredientMeasurement.text = ingredient.measurement
        }
    }
}
data class Ingredient(val title: String, val measurement: String)
