package com.example.yumyum.ui.secondactivity

import android.util.Log
import androidx.annotation.LongDef
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.yumyum.data.model.Area
import com.example.yumyum.data.model.Areas
import com.example.yumyum.data.model.Categories
import com.example.yumyum.data.model.FavoriteMeal
import com.example.yumyum.data.model.Meal
import com.example.yumyum.data.model.ReturnedMeal
import com.example.yumyum.data.model.SearchedMeal
import com.example.yumyum.data.model.relation.UserMealCrossRef
import com.example.yumyum.data.repository.MealsRepository
import com.example.yumyum.data.repository.UserRepository
import com.example.yumyum.ui.firstactivity.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MealViewModel(
    private val repo:MealsRepository
):ViewModel() {
    private val _areas = MutableLiveData<Areas>()
    val areas:LiveData<Areas> get() = _areas

    private val _categories = MutableLiveData<Categories>()
    val categories:LiveData<Categories> get() = _categories

    private val _meals = MutableLiveData<List<Meal>>()
    val meals:LiveData<List<Meal>> get() = _meals

    private val _favMeals = MutableLiveData<List<FavoriteMeal>>()
    val favMeals:LiveData<List<FavoriteMeal>> get() = _favMeals

    private val _searchResults = MutableLiveData<SearchedMeal>()
    val searchResults: LiveData<SearchedMeal> get() = _searchResults

    private val queryChannel = Channel<String>(Channel.CONFLATED)

    private val _mealDetails = MutableLiveData<ReturnedMeal>()
    val mealDetails: LiveData<ReturnedMeal> get() = _mealDetails

//    private val _mealsWithFavoriteStatus = MutableLiveData<List<FavoriteMeal>>()
//    val mealsWithFavoriteStatus: LiveData<List<FavoriteMeal>> get() = _mealsWithFavoriteStatus

    init {
        viewModelScope.launch(Dispatchers.IO) {
            queryChannel.receiveAsFlow()
                .debounce(300)
                .distinctUntilChanged()
                .collect { query ->
                    searchMealByName(query)
                }
        }
    }
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

    fun insertFavoriteMealById(username:String,mealId:String){
        viewModelScope.launch(Dispatchers.IO) {
            val result = async { repo.getMealById(mealId) }
            val meal = result.await()
            Log.d("checking_database",meal.toString())
            repo.insertFavoriteMeal(meal.meals[0])
            val crossRef = UserMealCrossRef(username,meal.meals[0].idMeal)
            repo.insertCrossRef(crossRef)
        }
    }

    fun getFavoriteMealsByUsername(username: String){
        viewModelScope.launch(Dispatchers.IO) {
            val result = async { repo.getMealsByUsername(username) }
            _favMeals.postValue(result.await())
        }
    }

    fun searchMealByName(mealName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = async { repo.searchMealByName(mealName) }
                _searchResults.postValue(result.await())
                Log.d("viewmodel_logging", result.await().toString())
            } catch (e: Exception) {
                Log.e("viewmodel_logging", "Error searching for meal: ${e.message}")
            }
        }
    }
    fun submitQuery(query: String) {
        queryChannel.trySend(query)
    }
    fun fetchMealDetails(mealId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = async { repo.getMealById(mealId) }
                _mealDetails.postValue(result.await())
                Log.d("viewmodel_logging", result.await().meals[0].toString())
            } catch (e: Exception) {
                Log.e("viewmodel_logging", "Error fetching meal details: ${e.message}")
            }
        }
    }
    fun deleteMealFromFavorites(username: String, mealId: String)
    {
        viewModelScope.launch (Dispatchers.IO){
            val result = async { repo.getMealById(mealId) }
            val meal = result.await()
            repo.deleteFavoriteMeal(meal.meals[0])
            repo.deleteCrossRef(username,meal.meals[0].idMeal)
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