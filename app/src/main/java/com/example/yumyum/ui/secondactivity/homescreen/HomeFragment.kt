package com.example.yumyum.ui.secondactivity.homescreen

import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yumyum.R
import com.example.yumyum.data.repository.MealsRepositoryImpl
import com.example.yumyum.data.source.local.ApplicationDatabase
import com.example.yumyum.data.source.remote.RetrofitClient
import com.example.yumyum.databinding.FragmentHomeBinding
import com.example.yumyum.ui.Resource
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
        val nightModeFlags =
            requireContext().resources.configuration.uiMode and
                    Configuration.UI_MODE_NIGHT_MASK
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> {
                binding.gradientHome.setImageResource(R.drawable.heavymetal_gradient)
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                binding.gradientHome.setImageResource(R.drawable.mystic_gradient)
            }
        }
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
        {
            showToast("No internet connection")
            //showSnackbar("No internet connection")
        }
//            Snackbar.make(binding.root,"no Internet connection",Snackbar.LENGTH_SHORT).show()
//            viewModel.areas.observe(viewLifecycleOwner){ areas ->
//                areaAdapter.areas = areas.meals
//        }
        viewModel.areas.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is Resource.Loading -> showLoading()
                is Resource.Success -> {
                    hideLoading()
                    areaAdapter.areas = resource.data?.meals ?: emptyList()
                }
                is Resource.Error -> {
                    hideLoading()
                    showToast(resource.message ?: "Error fetching areas")
                }
            }
        })
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
        {
            showToast("No internet connection")
            //showSnackbar("No internet connection")
//            Snackbar.make(binding.root,"no Internet connection",Snackbar.LENGTH_SHORT).show()
//        viewModel.categories.observe(viewLifecycleOwner){categories ->
//            categoryAdapter.categories = categories.categories
        }
        viewModel.categories.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is Resource.Loading -> showLoading()
                is Resource.Success -> {
                    hideLoading()
                    categoryAdapter.categories = resource.data?.categories ?: emptyList()
                }
                is Resource.Error -> {
                    hideLoading()
                    showToast(resource.message ?: "Error fetching categories")
                    //showSnackbar(resource.message ?: "Error fetching categories")
                }
            }
        })
        binding.categoryRecycler.adapter = categoryAdapter
        binding.categoryRecycler.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun checkInternet():Boolean{
        val connManger = activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connManger.activeNetworkInfo
        return (networkInfo != null && networkInfo.isConnected)
    }
//    private fun showSnackbar(message: String) {
//        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
//    }
private fun showToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }


}