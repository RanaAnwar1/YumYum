package com.example.yumyum.ui.secondactivity.homescreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.yumyum.R
import com.example.yumyum.data.model.Area
import com.example.yumyum.data.repository.MealsRepositoryImpl
import com.example.yumyum.data.source.local.ApplicationDatabase
import com.example.yumyum.data.source.remote.ApiService
import com.example.yumyum.data.source.remote.RetrofitClient
import com.example.yumyum.databinding.FragmentHomeBinding
import com.example.yumyum.ui.secondactivity.MealViewModel
import com.example.yumyum.ui.secondactivity.MealViewModelFactory
import com.example.yumyum.util.Constant
import com.example.yumyum.util.FilterType

class HomeFragment : Fragment() {


    lateinit var binding: FragmentHomeBinding
    private val viewModel:MealViewModel by viewModels {
        MealViewModelFactory(MealsRepositoryImpl(RetrofitClient,ApplicationDatabase.getInstance(requireContext())))
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
//        (activity as? AppCompatActivity)?.supportActionBar?.title = "Hello, Rana"
        binding = FragmentHomeBinding.inflate(layoutInflater,container,false)
        val areaAdapter = AreaAdapter{ area ->
            val action = HomeFragmentDirections.actionNavigationHomeToMealsViewFragment(FilterType.AREA.ordinal,area)
            findNavController().navigate(action)
        }
        viewModel.getAllAreas()
        viewModel.areas.observe(viewLifecycleOwner){
            areaAdapter.setList(it.meals)
        }
        binding.areaRecycler.adapter = areaAdapter
        binding.areaRecycler.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        val categoryAdapter = CategoryAdapter{category ->
            val action = HomeFragmentDirections.actionNavigationHomeToMealsViewFragment(FilterType.CATEGORY.ordinal,category)
            findNavController().navigate(action)
        }
        viewModel.getAllCategories()
        viewModel.categories.observe(viewLifecycleOwner){categories ->
            categoryAdapter.setList(categories.categories)
        }
        binding.categoryRecycler.adapter = categoryAdapter
        binding.categoryRecycler.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
    }
}