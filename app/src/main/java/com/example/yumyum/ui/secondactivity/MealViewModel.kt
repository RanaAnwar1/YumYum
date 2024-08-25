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
import com.example.yumyum.util.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
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

    private val _favoriteMealIds = MutableLiveData<Set<String>>()
    val favoriteMealIds: LiveData<Set<String>> get() = _favoriteMealIds

    init {
        viewModelScope.launch(Dispatchers.IO) {
            queryChannel.receiveAsFlow()
                .debounce(1000)
                .distinctUntilChanged()
                .collect { query ->
                    searchMealByName(query)
                }
        }
    }
    fun getAllAreas(){
        viewModelScope.launch (Dispatchers.IO){
            try {
            val result = async { repo.getAreas() }
            _areas.postValue(result.await())
            } catch (e: Exception) {
                Log.e("MealViewModel", "Error retrieving all areas: ${e.message}")
            }
        }
    }

    fun getAllCategories(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
            val result = async { repo.getCategories() }
            _categories.postValue(result.await())
            Log.d("viewmodel_logging",result.await().categories.toString())
            } catch (e: Exception) {
                Log.e("MealViewModel", "Error retrieving all categories: ${e.message}")
            }
        }
    }

    fun getMealsByArea(area: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
            val result = async { repo.getMealsByAreas(area) }
            _meals.postValue(result.await())
            Log.d("viewmodel_logging",result.await().toString())
            } catch (e: Exception) {
                Log.e("MealViewModel", "Error retrieving meals by area: ${e.message}")
            }

        }
    }

    fun getMealsByCategory(category:String){
        viewModelScope.launch (Dispatchers.IO){
            try {
            val result = async { repo.getMealsByCategories(category) }
            _meals.postValue(result.await())
            Log.d("viewmodel_logging",result.await().toString())
            } catch (e: Exception) {
                Log.e("MealViewModel", "Error retrieving meals by category: ${e.message}")
            }
        }
    }

    fun insertFavoriteMealById(username:String,mealId:String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = async { repo.getMealById(mealId) }
                val meal = result.await()
                Log.d("checking_database", meal.toString())
                repo.insertFavoriteMeal(meal.meals[0])
                val crossRef = UserMealCrossRef(username, meal.meals[0].idMeal)
                repo.insertCrossRef(crossRef)
            }catch (e: Exception) {
            Log.e("MealViewModel", "Error inserting favorites: ${e.message}")
            }
        }
    }

    fun getFavoriteMealsByUsername(username: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val meals = async {repo.getMealsByUsername(username)}
                _favMeals.postValue(meals.await())
            } catch (e: Exception) {
                Log.e("MealViewModel", "Error retrieving favorites: ${e.message}")
            }
        }
    }

    fun searchMealByName(mealName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = async { repo.searchMealByName(mealName) }
                _searchResults.postValue(result.await())
                Log.d("MealViewModel", result.await().toString())
            } catch (e: Exception) {
                Log.e("MealViewModel", "Error searching for meal: ${e.message}")
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
                Log.d("MealViewModel", result.await().meals[0].toString())
            } catch (e: Exception) {
                Log.e("MealViewModel", "Error fetching meal details: ${e.message}")
            }
        }
    }
    fun deleteMealFromFavorites(username: String, mealId: String)
    {
        viewModelScope.launch (Dispatchers.IO){
            try {
                val result = async { repo.getMealById(mealId) }
                val meal = result.await()
                repo.deleteFavoriteMeal(meal.meals[0])
                repo.deleteCrossRef(username,meal.meals[0].idMeal)
                getFavoriteMealsByUsername(username)
            } catch (e: Exception) {
                Log.e("MealViewModel", "Error deleting from favorites: ${e.message}")
            }
        }
    }

    fun getFavoriteMealIds() {
        viewModelScope.launch {
            try {
                val ids = async {repo.getFavoriteMealIdsByUsername(Constant.USER_NAME)}
                _favoriteMealIds.postValue(ids.await().toSet())
            } catch (e: Exception) {
                Log.e("MealViewModel", "Error fetching favorite meal ids: ${e.message}")
            }
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