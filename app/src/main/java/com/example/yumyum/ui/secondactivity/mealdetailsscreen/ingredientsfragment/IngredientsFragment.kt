package com.example.yumyum.ui.secondactivity.mealdetailsscreen.ingredientsfragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.yumyum.R
import com.example.yumyum.data.model.FavoriteMeal
import com.example.yumyum.data.repository.MealsRepositoryImpl
import com.example.yumyum.data.source.local.ApplicationDatabase
import com.example.yumyum.data.source.remote.RetrofitClient
import com.example.yumyum.databinding.FragmentIngredientsBinding
import com.example.yumyum.ui.secondactivity.MealViewModel
import com.example.yumyum.ui.secondactivity.MealViewModelFactory

class IngredientsFragment : Fragment() {

    private lateinit var recyclerViewIngredients: RecyclerView
    private val ingredientViewModel: MealViewModel by viewModels({requireParentFragment()}) {
        MealViewModelFactory(
            MealsRepositoryImpl(RetrofitClient, ApplicationDatabase.getInstance(requireContext()))
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ingredients, container, false)

        recyclerViewIngredients = view.findViewById(R.id.recyclerViewIngredients)

        ingredientViewModel.mealDetails.observe(viewLifecycleOwner) { meal ->
            if (meal != null) {
                Log.d("IngredientsFragment", "mealDetails observed: $meal")
                val ingredientsList = generateIngredientsList(meal.meals[0])
                val ingredientsAdapter = IngredientsAdapter(ingredientsList)
                Log.d("IngredientsFragment", "Meal details found: ${meal.meals[0]}")
                recyclerViewIngredients.adapter = ingredientsAdapter
                recyclerViewIngredients.layoutManager = LinearLayoutManager(context)
            } else {
                Log.d("IngredientsFragment", "No meal details found")
            }
        }
        Log.d("IngredientsFragment", "onCreateView called")
        return view
    }

    private fun generateIngredientsList(meal: FavoriteMeal): List<Ingredient> {
        val ingredients = mutableListOf<Ingredient>()

        for (i in 1..20) {
            val ingredientField = meal.javaClass.getDeclaredField("strIngredient$i")
            val measurementField = meal.javaClass.getDeclaredField("strMeasure$i")

            ingredientField.isAccessible = true
            measurementField.isAccessible = true

            val ingredient = ingredientField.get(meal) as? String
            val measurement = measurementField.get(meal) as? String

            if (ingredient.isNullOrEmpty() || measurement.isNullOrEmpty()) break
            ingredients.add(Ingredient(ingredient, measurement))
        }

        return ingredients
    }
}

