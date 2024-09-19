package com.example.yumyum.ui.secondactivity.searchscreen

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yumyum.R
import com.example.yumyum.data.repository.MealsRepositoryImpl
import com.example.yumyum.data.source.local.ApplicationDatabase
import com.example.yumyum.data.source.remote.RetrofitClient
import com.example.yumyum.databinding.FragmentSearchBinding
import com.example.yumyum.ui.Resource
import com.example.yumyum.ui.secondactivity.MealViewModel
import com.example.yumyum.ui.secondactivity.MealViewModelFactory
import com.example.yumyum.ui.secondactivity.mealsviewscreen.MealAdapter
import com.example.yumyum.util.Constant
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class SearchFragment : Fragment() {

    private lateinit var mealSearchAdapter: MealSearchAdapter
    private lateinit var binding: FragmentSearchBinding
    private val searchViewModel: MealViewModel by viewModels {
        MealViewModelFactory(
            MealsRepositoryImpl(RetrofitClient, ApplicationDatabase.getInstance(requireContext()))
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecycler()
        setSearchBar()

        binding.recyclerViewSearchedMeals.adapter = mealSearchAdapter
        if(checkInternet())
            searchViewModel.getFavoriteMealIds()
        else
            Toast.makeText(requireContext(), "No Internet connection", Toast.LENGTH_SHORT).show()

//        searchViewModel.searchResults.observe(viewLifecycleOwner, Observer { meals ->
//            val mealsList = meals?.meals ?: emptyList()
//            searchViewModel.favoriteMealIds.observe(viewLifecycleOwner) { favoriteMealIds ->
//                mealSearchAdapter.updateMeals(mealsList, favoriteMealIds.toSet())
//            }
//        })
        searchViewModel.searchResults.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val mealsList = resource.data?.meals ?: emptyList()
                    searchViewModel.favoriteMealIds.observe(viewLifecycleOwner) { favoriteMealIds ->
                        mealSearchAdapter.updateMeals(mealsList, favoriteMealIds.toSet())
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), resource.message ?: "Error fetching meals", Toast.LENGTH_SHORT).show()
                }
            }
        })


    }
    private fun setSearchBar(){
        binding.materialSearchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                if (checkInternet())
                    searchViewModel.submitQuery(query)
                else
                    Toast.makeText(requireContext(), "No Internet connection", Toast.LENGTH_SHORT).show()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

    }
    private fun setRecycler(){
        binding.recyclerViewSearchedMeals.layoutManager = LinearLayoutManager(context)
        mealSearchAdapter = MealSearchAdapter(emptyList()) { mealId ->
            val isFavorite = searchViewModel.favoriteMealIds.value?.contains(mealId) ?: false
            if (isFavorite) {
                searchViewModel.deleteMealFromFavorites(Constant.USER_NAME, mealId)
            } else {
                searchViewModel.insertFavoriteMealById(Constant.USER_NAME, mealId)
            }
        }
        binding.recyclerViewSearchedMeals.adapter = mealSearchAdapter
    }

    private fun checkInternet():Boolean{
        val connManger = activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connManger.activeNetworkInfo
        return (networkInfo != null && networkInfo.isConnected)
    }
}
