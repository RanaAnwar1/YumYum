package com.example.yumyum.ui.secondactivity.homescreen

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.yumyum.data.repository.MealsRepositoryImpl
import com.example.yumyum.data.source.local.ApplicationDatabase
import com.example.yumyum.data.source.remote.RetrofitClient
import com.example.yumyum.databinding.FragmentHomeBinding
import com.example.yumyum.ui.secondactivity.MealViewModel
import com.example.yumyum.ui.secondactivity.MealViewModelFactory
import com.example.yumyum.util.FilterType
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {


    lateinit var binding: FragmentHomeBinding
    private val viewModel:MealViewModel by viewModels {
        MealViewModelFactory(MealsRepositoryImpl(RetrofitClient,ApplicationDatabase.getInstance(requireContext())))
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Hello"
        binding = FragmentHomeBinding.inflate(layoutInflater,container,false)
        setupAreaAdapter()
        setupCategoryAdapter()
        return binding.root
    }

    private fun setupAreaAdapter(){
        val areaAdapter = AreaAdapter{ area ->
            HomeFragmentDirections
                .actionNavigationHomeToMealsViewFragment(FilterType.AREA.ordinal,area)
                .apply {
                    findNavController().navigate(this)
                }
        }
        if(checkInternet())
            viewModel.getAllAreas()
        else
            Toast.makeText(requireContext(),"no internet connection",Toast.LENGTH_SHORT).show()
        viewModel.areas.observe(viewLifecycleOwner){ areas ->
            areaAdapter.areas = areas.meals
        }
        binding.areaRecycler.adapter = areaAdapter
        binding.areaRecycler.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
    }

    private fun setupCategoryAdapter(){
        val categoryAdapter = CategoryAdapter{category ->
            HomeFragmentDirections
                .actionNavigationHomeToMealsViewFragment(FilterType.CATEGORY.ordinal,category)
                .apply {
                    findNavController().navigate(this)
                }
        }
        if(checkInternet())
            viewModel.getAllCategories()
        else
            Toast.makeText(requireContext(),"no internet connection",Toast.LENGTH_SHORT).show()
        viewModel.categories.observe(viewLifecycleOwner){categories ->
            categoryAdapter.categories = categories.categories
        }
        binding.categoryRecycler.adapter = categoryAdapter
        binding.categoryRecycler.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun checkInternet():Boolean{
        val connManger = activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connManger.activeNetworkInfo
        return (networkInfo != null && networkInfo.isConnected)
    }
}