package com.example.yumyum.ui.secondactivity.mealsviewscreen

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yumyum.data.model.Meal
import com.example.yumyum.data.repository.MealsRepositoryImpl
import com.example.yumyum.data.source.local.ApplicationDatabase
import com.example.yumyum.data.source.remote.RetrofitClient
import com.example.yumyum.databinding.FragmentMealsViewBinding
import com.example.yumyum.ui.Resource
import com.example.yumyum.ui.secondactivity.MealViewModel
import com.example.yumyum.ui.secondactivity.MealViewModelFactory
import com.example.yumyum.util.Constant
import com.example.yumyum.util.FilterType
import com.google.android.material.snackbar.Snackbar


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
            var isFavorite = false
            viewModel.favoriteMealIds.observe(viewLifecycleOwner){ favIds ->
                isFavorite = favIds.contains(mealId)
            }
            if (isFavorite) {
                if (checkInternet())
                {
                    viewModel.deleteMealFromFavorites(Constant.USER_NAME, mealId)
                }
                else
                    Toast.makeText(requireContext(), "No internet connection", Toast.LENGTH_LONG).show()            } else {
                if (checkInternet())
                    viewModel.insertFavoriteMealById(Constant.USER_NAME, mealId)
                else
                    Toast.makeText(requireContext(), "No internet connection", Toast.LENGTH_LONG).show()
            }
        }
        binding.mealsRecycler.adapter = adapter
        binding.mealsRecycler.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setTheMealSource(){
        val filterType = args.filterType
        val filterWord = args.filterWord
        if (checkInternet())
            viewModel.getMeals(filterWord,filterType)
        else
            Toast.makeText(requireContext(), "No internet connection", Toast.LENGTH_LONG).show()//        viewModel.meals.observe(viewLifecycleOwner) { meals ->
//            adapter.meals = meals
//        }
        viewModel.meals.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    Log.d("MealsViewFragment", "Loading meals...")
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    resource.data?.let { meals ->
                        adapter.meals = meals
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), resource.message ?: "Error loading meals", Toast.LENGTH_LONG).show()
                }
            }
        }
        viewModel.favoriteMealIds.observe(viewLifecycleOwner) { favoriteMealIds ->
            adapter.favoriteMealIds = favoriteMealIds
        }
//        viewModel.meals.observe(viewLifecycleOwner) { meals ->
//            adapter.setList(meals, viewModel.favoriteMealIds.value ?: emptySet())
//        }
//
//        viewModel.favoriteMealIds.observe(viewLifecycleOwner) { favoriteMealIds ->
//            adapter.setList(viewModel.meals.value ?: emptyList(), favoriteMealIds)
//        }
    }

    private fun checkInternet():Boolean{
        val connManger = activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connManger.activeNetworkInfo
        return (networkInfo != null && networkInfo.isConnected)
    }

}