package com.example.yumyum.ui.secondactivity.mealdetailsscreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.yumyum.R
import com.example.yumyum.data.repository.MealsRepositoryImpl
import com.example.yumyum.data.source.local.ApplicationDatabase
import com.example.yumyum.data.source.remote.RetrofitClient
import com.example.yumyum.ui.secondactivity.MealViewModel
import com.example.yumyum.ui.secondactivity.MealViewModelFactory
import com.example.yumyum.util.Constant
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MealDetailsFragment : Fragment() {
    private lateinit var mealId: String
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var mealDetailsTitle: TextView
    private lateinit var mealDetailsCategory: TextView
    private lateinit var mealDetailsCoverPhoto: ImageView
    private lateinit var faviconbtn: ImageView


    private val MealDetailsViewModel: MealViewModel by viewModels {
        MealViewModelFactory(
            MealsRepositoryImpl(RetrofitClient, ApplicationDatabase.getInstance(requireContext()))
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mealId = it.getString("mealId") ?: ""
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_meal_details, container, false)
        viewPager = view.findViewById(R.id.viewPager)
        tabLayout = view.findViewById(R.id.tabLayout)
        mealDetailsTitle = view.findViewById(R.id.mealDetailsTitle)
        mealDetailsCategory = view.findViewById(R.id.mealDetailsCategory)
        mealDetailsCoverPhoto = view.findViewById(R.id.mealDetailsCoverPhoto)
        faviconbtn = view.findViewById(R.id.btnFavorite)
        val mealId = arguments?.getString("mealId")
        mealId?.let {
            MealDetailsViewModel.fetchMealDetails(it)
            Log.d("IngredientsFragment", "Mealsdetails oncreate view")
        }
        MealDetailsViewModel.mealDetails.observe(viewLifecycleOwner) { meal ->
            mealDetailsTitle.text = meal.meals[0].strMeal
            mealDetailsCategory.text = meal.meals[0].strCategory
            Glide.with(this).load(meal.meals[0].strMealThumb).into(mealDetailsCoverPhoto)
        }
        MealDetailsViewModel.getFavoriteMealIds()
        MealDetailsViewModel.favoriteMealIds.observe(viewLifecycleOwner) { favoriteMealIds ->
            val isFavorite = favoriteMealIds.contains(mealId)
            faviconbtn.setImageResource(
                if (isFavorite) R.drawable.baseline_favorite_24
                else R.drawable.baseline_favorite_border_24
            )
        }
        faviconbtn.setOnClickListener {
            if (mealId != null) {
                MealDetailsViewModel.favoriteMealIds.observe(viewLifecycleOwner) { favoriteMealIds ->
                    val isFavorite = favoriteMealIds.contains(mealId)
                    faviconbtn.setImageResource(
                        if (isFavorite) {
                            MealDetailsViewModel.deleteMealFromFavorites(Constant.USER_NAME, mealId)
                            R.drawable.baseline_favorite_border_24
                        } else {
                            MealDetailsViewModel.insertFavoriteMealById(Constant.USER_NAME, mealId)
                            R.drawable.baseline_favorite_24
                        }
                    )
                }
            }
        }

        val adapter = MealDetailsPagerAdapter(this)
        viewPager.adapter = adapter
        Log.d("IngredientsFragment", "$adapter")
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Ingredients"
                1 -> "Instructions"
                else -> throw IllegalStateException("Unexpected position $position")
            }
        }.attach()

        return view
    }


}