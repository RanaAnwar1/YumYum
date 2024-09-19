package com.example.yumyum.ui.secondactivity.favoritescreen

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yumyum.R
import com.example.yumyum.data.repository.MealsRepositoryImpl
import com.example.yumyum.data.source.local.ApplicationDatabase
import com.example.yumyum.data.source.remote.RetrofitClient
import com.example.yumyum.databinding.FragmentFavoriteBinding
import com.example.yumyum.ui.Resource
import com.example.yumyum.ui.secondactivity.MealViewModel
import com.example.yumyum.ui.secondactivity.MealViewModelFactory
import com.example.yumyum.util.Constant
import com.google.android.material.snackbar.Snackbar

class FavoriteFragment : Fragment() {

    private lateinit var recyclerViewFavorites: RecyclerView

    private val viewModel:MealViewModel by viewModels {
        MealViewModelFactory(
            MealsRepositoryImpl(RetrofitClient,ApplicationDatabase.getInstance(requireContext()))
        )
    }
    lateinit var binding:FragmentFavoriteBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(layoutInflater,container,false)
        val adapter = FavAdapter{ mealId ->
            if(checkInternet())
                viewModel.deleteMealFromFavorites(Constant.USER_NAME, mealId)
            else
                showToast("No Internet connection")
        }
//        recyclerViewFavorites = binding.recyclerViewFavorites
//        recyclerViewFavorites.layoutManager = GridLayoutManager(context, 2)
        if (checkInternet())
            viewModel.getFavoriteMealsByUsername(Constant.USER_NAME)
        else
            showToast("No Internet Connection")
//        viewModel.favMeals.observe(viewLifecycleOwner){ favMeals ->
//            adapter.favMealList = favMeals
//        }
        viewModel.favMeals.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.recyclerViewFavorites.visibility = View.GONE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerViewFavorites.visibility = View.VISIBLE
                    resource.data?.let { favMeals ->
                        adapter.favMealList = favMeals
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    showToast(resource.message ?: "Error while loading Favorites")                }
            }
        }
        binding.recyclerViewFavorites.adapter = adapter
        binding.recyclerViewFavorites.layoutManager = LinearLayoutManager(requireContext())
        return binding.root
    }
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
    private fun checkInternet():Boolean{
        val connManger = activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connManger.activeNetworkInfo
        return (networkInfo != null && networkInfo.isConnected)
    }

}