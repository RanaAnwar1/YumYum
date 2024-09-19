package com.example.yumyum.ui.secondactivity.mealdetailsscreen

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.yumyum.R
import com.example.yumyum.data.repository.MealsRepositoryImpl
import com.example.yumyum.data.source.local.ApplicationDatabase
import com.example.yumyum.data.source.remote.RetrofitClient
import com.example.yumyum.ui.Resource
import com.example.yumyum.ui.secondactivity.MealViewModel
import com.example.yumyum.ui.secondactivity.MealViewModelFactory
import com.example.yumyum.util.Constant
import com.google.android.material.snackbar.Snackbar
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
    private lateinit var progressBar:ProgressBar

    private val mealDetailsViewModel: MealViewModel by viewModels {
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
        progressBar = view.findViewById(R.id.progressBar)
        val mealId = arguments?.getString("mealId")
        mealId?.let {
            if (checkInternet())
                mealDetailsViewModel.fetchMealDetails(it)
            else
                Toast.makeText(requireContext(), "No internet connection", Toast.LENGTH_SHORT).show()
        }
//        mealDetailsViewModel.mealDetails.observe(viewLifecycleOwner) { meal ->
//            mealDetailsTitle.text = meal.meals[0].strMeal
//            mealDetailsCategory.text = meal.meals[0].strCategory
//            Glide.with(this).load(meal.meals[0].strMealThumb).into(mealDetailsCoverPhoto)
//        }
        mealDetailsViewModel.mealDetails.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    progressBar.visibility = View.GONE
                    resource.data?.let { meal ->
                        mealDetailsTitle.text = meal.meals[0].strMeal
                        mealDetailsCategory.text = meal.meals[0].strCategory
                        Glide.with(this).load(meal.meals[0].strMealThumb).into(mealDetailsCoverPhoto)
                    }
                }
                is Resource.Error -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), resource.message ?: "Error loading details", Toast.LENGTH_SHORT).show()
                }
            }
        }
        if (checkInternet())
            mealDetailsViewModel.getFavoriteMealIds()
        else
            Toast.makeText(requireContext(),"No internet connection",Toast.LENGTH_SHORT).show()
        mealDetailsViewModel.favoriteMealIds.observe(viewLifecycleOwner) { favoriteMealIds ->
            val isFavorite = favoriteMealIds.contains(mealId)
            faviconbtn.setImageResource(
                if (isFavorite) R.drawable.baseline_favorite_24
                else R.drawable.baseline_favorite_border_24
            )
        }
        faviconbtn.setOnClickListener {
            if (mealId != null) {
                mealDetailsViewModel.favoriteMealIds.observe(viewLifecycleOwner) { favoriteMealIds ->
                    val isFavorite = favoriteMealIds.contains(mealId)
                    faviconbtn.setImageResource(
                        if (isFavorite) {
                            if(checkInternet())
                                mealDetailsViewModel.deleteMealFromFavorites(Constant.USER_NAME, mealId)
                            else
                                Toast.makeText(requireContext(), "No internet connection", Toast.LENGTH_SHORT).show()
                            R.drawable.baseline_favorite_border_24
                        } else {
                            if(checkInternet())
                                mealDetailsViewModel.insertFavoriteMealById(Constant.USER_NAME, mealId)
                            else
                                Toast.makeText(requireContext(), "No internet connection", Toast.LENGTH_SHORT).show()
                            R.drawable.baseline_favorite_24
                        }
                    )
                }
                mealDetailsViewModel.getFavoriteMealIds()
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

    private fun checkInternet():Boolean{
        val connManger = activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connManger.activeNetworkInfo
        return (networkInfo != null && networkInfo.isConnected)
    }

}