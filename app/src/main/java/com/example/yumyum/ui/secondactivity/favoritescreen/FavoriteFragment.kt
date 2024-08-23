package com.example.yumyum.ui.secondactivity.favoritescreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yumyum.R
import com.example.yumyum.data.repository.MealsRepositoryImpl
import com.example.yumyum.data.source.local.ApplicationDatabase
import com.example.yumyum.data.source.remote.RetrofitClient
import com.example.yumyum.databinding.FragmentFavoriteBinding
import com.example.yumyum.ui.secondactivity.MealViewModel
import com.example.yumyum.ui.secondactivity.MealViewModelFactory
import com.example.yumyum.util.Constant

class FavoriteFragment : Fragment() {

    private val viewModel:MealViewModel by viewModels {
        MealViewModelFactory(
            MealsRepositoryImpl(RetrofitClient,ApplicationDatabase.getInstance(requireContext()))
        )
    }
    lateinit var binding:FragmentFavoriteBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(layoutInflater,container,false)
        val adapter = FavAdapter{

        }
        viewModel.getFavoriteMealsByUsername(Constant.USER_NAME)
        viewModel.favMeals.observe(viewLifecycleOwner){ favMeals ->
            adapter.setList(favMeals)
        }
        binding.favRecycler.adapter = adapter
        binding.favRecycler.layoutManager = LinearLayoutManager(requireContext())
        return binding.root
    }

}