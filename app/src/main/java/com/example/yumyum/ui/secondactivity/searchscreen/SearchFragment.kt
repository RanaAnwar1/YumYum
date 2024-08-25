package com.example.yumyum.ui.secondactivity.searchscreen

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.yumyum.ui.secondactivity.MealViewModel
import com.example.yumyum.ui.secondactivity.MealViewModelFactory
import com.example.yumyum.ui.secondactivity.mealsviewscreen.MealAdapter
import com.example.yumyum.util.Constant
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


        mealSearchAdapter = MealSearchAdapter(emptyList()) { mealId ->
//            searchViewModel.insertFavoriteMealById(Constant.USER_NAME, mealId)
            val isFavorite = searchViewModel.favoriteMealIds.value?.contains(mealId) ?: false
            if (isFavorite) {
                searchViewModel.deleteMealFromFavorites(Constant.USER_NAME, mealId)
            } else {
                searchViewModel.insertFavoriteMealById(Constant.USER_NAME, mealId)
            }
        }
        binding.recyclerViewSearchedMeals.adapter = mealSearchAdapter
        searchViewModel.getFavoriteMealIds()
        searchViewModel.searchResults.observe(viewLifecycleOwner, Observer { meals ->
            val mealsList = meals?.meals ?: emptyList()
            searchViewModel.favoriteMealIds.observe(viewLifecycleOwner) { favoriteMealIds ->
                mealSearchAdapter.updateMeals(mealsList, favoriteMealIds.toSet())
            }
        })

    }
    private fun setSearchBar(){
        binding.materialSearchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                searchViewModel.submitQuery(query)
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
//            searchViewModel.insertFavoriteMealById(Constant.USER_NAME, mealId)
            val isFavorite = searchViewModel.favoriteMealIds.value?.contains(mealId) ?: false
            if (isFavorite) {
                searchViewModel.deleteMealFromFavorites(Constant.USER_NAME, mealId)
            } else {
                searchViewModel.insertFavoriteMealById(Constant.USER_NAME, mealId)
            }
        }
        binding.recyclerViewSearchedMeals.adapter = mealSearchAdapter
    }
}
