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
import com.example.yumyum.ui.secondactivity.MealViewModel
import com.example.yumyum.ui.secondactivity.MealViewModelFactory
import com.example.yumyum.util.Constant

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
                Toast.makeText(requireContext(),"No internet",Toast.LENGTH_SHORT).show()
        }
        recyclerViewFavorites = binding.recyclerViewFavorites
        recyclerViewFavorites.layoutManager = GridLayoutManager(context, 2)
        if (checkInternet())
            viewModel.getFavoriteMealsByUsername(Constant.USER_NAME)
        else
            Toast.makeText(requireContext(),"No internet",Toast.LENGTH_SHORT).show()
        viewModel.favMeals.observe(viewLifecycleOwner){ favMeals ->
            adapter.favMealList = favMeals
        }
        binding.recyclerViewFavorites.adapter = adapter
        binding.recyclerViewFavorites.layoutManager = LinearLayoutManager(requireContext())
        return binding.root
    }

    private fun checkInternet():Boolean{
        val connManger = activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connManger.activeNetworkInfo
        return (networkInfo != null && networkInfo.isConnected)
    }

}