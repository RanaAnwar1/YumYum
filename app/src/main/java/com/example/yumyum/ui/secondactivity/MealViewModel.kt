package com.example.yumyum.ui.secondactivity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.yumyum.data.model.Area
import com.example.yumyum.data.model.Areas
import com.example.yumyum.data.model.Categories
import com.example.yumyum.data.model.Meal
import com.example.yumyum.data.repository.MealsRepository
import com.example.yumyum.data.repository.UserRepository
import com.example.yumyum.ui.firstactivity.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MealViewModel(
    val repo:MealsRepository
):ViewModel() {
    private val _areas = MutableLiveData<Areas>()
    val areas:LiveData<Areas> get() = _areas

    private val _categories = MutableLiveData<Categories>()
    val categories:LiveData<Categories> get() = _categories

    private val _meals = MutableLiveData<List<Meal>>()
    val meals:LiveData<List<Meal>> get() = _meals


    fun getAllAreas(){
        viewModelScope.launch (Dispatchers.IO){
            val result = async { repo.getAreas() }
            _areas.postValue(result.await())
        }
    }

    fun getAllCategories(){
        viewModelScope.launch(Dispatchers.IO) {
            val result = async { repo.getCategories() }
            _categories.postValue(result.await())
            Log.d("viewmodel_logging",result.await().categories.toString())
        }
    }

    fun getMealsByArea(area: String){
        viewModelScope.launch(Dispatchers.IO) {
            val result = async { repo.getMealsByAreas(area) }
            _meals.postValue(result.await())
            Log.d("viewmodel_logging",result.await().toString())

        }
    }

    fun getMealsByCategory(category:String){
        viewModelScope.launch (Dispatchers.IO){
            val result = async { repo.getMealsByCategories(category) }
            _meals.postValue(result.await())
            Log.d("viewmodel_logging",result.await().toString())
        }
    }

}

class MealViewModelFactory(
    private val repo: MealsRepository
): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MealViewModel(repo) as T
    }
}