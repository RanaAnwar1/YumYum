package com.example.yumyum.ui.secondactivity.mealdetailsscreen.ingredientsfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yumyum.R

class IngredientsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var ingredientsAdapter: IngredientsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ingredients, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewIngredients)

        val ingredients = listOf(
            Ingredient("Tortilla Chips", "2"),
            Ingredient("Avocado", "1"),
            Ingredient("Red Cabbage", "9"),
            Ingredient("Peanuts", "1"),
        )

        ingredientsAdapter = IngredientsAdapter(ingredients)
        recyclerView.adapter = ingredientsAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }
}