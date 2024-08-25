package com.example.yumyum.ui.secondactivity.mealsviewscreen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yumyum.data.model.Meal
import com.example.yumyum.data.repository.MealsRepositoryImpl
import com.example.yumyum.data.source.local.ApplicationDatabase
import com.example.yumyum.data.source.remote.RetrofitClient
import com.example.yumyum.databinding.FragmentMealsViewBinding
import com.example.yumyum.ui.secondactivity.MealViewModel
import com.example.yumyum.ui.secondactivity.MealViewModelFactory
import com.example.yumyum.util.Constant
import com.example.yumyum.util.FilterType


class MealsViewFragment : Fragment() {

    lateinit var binding:FragmentMealsViewBinding
    lateinit var adapter: MealAdapter
    private val args: MealsViewFragmentArgs by navArgs()
    private val viewModel:MealViewModel by viewModels {
        MealViewModelFactory(MealsRepositoryImpl(RetrofitClient,ApplicationDatabase.getInstance(requireContext())))
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMealsViewBinding.inflate(layoutInflater,container,false)
        setupMealAdapter()
        setTheMealSource()
        return binding.root
    }

    private fun setupMealAdapter(){
        viewModel.getFavoriteMealIds()
        adapter = MealAdapter { mealId ->
            val isFavorite = viewModel.favoriteMealIds.value?.contains(mealId) ?: false
            if (isFavorite) {
                viewModel.deleteMealFromFavorites(Constant.USER_NAME, mealId)
            } else {
                viewModel.insertFavoriteMealById(Constant.USER_NAME, mealId)
            }
        }
        binding.mealsRecycler.adapter = adapter
        binding.mealsRecycler.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setTheMealSource(){
        val filterType = args.filterType
        val filterWord = args.filterWord
        if(filterType == FilterType.AREA.ordinal)
            viewModel.getMealsByArea(filterWord)
        else
            viewModel.getMealsByCategory(filterWord)
        viewModel.meals.observe(viewLifecycleOwner) { meals ->
            viewModel.favoriteMealIds.observe(viewLifecycleOwner) { favoriteMealIds ->
                adapter.setList(meals, favoriteMealIds)
            }
        }
    }

}